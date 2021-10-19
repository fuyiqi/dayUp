package EngineSkill.Drools;

import EngineSkill.Drools.Entity.Alert;
import EngineSkill.Drools.Utils.CommonConstants;

import EngineSkill.Drools.Utils.DroolsHelper;


import org.kie.api.runtime.KieSession;

import org.testng.annotations.Test;



import java.util.*;


public class Main {

   @Test
    public void t1(){
        DroolsHelper droolsHelper = DroolsHelper.getInstance();
        String c1 = CommonConstants.readFileContent(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/01.drl","UTF8");
        String c2 = CommonConstants.readFileContent(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/02.drl","UTF8");
        String c3 = CommonConstants.readFileContent(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/03.drl","UTF8");

        KieSession ks = droolsHelper.add_rule(c1,CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/01.drl");
       System.out.println(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/01.drl"+"\n===\n"+c1+"****");
        ks = droolsHelper.add_rule(c2,CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/02.drl");
       System.out.println(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/02.drl"+"\n===\n"+c3+"****");
        ks = droolsHelper.add_rule(c3,CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/03.drl");
       System.out.println(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules/03.drl"+"\n===\n"+c3+"****");

        if(ks == null){

        }
        Alert a1 = new Alert("hh","thyu",6,"19.2.3.4");
        ks.insert(a1);
        droolsHelper.runKieSession(ks);
   }


    @Test
    public void t2(){
        DroolsHelper droolsHelper = DroolsHelper.getInstance();
        Map<String,String> ruleInfo_list = getRuleInfoList(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"trigger_rules");
        KieSession ks = droolsHelper.add_rule_list(ruleInfo_list);
        if(ks == null){
            System.out.println("ERROR, kieSession is null");
        }
        Alert a1 = new Alert("hh","thyu",6,"19.2.3.4");
        ks.insert(a1);
        droolsHelper.runKieSession(ks);


    }

    public Map<String,String> getRuleInfoList(String dir_path){
       Map<String,String> res = new HashMap<>();
        List<String> name_list = CommonConstants.readFileNames_On_OneDir(dir_path);
        for(String name:name_list){
            String content = CommonConstants.readFileContent(name,"UTF8");
            res.put(name,content);
        }
        return res;
    }




































}
