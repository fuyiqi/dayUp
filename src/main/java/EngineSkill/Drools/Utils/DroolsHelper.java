package EngineSkill.Drools.Utils;



import org.drools.compiler.kie.builder.impl.KieFileSystemImpl;
import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.util.Map;


public class DroolsHelper {
    private static KieHelper kieHelper;
    private static KieBase kieBase;
    private static KieFileSystemImpl kfs;
    private String packageName;
    static {
        kieHelper = new KieHelper();
        kieBase = kieHelper.getKieContainer().getKieBase();
    }


    private DroolsHelper(String packageName){
        this.packageName = packageName;
    }
    public static DroolsHelper getInstance(){
        return new DroolsHelper("");
    }

    public static DroolsHelper getInstance(String packageName){
        return new DroolsHelper(packageName);
    }

    /**
     * 加载单条规则至working memory
     * @param content drl文件内容
     * @param name 绝对全路径除去"src/main/resources/"的drl文件名
     */
    public void add_rule(String content,String name){
        kieHelper.addContent(content,name);
        Results pre_build = kieHelper.verify();
        if(pre_build.hasMessages(Message.Level.ERROR)){
            String error_msg = pre_build.getMessages(Message.Level.ERROR).toString();
            System.out.println(error_msg);
            return ;
        }
        kieBase  = kieHelper.build();
        this.kfs = (KieFileSystemImpl) kieHelper.kfs;
    }

    public void remove_rule(String name){
        String path = "src/main/resources/"+packageName+"/"+name;
        System.out.println(path);
        this.kfs.delete(path);
        Results pre_build = kieHelper.verify();
        if(pre_build.hasMessages(Message.Level.ERROR)){
            String error_msg = pre_build.getMessages(Message.Level.ERROR).toString();
            System.out.println(error_msg);
            return ;
        }
        kieBase  = kieHelper.build();
    }

    /**
     * 获取匹配会话的句柄
     * */
    public KieSession getKieSession(){
        return kieBase.newKieSession();
    }

    /**
     * 获取运行态时working memory中的规则
     */
    public void getRules_onRuntime() {
        KiePackage[]kiePackages = kieBase.getKiePackages().toArray(new KiePackage[0]);
        for (KiePackage kiePackage : kiePackages) {
            String packageName = kiePackage.getName();
            if(!packageName.equals(CommonConstants.defaultKiePackageName)){
                Rule[] rules = kiePackage.getRules().toArray(new Rule[0]);
                System.out.println(packageName+"->" + rules.length);
                for (Rule rule : rules) {
                    System.out.println(rule.getName());
                }
            }

        }

    }

    /**
     * 通过kiesession执行规则匹配
     * */
    public int runMatch(KieSession kieSession){
        int matchNum = -1;
        try {
            matchNum = kieSession.fireAllRules(
                    match -> {
                        System.out.println("============MatchRuleName:" + match.getRule().getName() + "============");
                        return true;
                    }
            );

        }catch (Exception e){
            System.out.println("[runMatch ERROR],"+e);
        }
        finally {
            if(null != kieSession){
                kieSession.dispose();
            }
        }
        return matchNum;
    }



/*********************************************************************/
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





}
