package EngineSkill.Drools;

import EngineSkill.Drools.Entity.Alert;
import EngineSkill.Drools.Utils.CommonConstants;
import EngineSkill.Drools.Utils.DroolsEngine;
import org.kie.api.runtime.KieSession;
import org.testng.annotations.Test;

public class Main {

    @Test
    public void SingleConditionTest(){
        DroolsEngine droolsEngine = DroolsEngine.getInstance();
        String alertRule = CommonConstants.AlertRule();
        droolsEngine.loadRule(alertRule);
        KieSession ks = droolsEngine.getKieSession();

        Alert alert1 = new Alert("chushi","hhhhhh",6,"12.7.78.5");

        ks.insert(alert1);

        droolsEngine.runKieSession(ks);

    }


    @Test
    public void MultiInstancesTest(){
        DroolsEngine droolsEngine = DroolsEngine.getInstance();

        String rulePath = CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"rule.drl";



    }
























}
