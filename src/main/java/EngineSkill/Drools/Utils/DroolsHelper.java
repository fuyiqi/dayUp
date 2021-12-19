package EngineSkill.Drools.Utils;


import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.kie.internal.utils.KieHelper;

import java.util.Map;


public class DroolsHelper {
    private static KieHelper kieHelper;
    private static KieBase kieBase;
    static {
        kieHelper = new KieHelper();
        kieBase = kieHelper.getKieContainer().getKieBase();
    }

    public static KieBase getKieBase() {
        return kieBase;
    }

    private static class Holder{
        private static final DroolsHelper _instance = new DroolsHelper();
    }

    public static DroolsHelper getInstance(){
        return Holder._instance;
    }

    public void add_rule(String content,String name){
        kieHelper.addContent(content,name);

        Results pre_build = kieHelper.verify();
        if(pre_build.hasMessages(Message.Level.ERROR)){
            String error_msg = pre_build.getMessages(Message.Level.ERROR).toString();
            System.out.println(error_msg);
            return ;
        }
        kieBase  = kieHelper.build();

/*        System.out.println("kieBase.getKiePackages().size()=>"+kieBase.getKiePackages().size());
        for(KiePackage kiePackage : kieBase.getKiePackages()){
            System.out.println("kiePackage.getRules().size()=>"+kiePackage.getRules().size());
            for(Rule rule:kiePackage.getRules()){
                System.out.println(rule.getName());
            }
        }*/

    }

    public void add_rule_list(Map<String,String> ruleInfo_list){
        for(Map.Entry<String,String> entry:ruleInfo_list.entrySet()){
            String content = entry.getValue();
            String name = entry.getKey();
            kieHelper.addContent(content,name);
        }

        Results pre_build = kieHelper.verify();
        if(pre_build.hasMessages(Message.Level.ERROR)){
            String error_msg = pre_build.getMessages(Message.Level.ERROR).toString();
            System.out.println(error_msg);
            return ;
        }
        kieBase  = kieHelper.build();

        return ;
    }

    /**获取匹配会话的句柄**/
    public KieSession getKieSession(){
        return kieBase.newKieSession();
    }

    /******通过kiesession执行规则匹配******/
    public int runKieSession(KieSession kieSession){
        int matchNum = -1;
        try {
            matchNum = kieSession.fireAllRules(
                    new AgendaFilter() {
                        @Override
                        public boolean accept(Match match) {
                            System.out.println("============MatchRuleName:" + match.getRule().getName() + "============");
                            return true;
                        }
                    }
            );

        }catch (Exception e){
            System.out.println("[runKieSession ERROR],"+e);
        }
        finally {
            if(null != kieSession){
                kieSession.dispose();
            }
        }
        return matchNum;
    }


}
