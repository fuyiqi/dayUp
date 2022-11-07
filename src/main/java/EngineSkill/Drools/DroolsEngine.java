package EngineSkill.Drools;

import EngineSkill.Drools.bean.Person;
import EngineSkill.Drools.demo.CommonConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.drools.compiler.compiler.io.memory.MemoryFileSystem;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.drools.compiler.kie.builder.impl.KieFileSystemImpl;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.definition.KiePackage;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.*;
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
        //valid_in_kfs_and_kiebase();//单例不会出问题
    }

    /**
     * 校验kfs和kiebase的规则
     */
    public void valid_in_kfs_and_kiebase(){
        log.debug("=*=*=*=*=*=*=*=*=*=*=*Valid[start]=*=*=*=*=*=*=*=*=*=*=*");
        List<String> drls_kfs = getRules_fromKFS();
        List<String> drls_kiebase = getRules_fromkieBase();
        areTheySame(drls_kfs,drls_kiebase);
        log.debug("=*=*=*=*=*=*=*=*=*=*=*Valid[end]=*=*=*=*=*=*=*=*=*=*=*");
    }

    /**
     * 以kiebase为准,校验内容是否一致
     */
    private Map<String,Object> areTheySame(List<String> drls_kfs,List<String> drls_kiebase){
        Map<String,Object> res = new HashMap<>();
        boolean isSameFlag = drls_kfs.toString().equals(drls_kiebase.toString());
        if(isSameFlag){
            res.put("ValidResult",true);
            log.debug("ValidResult={}",isSameFlag);
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
            log.debug("[{} insert to KFS]add rule>>{} ",this.engineName,drlname);
        }
    }

    /**
     * 删除规则
     */
    public void deleteDrls(String drlname){
        String kieFilePath = "src/main/resources/" + drlname;
        kfs.delete(kieFilePath);
        log.debug("[{} delete from KFS] rule >>{} ",this.engineName,drlname);
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
            log.debug("[{}]===>KFS's rule ={} ",this.engineName,drlPath);
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
                log.debug("[{}]=====>kieBase's rule={} ",this.engineName,name);

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


    /**
     * 防止死循环，当规则通过update之类的函数修改了Fact对象时，可能使规则再次被激活，从而导致死循环。
     * 将no-loop设置为true的目的是避免当前规则then部分被修改后的事实对象再次被激活，从而防止死循环的发生，即执行下面的规则。
     */
    @Test
    public void TestisNotLoop(){
        DroolsEngine droolsEngine = getDroolsEnginewithRules();
        //构建待匹配的事实
        Person person = new Person(30,"张三","一班");
        //匹配
        int matchNum = droolsEngine.runMatch(person);

    }

    /**
     * no-loop的升级版。
     * agenda-group再次被激活时，即使在规则体中设置了lock-on-active为true，该规则体也不能再次被激活，即无论如何更新规则事实对象，当前规则也只能被触发一次。
     */
    @Test
    public void TestisLockNoActive(){
        DroolsEngine droolsEngine = getDroolsEnginewithRules();
        Person person = new Person(30,"张三","一班");
        //匹配
        int matchNum = droolsEngine.runMatch(person);

    }

    /**
     * salience
     * 规则体被执行的顺序，每一个规则都有一个默认的执行顺序，如果不设置salience属性，规则体的执行顺序为由上到下。salience值可以是一个整数，但也可以是一个负数，其值越大，执行顺序越高，排名越靠前。
     */

    /**
     * activation-group
     * activation-group是指激活分组，通过字符串定义分组名称，具有相同组名称的规则体有且只有一个规则被激活，
     * 其他规则体的LHS部分仍然为true也不会再被执行。该属性受salience属性的影响，
     * 如当前规则文件中的其他规则未设计该属性，则视为规则处于被激活状态，并不受该属性的影响。
     */
    @Test
    public void TestisActiveGroup(){
        DroolsEngine droolsEngine = getDroolsEnginewithRules();
        droolsEngine.runMatch("");

    }

    /**
     * 另一种可控的规则执行方式，是指用户可以通过配置agenda-group的参数来控制规则的执行，而且只有获取焦点的规则才会被激活。
     * 实际应用中的agenda-group可以与auto-focus属性一起使用，这样就不会出现上述问题了。如果将某个规则体的auto-focus属性设置为true，
     * 那么即使该规则设置了agenda-group属性，也不需要在Java代码中设置。
     */
    @Test
    public void TestisAgendaGroup(){
        DroolsEngine droolsEngine = getDroolsEnginewithRules();
        StatefulKnowledgeSessionImpl kieSession = (StatefulKnowledgeSessionImpl) droolsEngine.kieContainer.newKieSession();;
        //让AgendaGroup分组为ag1的获取焦点
        kieSession.getAgenda().getAgendaGroup("ag1").setFocus();
        kieSession.fireAllRules(
                match -> {
                    log.info("MatchRuleName:{}" ,match.getRule().getName() );
                    return true;
                }
        );
        kieSession.dispose();
    }

    @Test
    public void TestisAgendaGroupAndActivationGroup6(){
        DroolsEngine droolsEngine = getDroolsEnginewithRules();
        StatefulKnowledgeSessionImpl kieSession = (StatefulKnowledgeSessionImpl) droolsEngine.kieContainer.newKieSession();;
        //让AgendaGroup分组为ag1的获取焦点
        kieSession.getAgenda().getAgendaGroup("ag6").setFocus();
        kieSession.fireAllRules(
                match -> {
                    log.info("MatchRuleName:{}" ,match.getRule().getName() );
                    return true;
                }
        );
        kieSession.dispose();
    }

    @Test
    public void TestisAgendaGroupAndActivationGroup8(){
        DroolsEngine droolsEngine = getDroolsEnginewithRules();
        StatefulKnowledgeSessionImpl kieSession = (StatefulKnowledgeSessionImpl) droolsEngine.kieContainer.newKieSession();;
        //让AgendaGroup分组为ag1的获取焦点
        kieSession.getAgenda().getAgendaGroup("ag8").setFocus();
        kieSession.fireAllRules(
                match -> {
                    log.info("MatchRuleName:{}" ,match.getRule().getName() );
                    return true;
                }
        );
        kieSession.dispose();
    }




    /**
     * 获取有规则内容的引擎
     * @return
     */
    public static DroolsEngine getDroolsEnginewithRules(){
        //创建drools引擎
        DroolsEngine droolsEngine =  DroolsEngine.getInstance();
        //获取规则库
        Map<String,String> drls = CommonConstants.getRuleInfoList("src/main/resources/drools");
        //规则库添加至drools引擎中
        droolsEngine.add_drlContent_list(drls);
        //drools引擎对规则进行编译
        droolsEngine.prepareEngineSinglton();
        return droolsEngine;
    }






    /*=*==*==*==*==*==*==*==*=模拟并发[start]*=*==*==*==*==*==*==*==*=*/

    public static void main(String[] args){
        //new ParallelMyTool().doParallel();
        new ParallelMyTool().doSingleton();
    }

    /**
     * 使用多线程的并发类
     */
    static class ParallelMyTool{

        /**
         * 并发
         */
        private void doParallel(){
            int parallelismNum = 1000;
            for(int i=0;i<parallelismNum;i++){
                new Thread(()->{
                    String engineName = RandomStringUtils.randomAlphanumeric(2);
                    DroolsEngine droolsEngine =  new DroolsEngine(engineName);
                    Map<String,String> drls = createDrls(droolsEngine,3);
                    addDrlsToKFS(droolsEngine,drls);
                    buildEngine(droolsEngine);
                   List<String> list = createFact(droolsEngine,2);
                    match(droolsEngine,list);
                    removeDrlsFromKFS(droolsEngine,drls);
                    buildEngine(droolsEngine);
                }).start();
            }
        }

        /**
         * 使用单例的情况
         */
        private void doSingleton(){
            int parallelismNum = 1000;//1000的数量跑不起来
            for(int i=0;i<parallelismNum;i++){
                new Thread(()->{
                    DroolsEngine droolsEngine =  DroolsEngine.getInstance();
                    Map<String,String> drls = createDrls(droolsEngine,3);
                    addDrlsToKFSSinglton(droolsEngine,drls);
                    buildEngineSinglton(droolsEngine);
                    List<String> list = createFact(droolsEngine,3);
                    match(droolsEngine,list);
                    removeDrlsFromKFSSinglton(droolsEngine,drls);
                    buildEngineSinglton(droolsEngine);
                }).start();
            }
        }

        private Map<String,String> createDrls(DroolsEngine droolsEngine,int drlContentsNum){
            log.debug("{}{}",droolsEngine.engineName,"初始化drl文件内容");
            String randomStr = RandomStringUtils.randomAlphanumeric(2);
            return buildDrlContents(drlContentsNum,droolsEngine.engineName,randomStr);
        }

        private List<String> createFact(DroolsEngine droolsEngine,int elemNum){
            log.debug("{}{}",droolsEngine.engineName,"初始化事实");
            return getFact(elemNum);
        }

        private void match(DroolsEngine droolsEngine,List<String> list){
            log.debug("{}{}",droolsEngine.engineName,"将事实与规则进行匹配");
            int matchNum = droolsEngine.runMatch(list);
            log.info("匹配数量:{}",matchNum);
        }

        private void addDrlsToKFSSinglton(DroolsEngine droolsEngine,Map<String,String> drls){
            log.debug("{}{}",droolsEngine.engineName,"将drl文件内容添加到drools引擎的kfs中");
            droolsEngine.add_drlContent_list(drls);
        }

        private void addDrlsToKFS(DroolsEngine droolsEngine,Map<String,String> drls){
            log.debug("{}{}",droolsEngine.engineName,"将drl文件内容添加到drools引擎的kfs中");
            droolsEngine.add_drlContent_list(drls);
            droolsEngine.needBuildFlag = true;
            log.debug("{}待编译标志->{}：待编译，kfs资源状态为新",droolsEngine.engineName,droolsEngine.needBuildFlag);
        }

        private void buildEngineSinglton(DroolsEngine droolsEngine){
            log.debug("{}{}",droolsEngine.engineName,"将drools引擎kfs中的规则进行编译");
            droolsEngine.prepareEngineSinglton();
        }

        private void buildEngine(DroolsEngine droolsEngine){
            log.debug("{}{}",droolsEngine.engineName,"将drools引擎kfs中的规则进行编译");
            droolsEngine.prepareEngine();
            droolsEngine.needBuildFlag = false;
            log.debug("{}待编译标志->{}：已编译，kfs资源状态为旧",droolsEngine.engineName,droolsEngine.needBuildFlag);
        }

        private void removeDrlsFromKFSSinglton(DroolsEngine droolsEngine,Map<String,String> drls){
            log.debug("{}{}",droolsEngine.engineName,"根据drl文件名剔除drools引擎kfs中的规则");
            droolsEngine.remove__drlContent_list(drls);
        }

        private void removeDrlsFromKFS(DroolsEngine droolsEngine,Map<String,String> drls){
            log.debug("{}{}",droolsEngine.engineName,"根据drl文件名剔除drools引擎kfs中的规则");
            droolsEngine.remove__drlContent_list(drls);
            droolsEngine.needBuildFlag = true;
            log.debug("{}待编译标志->{}：待编译，kfs资源状态为新",droolsEngine.engineName,droolsEngine.needBuildFlag);
        }
    }

    /*=*==*==*==*==*==*==*==*=模拟并发[end]*=*==*==*==*==*==*==*==*=*/

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
                //+ " System.out.println(\"{engineName}{name}\");\n"
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
