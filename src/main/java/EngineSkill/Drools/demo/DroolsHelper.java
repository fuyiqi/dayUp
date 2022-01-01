package EngineSkill.Drools.demo;



import org.drools.compiler.compiler.io.memory.MemoryFileSystem;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.drools.compiler.kie.builder.impl.KieFileSystemImpl;
import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.util.HashMap;
import java.util.Map;


public class DroolsHelper {
    private static KieHelper kieHelper;
    private static KieBase kieBase;
    private static KieContainerImpl kieContainer;
    //kie的文件系统，规则储存的地方
    private static KieFileSystemImpl kfs;
    //规则内容的包名
    private String packageName;
    static {
        kieHelper = new KieHelper();
        kieContainer = (KieContainerImpl)kieHelper.getKieContainer();
        kieBase = kieContainer.getKieBase();
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
     * 对working memory中的规则进行编译动作
     */
    private void compile_rules(){
        Results pre_build = kieHelper.verify();
        if(pre_build.hasMessages(Message.Level.ERROR)){
            String error_msg = pre_build.getMessages(Message.Level.ERROR).toString();
            System.out.println(error_msg);
            return ;
        }
        kieBase  = kieHelper.build();
        this.kfs = (KieFileSystemImpl) kieHelper.kfs;
        //校验
        getRuleFileNames_fromKFS();
        System.out.println("+++++++");
        getRuleContents_fromRete();
    }




    /**
     * 加载单条规则至working memory
     * @param content drl文件内容
     * @param name 绝对全路径除去"src/main/resources/"的drl文件名
     */
    public void add_rule(String content,String name){
        kieHelper.addContent(content,name);
        compile_rules();
    }

    /**
     * 剔除working memory中的单条规则
     * @param name 绝对全路径除去"src/main/resources/"的drl文件名
     */
    public void remove_rule(String name){
        //构建当前规则在drools中全路径
        String path = buildCompletePath_inDrools(name);
        this.kfs.delete(path);
        compile_rules();
    }

    /**
     * 获取匹配会话的句柄
     * */
    public KieSession getKieSession(){
        return kieBase.newKieSession();
    }

    /**
     * 获取kfs中的规则文件
     */
    public Map<String,String> getRuleFileNames_fromKFS() {
        Map<String,String> res = new HashMap<>();
        MemoryFileSystem mfs = this.kfs.getMfs();
         for(Map.Entry<String,byte[]> entrySet:mfs.getMap().entrySet()){
            String drlPathName = entrySet.getKey();
            String drlContent = new String(entrySet.getValue());
            System.out.println(drlPathName);
            res.put(drlPathName,drlContent);
         }
        return res;
    }



    /**
     * 获取rete网络中的规则内容
     */
    public void getRuleContents_fromRete(){
        KiePackage[]kiePackages = kieBase.getKiePackages().toArray(new KiePackage[0]);
        for (KiePackage kiePackage : kiePackages) {
            //规则内容中的包头
            String packageName = kiePackage.getName();
            if(!packageName.equals(CommonConstants.defaultKiePackageName)){
                Rule[] rules = kiePackage.getRules().toArray(new Rule[0]);
                //System.out.println(packageName+"->" + rules.length);
                for (Rule rule : rules) {
                    //规则内容中的规则名
                    String ruleName = buildCompletePath_inDrools(rule.getName()+".drl");
                    System.out.println(ruleName);
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



    /**
     * 批量加载规则至working memory
     * @param ruleInfo_list
     */
    public void add_rule_list(Map<String,String> ruleInfo_list){
        for(Map.Entry<String,String> entry:ruleInfo_list.entrySet()){
            String content = entry.getValue();
            //绝对全路径除去"src/main/resources/"的drl文件名
            String name = entry.getKey();
            if(name.contains(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX)){
                name = name.replace(CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"/"+packageName+"/","");
            }
            name = packageName+name;
            kieHelper.addContent(content,name);
        }
        compile_rules();
        return ;
    }

    /**
     * 构建drl文件在drools中的绝对全路径
     * @param ruleName
     */
    public String buildCompletePath_inDrools(String ruleName){
        return CommonConstants.SCRIPT_RULE_KIE_PATH_PREFIX+"/"+packageName+ruleName;

    }



}
