package EngineSkill.Drools;

import org.apache.commons.lang3.RandomStringUtils;
import org.drools.compiler.compiler.io.memory.MemoryFileSystem;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.drools.compiler.kie.builder.impl.KieFileSystemImpl;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.definition.KiePackage;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DroolsEngine {
    private static final Logger log= LoggerFactory.getLogger(DroolsEngine.class);
    private final KieServices ks = KieServices.Factory.get();
    private final KieFileSystemImpl kfs = (KieFileSystemImpl)ks.newKieFileSystem();
    private KieContainerImpl kieContainer;
    private KieBase kieBase;

    //测试所用变量
    public String engineName = "default";
    //测试所用的析构方法
    public DroolsEngine(String enginename){
        engineName = enginename;
    }
    //是否要编译的标志【kfs有变动就要编译，获取到kieContainer就不用】
    public boolean needBuildFlag;
    //全局锁
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    /**
     * 嵌套模式的单例
     * 线程安全
     * **/
    private DroolsEngine(){}

    private static class Holder{
        private static final DroolsEngine _instance = new DroolsEngine();
    }

    public static DroolsEngine getInstance(){
        return Holder._instance;
    }

    /**
     * 获取kieContainer
     */
    private KieContainer getKieContainer(){
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        kieBuilder.buildAll();
        Results results = kieBuilder.getResults();
        if(results.hasMessages(Message.Level.ERROR)){
            throw new RuntimeException(results.getMessages().toString());
        }
        KieRepository kieRepository = ks.getRepository();
        ReleaseId releaseId = kieRepository.getDefaultReleaseId();
        return ks.newKieContainer(releaseId);
    }


    /**
     * 编译
     */
    public void prepareEngine(){
        lock.lock();
        try{
            if(needBuildFlag){
                kieContainer = (KieContainerImpl)getKieContainer();
                kieBase = kieContainer.getKieBase();
                valid_in_kfs_and_kiebase();
            }else{
                //condition.await();
            }
        }catch (Exception e){
            log.error("Compile Exception->",e);
        }finally {
            lock.unlock();
        }

    }

    /**
     * 单例时使用的编译
     */
    public void prepareEngineSinglton(){
        kieContainer = (KieContainerImpl)getKieContainer();
        kieBase = kieContainer.getKieBase();
        valid_in_kfs_and_kiebase();
    }

    /**
     * 校验kfs和kiebase的规则
     */
    public void valid_in_kfs_and_kiebase(){
        log.info("=*=*=*=*=*=*=*=*=*=*=*Valid[start]=*=*=*=*=*=*=*=*=*=*=*");
        List<String> drls_kfs = getRules_fromKFS();
        List<String> drls_kiebase = getRules_fromkieBase();
        areTheySame(drls_kfs,drls_kiebase);
        log.info("=*=*=*=*=*=*=*=*=*=*=*Valid[end]=*=*=*=*=*=*=*=*=*=*=*");
    }

    /**
     * 以kiebase为准,校验内容是否一致
     */
    private Map<String,Object> areTheySame(List<String> drls_kfs,List<String> drls_kiebase){
        Map<String,Object> res = new HashMap<>();
        boolean isSameFlag = drls_kfs.toString().equals(drls_kiebase.toString());
        if(isSameFlag){
            res.put("ValidResult",true);
            log.info("ValidResult={}",isSameFlag);
        }else{
            res.put("ValidResult",false);
            log.error("They are NOT SAME,kfs={}, kiebase={}",drls_kfs,drls_kiebase);
        }
        return res;
    }

    /**
     * 添加单条规则 路径src/main/resources/drlname
     * @param content drl文件内容
     * @param drlname drl文件名
     */
    public void addDRLs(String content,String drlname){
        if(content!=null && !"".equals(content.trim())){
            String kieFilePath = "src/main/resources/" + drlname;
            //kfs.delete(kieFilePath);
            kfs.write(kieFilePath, content);
            log.info("[{} insert to KFS]add rule>>{} ",this.engineName,drlname);
        }
    }

    /**
     * 删除规则
     */
    public void deleteDrls(String drlname){
        String kieFilePath = "src/main/resources/" + drlname;
        kfs.delete(kieFilePath);
        log.info("[{} delete from KFS] rule >>{} ",this.engineName,drlname);
    }



    /**
     * 获取有状态的匹配会话句柄
     */
    public KieSession createStatefulSession() {
        return kieContainer.newKieSession();
    }


    /**
     * 获取kfs中的规则详情
     */
    private List<String> getRules_fromKFS(){
        List<String> res = new ArrayList<>();
        MemoryFileSystem mfs = kfs.getMfs();
        for(Map.Entry<String, byte[]> entry :mfs.getMap().entrySet()){
            String drlPath = entry.getKey();
            log.info("[{}]===>KFS's rule ={} ",this.engineName,drlPath);
            drlPath = drlPath.replace("src/main/resources/", "");
            res.add(drlPath);
        }
        //排序
        res.sort(Comparator.comparing(String::hashCode));
        return res;
    }

    /**
     * 获取kiebase(rete)中的规则详情
     */
    private List<String> getRules_fromkieBase(){
        List<String> res = new ArrayList<>();
        KiePackage[] kiePackages = kieBase.getKiePackages().toArray(new KiePackage[0]);
        for(KiePackage kiePackage:kiePackages){
            RuleImpl[] rules = kiePackage.getRules().toArray(new RuleImpl[0]);
            for(RuleImpl rule:rules){
                String name = rule.getName();
                log.info("[{}]=====>kieBase's rule={} ",this.engineName,name);

                name = name+".drl";
                res.add(name);
            }
        }
        //排序
        res.sort(Comparator.comparing(String::hashCode));
        return res;
    }


    /**
     * 执行匹配动作
     */
    public int runMatch(Object paramObject){
        KieSession ks = createStatefulSession();
        //插入到working memory中的fact对象句柄
        FactHandle handle = ks.insert(paramObject);
        int matchNum = -1;
        try {
            matchNum = ks.fireAllRules(
                    match -> {
                        log.info("MatchRuleName:{}" ,match.getRule().getName() );
                        return true;
                    }
            );
            //删除插入到working memory中的fact对象
            ks.delete(handle);
        }catch (Exception e){
            log.error("[runMatch ERROR]",e);
        }
        finally {
            if(null != ks){

                ks.dispose();
            }
        }
        return matchNum;
    }




/***************************功能测试***************************************/
    public static void main(String[] args){
        new ParallelMyTool().doParallel();
        //new ParallelMyTool().doSingleton();
    }

    /*=*==*==*==*==*==*==*==*=模拟并发*=*==*==*==*==*==*==*==*=*/

    /**
     * 使用多线程的并发类
     */
    static class ParallelMyTool{

        /**
         * 并发
         */
        private void doParallel(){
            int parallelismNum = 100000;
            for(int i=0;i<parallelismNum;i++){
                new Thread(()->{
                    String engineName = RandomStringUtils.randomAlphanumeric(2);
                    DroolsEngine droolsEngine =  new DroolsEngine(engineName);
                    Map<String,String> drls = createDrls(droolsEngine,3);
                    addDrlsToKFS(droolsEngine,drls);
                    buildEngine(droolsEngine);
/*                    List<String> list = createFact(droolsEngine,2);
                    match(droolsEngine,list);
                    removeDrlsFromKFS(droolsEngine,drls);
                    buildEngine(droolsEngine);*/
                }).start();
            }
        }

        /**
         * 使用单例的情况
         */
        private void doSingleton(){
            int parallelismNum = 2;
            for(int i=0;i<parallelismNum;i++){
                new Thread(()->{
                    DroolsEngine droolsEngine =  DroolsEngine.getInstance();
                    Map<String,String> drls = createDrls(droolsEngine,3);
                    addDrlsToKFSSinglton(droolsEngine,drls);
                    buildEngineSinglton(droolsEngine);
                    List<String> list = createFact(droolsEngine,2);
                    match(droolsEngine,list);
                    removeDrlsFromKFSSinglton(droolsEngine,drls);
                    buildEngineSinglton(droolsEngine);
                }).start();
            }
        }

        private Map<String,String> createDrls(DroolsEngine droolsEngine,int drlContentsNum){
            log.info("{}{}",droolsEngine.engineName,"初始化drl文件内容");
            String randomStr = RandomStringUtils.randomAlphanumeric(2);
            Map<String,String> drls = buildDrlContents(drlContentsNum,droolsEngine.engineName,randomStr);
            return drls;
        }

        private List<String> createFact(DroolsEngine droolsEngine,int elemNum){
            log.info("{}{}",droolsEngine.engineName,"初始化事实");
            List<String> list = getFact(elemNum);
            return list;
        }

        private void match(DroolsEngine droolsEngine,List<String> list){
            log.info("{}{}",droolsEngine.engineName,"将事实与规则进行匹配");
            droolsEngine.runMatch(list);
        }

        private void addDrlsToKFSSinglton(DroolsEngine droolsEngine,Map<String,String> drls){
            log.info("{}{}",droolsEngine.engineName,"将drl文件内容添加到drools引擎的kfs中");
            droolsEngine.add_drlContent_list(drls);
        }

        private void addDrlsToKFS(DroolsEngine droolsEngine,Map<String,String> drls){
            log.info("{}{}",droolsEngine.engineName,"将drl文件内容添加到drools引擎的kfs中");
            droolsEngine.add_drlContent_list(drls);
            droolsEngine.needBuildFlag = true;
            log.info("{}待编译标志->{}：待编译，kfs资源状态为新",droolsEngine.engineName,droolsEngine.needBuildFlag);
        }

        private void buildEngineSinglton(DroolsEngine droolsEngine){
            log.info("{}{}",droolsEngine.engineName,"将drools引擎kfs中的规则进行编译");
            droolsEngine.prepareEngineSinglton();
        }

        private void buildEngine(DroolsEngine droolsEngine){
            log.info("{}{}",droolsEngine.engineName,"将drools引擎kfs中的规则进行编译");
            droolsEngine.prepareEngine();
            droolsEngine.needBuildFlag = false;
            log.info("{}待编译标志->{}：已编译，kfs资源状态为旧",droolsEngine.engineName,droolsEngine.needBuildFlag);
        }

        private void removeDrlsFromKFSSinglton(DroolsEngine droolsEngine,Map<String,String> drls){
            log.info("{}{}",droolsEngine.engineName,"根据drl文件名剔除drools引擎kfs中的规则");
            droolsEngine.remove__drlContent_list(drls);
        }

        private void removeDrlsFromKFS(DroolsEngine droolsEngine,Map<String,String> drls){
            log.info("{}{}",droolsEngine.engineName,"根据drl文件名剔除drools引擎kfs中的规则");
            droolsEngine.remove__drlContent_list(drls);
            droolsEngine.needBuildFlag = true;
            log.info("{}待编译标志->{}：待编译，kfs资源状态为新",droolsEngine.engineName,droolsEngine.needBuildFlag);
        }
    }

    /**
     * 构建事实
     */
    private static List<String> getFact(int count){
        List<String> list = new ArrayList<>();
        for(int i =0;i<count;i++){
            list.add(RandomStringUtils.randomAlphanumeric(1)+i);
        }
        return list;
    }

    /**
     * 获取drl文件内容模版
     */
    private static String getDrlContentTemplate(){
        return "//包头 \n" +
                "package  MyDrools\n"
                + "dialect \"java\" \n"
                + "//引用类 \n"
                + "import java.util.*;\n"
                + "function Boolean judge{engineName}{name}(List list1){ \n"
                + " for(Object e :list1){ \n"
                + " if(e instanceof String){ \n"
                + " String elem = String.valueOf(e); \n"
                + " if(elem.contains(\"{targetStr}\")){ \n "
                + " return true; \n"
                + " } \n"
                + " } \n"
                + " } \n"
                + " return false; \n"
                + "}\n"
                + "rule \"{engineName}{name}\" \n"
                + "when \n"
                + " $list1:List(judge{engineName}{name}(this)) \n"
                + "then \n"
                + "end \n";
    }

    /**
     * 根据内容模版创建实例
     * @param targetStr 关键字
     * @param name 规则名
     * @param engineName 规则所属功能名
     */
    private static String getDrlContentInstance(String targetStr,String name,String engineName){
        String template = getDrlContentTemplate();
        template = template.replace("{targetStr}", targetStr);
        template = template.replace("{name}", name);
        template = template.replace("{engineName}", engineName);
        return template;
    }

    /**
     * 批量生成drl文件内容
     */
    private static Map<String,String> buildDrlContents(int count,String engineName){
        return buildDrlContents(count,engineName,null);
    }

    private static Map<String,String> buildDrlContents(int count,String engineName,String randomStr){
        Map<String,String> res = new HashMap<>();
        if(randomStr==null||randomStr.trim().length()==0){
            randomStr = "";
        }
        for (int i=0;i<count;i++){
            String iStr = String.valueOf(i);
            String content = getDrlContentInstance(iStr,iStr,engineName+randomStr);
            String drlPath = engineName+randomStr+iStr+".drl";
            res.put(drlPath, content);
        }
        return res;
    }


    /**
     * 批量将drl文件内容添加至kfs中
     */
    private void add_drlContent_list(Map<String,String> drlContent_list){
        for(Map.Entry<String, String>entry:drlContent_list.entrySet()){
            String drlPath = entry.getKey();
            String drlContent = entry.getValue();
            addDRLs(drlContent,drlPath);
        }
    }

    /**
     * 批量将drl文件内容从kfs中删除
     * @param drlContent_list drl文件名列表
     */
    private void remove__drlContent_list(Map<String,String> drlContent_list){
        for(Map.Entry<String, String>entry:drlContent_list.entrySet()){
            String drlPath = entry.getKey();
            deleteDrls(drlPath);
        }
    }


}
