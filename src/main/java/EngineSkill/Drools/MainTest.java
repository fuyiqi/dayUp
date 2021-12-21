package EngineSkill.Drools;


import EngineSkill.Drools.Utils.CommonConstants;


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
        String packageName = "h";
        //初始化引擎
        DroolsHelper droolsHelper = DroolsHelper.getInstance(packageName);
        //读取规则内容
        Map<String,String> ruleInfo_list = CommonConstants.getRuleInfoList(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+packageName);
        //引擎加载内容至内存
        droolsHelper.add_rule_list(ruleInfo_list);
        //拿到已有规则内容的引擎的匹配会话句柄
        KieSession ks = droolsHelper.getKieSession();
        if(ks == null){
            System.out.println("ERROR, kieSession is null");
            return;
        }
        //droolsHelper.getRuleFileNames_onRuntime();
        //待匹配的事实插入working memory
        List list = getFact();
        ks.insert(list);
        //匹配
        droolsHelper.runMatch(ks);
        droolsHelper.remove_rule("01.drl");
        //droolsHelper.getRuleFileNames_onRuntime();
        ks = droolsHelper.getKieSession();
        ks.insert(list);
        //匹配
        droolsHelper.runMatch(ks);
    }


   private List<String> getFact(){
       List<String> list = new ArrayList<>();
       list.add("8u");
       list.add("9yhg");
       list.add("1010");
       return list;
   }
































}
