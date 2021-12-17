package EngineSkill.Drools;

import EngineSkill.Drools.Entity.Alert;
import EngineSkill.Drools.Utils.CommonConstants;

import EngineSkill.Drools.Utils.DroolsEngine;
import EngineSkill.Drools.Utils.DroolsHelper;


import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieSession;

import org.testng.annotations.Test;



import java.util.*;


public class MainTest {


    /**
     * 测试DroolsHelper
     */
    @Test
    public void t1(){
        //初始化引擎
        DroolsHelper droolsHelper = DroolsHelper.getInstance();
        //读取规则内容
        Map<String,String> ruleInfo_list = CommonConstants.getRuleInfoList(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules");
        //引擎加载内容至内存
        droolsHelper.add_rule_list(ruleInfo_list);
        //拿到已有规则内容的引擎的匹配会话句柄
        KieSession ks = droolsHelper.getKieSession();
        if(ks == null){
            System.out.println("ERROR, kieSession is null");
            return;
        }
        spy_ruleContent_on_drools(ks);

/*        Alert a1 = new Alert("hh","thryu",6,"19.2.3.4");
        ks.insert(a1);
        droolsHelper.runKieSession(ks);*/


    }


    /**
     * 测试DroolsEngine
     */
    @Test
    public void t2(){
        //初始化引擎
        DroolsEngine engine = DroolsEngine.getInstance();
        //读取规则内容
        Map<String,String> ruleInfo_list = CommonConstants.getRuleInfoList(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules");
        //引擎加载内容至内存
        engine.loadRuleList(ruleInfo_list);
        //拿到已有规则内容的引擎的匹配会话句柄
        KieSession ks = engine.getKieSession();
        spy_ruleContent_on_drools(ks);
    }

    /**
     * 获取匹配时内存中规则内容
     */
    private void spy_ruleContent_on_drools(KieSession ks){
        Collection<KiePackage> kps = ks.getKieBase().getKiePackages();
        for (KiePackage kp : kps){
            Collection<Rule> rules = kp.getRules();
            for(Rule rule:rules){
                System.out.println(",文件名："+rule.getName()+",rule包名："+rule.getPackageName()+","+rule.getId());
            }
        }
    }
































}
