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
        Map<String,String> ruleInfo_list = CommonConstants.getRuleInfoList(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"h");
        //引擎加载内容至内存
        droolsHelper.add_rule_list(ruleInfo_list);
        //拿到已有规则内容的引擎的匹配会话句柄
        KieSession ks = droolsHelper.getKieSession();
        if(ks == null){
            System.out.println("ERROR, kieSession is null");
            return;
        }
        //spy_ruleContents_on_drools(ks);
        List<String> list = new ArrayList<>();
        list.add("8");
        ks.insert(list);
        droolsHelper.runKieSession(ks);



    }


    /**
     * 获取匹配时内存中规则内容
     */
    private void spy_ruleContents_on_drools(KieSession ks){
        Collection<KiePackage> kps = ks.getKieBase().getKiePackages();
        for (KiePackage kp : kps){
            Collection<Rule> rules = kp.getRules();
            for(Rule rule:rules){
                System.out.println(",文件名："+rule.getName()+",rule包名："+rule.getPackageName()+","+rule.getId());
            }
        }
    }
































}
