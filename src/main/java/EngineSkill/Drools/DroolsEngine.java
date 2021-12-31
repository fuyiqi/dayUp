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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


public class DroolsEngine {
    private static Logger log= LoggerFactory.getLogger(DroolsEngine.class);
    private final KieServices ks = KieServices.Factory.get();
    private KieFileSystemImpl kfs = (KieFileSystemImpl)ks.newKieFileSystem();
    private KieContainerImpl kieContainer;
    private KieBase kieBase;

    //测试所用变量
    public String engineName = "default";
    //测试所用的析构方法
    private DroolsEngine(String enginename){
        engineName = enginename;
    }


    /**嵌套模式的单例
     * 线程安全
     * **/
    private DroolsEngine(){

    }

    private static class Holder{
        private static DroolsEngine _instance = new DroolsEngine();
    }

    public static DroolsEngine getInstance(){
        return Holder._instance;
    }

    /**
     * 获取kieContainer
     * @return
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
        boolean isSameFlag = drls_kfs.toString().equals(drls_kiebase.toString());
        log.info("Valid[RESULT={}]",isSameFlag);
        if(!isSameFlag){
            log.error("NOT SAME,kfs={}, kiebase={}",drls_kfs.toString(),drls_kiebase.toString());
            return;
        }
        log.info("=*=*=*=*=*=*=*=*=*=*=*Valid[end]=*=*=*=*=*=*=*=*=*=*=*");
    }



    /**
     * 添加单条规则 路径src/main/resources/drlname
     * @param content
     * @param drlname
     */
    public void addDRLs(String content,String drlname){
        if(content!=null && !"".equals(content.trim())){
            String kieFilePath = "src/main/resources/" + drlname;
            kfs.delete(kieFilePath);
            kfs.write(kieFilePath, content);
            log.info("[{} DB to KFS]add rule>>{} ",this.engineName,drlname);
        }
    }

    /**
     * 删除规则
     * @param drlname
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
     * 匹配
     * @param paramObject
     * @return
     */
    public int runMatch(Object paramObject){
        KieSession ks = createStatefulSession();
        ks.insert(paramObject);
        int matchNum = -1;
        try {
            matchNum = ks.fireAllRules(
                    match -> {
                        log.info("============MatchRuleName:" + match.getRule().getName() + "============");
                        return true;
                    }
            );
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
        //new ParallelCountDownLatch().doCountDownLatch();
        new ParallelCyclicBarrier().doCyclicBarrier();
    }

    /**
     * 使用CountDownLatch实现的并行类
     * CountDownLatch 基于 AQS 的共享模式的使用
     */
    static class ParallelCountDownLatch{

        private void doCountDownLatch(){
            int parallelismNum = 2;
            CountDownLatch countDownLatch = new CountDownLatch(parallelismNum);
            for(int i=0;i<parallelismNum;i++){
                new Thread(()->{
                    String engineName = RandomStringUtils.randomAlphanumeric(2);
                    DroolsEngine droolsEngine =  new DroolsEngine(engineName);
                    log.info("{}{}",engineName,"初始化drl文件内容");
                    Map<String,String> drls = createDrlContents(5,engineName);
                    log.info("{}{}",engineName,"将drl文件内容添加到drools引擎的kfs中");
                    droolsEngine.add_drlContent_list(drls);
                    log.info("{}{}",engineName,"将drools引擎kfs中的规则进行编译");
                    droolsEngine.prepareEngine();
                    countDownLatch.countDown();
                }).start();
            }
            try{
                countDownLatch.await();
            }catch (Exception e){
                log.error("Exception -> {}",e);
            }

        }
    }

    /**
     * 使用CyclicBarrier实现的并行类
     * 基于 Condition 来实现的
     */
    static class ParallelCyclicBarrier{
        private void doCyclicBarrier(){
            int parallelismNum = 2;
            CyclicBarrier cyclicBarrier = new CyclicBarrier(parallelismNum);
            for(int i=0;i<parallelismNum;i++){
                new Thread(()->{
                    try{
                        String engineName = RandomStringUtils.randomAlphanumeric(2);
                        DroolsEngine droolsEngine =  new DroolsEngine(engineName);
                        log.info("{}{}",engineName,"初始化drl文件内容");
                        Map<String,String> drls = createDrlContents(5,engineName);
                        log.info("{}{}",engineName,"将drl文件内容添加到drools引擎的kfs中");
                        droolsEngine.add_drlContent_list(drls);
                        log.info("{}{}",engineName,"将drools引擎kfs中的规则进行编译");
                        droolsEngine.prepareEngine();
                    }catch (Exception e){
                        log.error("Exception -> {}",e);
                    }
                }).start();
            }
        }
    }



    /**说明drools的使用方法*/
    private static void A_Drools_Usage(){
        log.info("{}","初始化drools引擎");
        DroolsEngine droolsEngine = DroolsEngine.getInstance();
        log.info("{}","初始化drl文件内容");
        Map<String,String> drls = createDrlContents(5,"myTest");
        log.info("{}","将drl文件内容添加到drools引擎的kfs中");
        droolsEngine.add_drlContent_list(drls);
        log.info("{}","将drools引擎kfs中的规则进行编译");
        droolsEngine.prepareEngine();
        log.info("{}","初始化事实");
        List<String> list = getFact1(3);
        log.info("{}","将事实与规则进行匹配");
        droolsEngine.runMatch(list);
        log.info("{}","根据drl文件名剔除drools引擎kfs中的规则");
        droolsEngine.remove__drlContent_list(drls);
        log.info("{}","将drools引擎kfs中的规则进行编译");
        droolsEngine.prepareEngine();
    }

    /**
     * 构建事实
     */
    private static List<String> getFact1(int count){
        List<String> list = new ArrayList<>();
        for(int i =0;i<count;i++){
            list.add(RandomStringUtils.randomAlphanumeric(1)+String.valueOf(i));
        }
        return list;
    }

    /**
     * 获取drl文件内容模版
     * @return
     */
    private static String getDrlContentTemplate(){
        return "//包头 \n" +
                "package  MyDrools\n"
                + "dialect \"java\" \n"
                + "//引用类 \n"
                + "import java.util.*;\n"
                + "function Boolean judge{name}(List list1){ \n"
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
                + "rule \"{package}{name}\" \n"
                + "when \n"
                + " $list1:List(judge{name}(this)) \n"
                + "then \n"
                + "end \n";
    }

    /**
     * 根据内容模版创建实例
     * @param targetStr 关键字
     * @param name 规则名
     * @param engineName 规则所属功能名
     * @return
     */
    private static String getDrlContentInstance(String targetStr,String name,String engineName){
        String template = getDrlContentTemplate();
        template = template.replace("{targetStr}", targetStr);
        template = template.replace("{name}", name);
        template = template.replace("{package}", engineName);
        return template;
    }

    /**
     * 批量生成drl文件内容
     */
    private static Map<String,String> createDrlContents(int count,String engineName){
        Map<String,String> res = new HashMap<>();
        for (int i=0;i<count;i++){
            String content = getDrlContentInstance(String.valueOf(i),String.valueOf(i),engineName);
            String drlPath = engineName+String.valueOf(i)+".drl";
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
     * @param drlContent_list
     */
    private void remove__drlContent_list(Map<String,String> drlContent_list){
        for(Map.Entry<String, String>entry:drlContent_list.entrySet()){
            String drlPath = entry.getKey();
            deleteDrls(drlPath);
        }
    }










}
