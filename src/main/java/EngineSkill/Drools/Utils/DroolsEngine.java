package EngineSkill.Drools.Utils;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.util.Drools;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.kie.internal.conf.MultithreadEvaluationOption;

import java.util.List;
import java.util.Random;


/**
 * drools初始化必要的配置变量和运行变量
 * 向外输出匹配会话kieSession
 */

public class DroolsEngine extends CommonConstants{

    private static class DroolsEngineHolder{
        private static final DroolsEngine droolsEngine = new DroolsEngine();
    }

    public static DroolsEngine getInstance(){
        return DroolsEngineHolder.droolsEngine;
}


//kie配置层变量
    /**drools官方api**/
    private KieServices kieServices;
    /**drools工作内存文件系统**/
    private KieFileSystem kieFileSystem;
    /**drools中kmodule.xml文件构建类***/
    private KieModuleModel kieModuleModel;
    /**丰富kmodule文件中kbase属性值,也是KieBase的雏形***/
    KieBaseModel kieBaseModel;
    /**丰富kmodule文件中ksession属性值,也是KieSession的雏形***/
    KieSessionModel kieSessionModel;
    /**drools发行版本号**/
    private ReleaseId releaseId;
//kie运行层变量
    KieBuilder kieBuilder;
    /**kieSession的会话容器***/
    private KieContainer kieContainer;
    /**包含规则的drools知识仓库***/
    private KieBase kieBase;


    public DroolsEngine(){
        kieServices = KieServices.Factory.get();
        releaseId = getReleaseId();
        Kmodule2KieFileSystem(CommonConstants.kbaseName,CommonConstants.ksessionName);
    }

    /**
     * 生成releaseID
     */
    private ReleaseId getReleaseId(){
        return new ReleaseIdImpl("org.default",
                "artifact",
                "1.0.0-SNAPSHOT");
    }

    /**
     * 代码实现kmodule.xml配置文件功能
     */
    private DroolsEngine Kmodule2KieFileSystem(String kbaseName,String KsessionName){
        kieFileSystem = kieServices.newKieFileSystem();
        // 构建 kmodule.xml配置文件
        kieModuleModel = kieServices.newKieModuleModel(); //1为kmodule.xml创建KieModuleModel类
        kieBaseModel = kieModuleModel.newKieBaseModel(kbaseName)//"FileSystemKBase"
                .addPackage(packagesName)//2给kmodule.xml中kbase属性（name和packages）赋值
                .setDefault(true)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.STREAM);
        kieSessionModel = kieBaseModel.newKieSessionModel(KsessionName)//"FileSystemKSession"
                .setDefault(true)//3给kmodule.xml中kbase属性下kiesession属性（name）赋值
                .setClockType(ClockTypeOption.get("realtime"))
                .setType(KieSessionModel.KieSessionType.STATEFUL);
        // 加载 kmodule.xml配置文件到drools文件系统中
        String kmoduleXml_str = kieModuleModel.toXML(); //4生成kmodule.xml文件内容
        kieFileSystem.writeKModuleXML(kmoduleXml_str);//5将这个xml文件写入到KieFileSystem中

        return this;
    }


    public void loadRule(String rule){

        kieFileSystem.write(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"rule.drl",
                rule);

        //根据 kmodule.xml配置文件 和 .drl规则文件 加载KieContainer模型
        kieBuilder = kieServices.newKieBuilder(kieFileSystem)
                .buildAll();

        Results buildResult = kieBuilder.getResults();
        if(buildResult.hasMessages(Message.Level.ERROR)){
            throw new RuntimeException("构建kieBuilder失败:\n"
                    + buildResult.toString());
        }

        kieContainer = kieServices.newKieContainer(releaseId);
        kieBase = kieContainer.getKieBase(CommonConstants.kbaseName);
/***** 多线程 todo
        KieBaseConfiguration kieBaseConfiguration = kieServices.newKieBaseConfiguration();
        kieBaseConfiguration.setOption(MultithreadEvaluationOption.YES);
******/
        kieBaseMap.put(CommonConstants.kieBaseMap_key,kieBase);

    }


    public void loadRuleList(List<String> ruleContent_list){
        for(String ruleContent:ruleContent_list){
            //获取规则文件的文件名
            String ruleName = String.valueOf(new Random().nextInt());
            String rule_path = CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+ruleName+".drl";
            kieFileSystem.write(rule_path,
                    ruleContent);
        }

        //根据 kmodule.xml配置文件 和 .drl规则文件 加载KieContainer模型
        kieBuilder = kieServices.newKieBuilder(kieFileSystem)
                .buildAll();

        Results buildResult = kieBuilder.getResults();
        if(buildResult.hasMessages(Message.Level.ERROR)){
            throw new RuntimeException("构建kieBuilder失败:\n"
                    + buildResult.toString());
        }

        kieContainer = kieServices.newKieContainer(releaseId);
        kieBase = kieContainer.getKieBase(CommonConstants.kbaseName);

        kieBaseMap.put(CommonConstants.kieBaseMap_key,kieBase);
    }





















    public KieSession getKieSession(){
        return kieBaseMap.get(CommonConstants.kieBaseMap_key).newKieSession();
    }

    /******通过kiesession执行规则匹配******/
    public void runKieSession(KieSession kieSession){
        try {
            int matchNum = kieSession.fireAllRules(
                    new AgendaFilter() {
                        @Override
                        public boolean accept(Match match) {
                            System.out.println("============MatchRuleName:" + match.getRule().getName() + "============");
                            return true;
                        }
                    }
            );
            System.out.println(matchNum);
        }catch (Exception e){
            System.out.println("[runKieSession ERROR],"+e);
        }
        finally {
            if(null != kieSession){
                kieSession.dispose();
            }
        }
    }



















}
