package EngineSkill.Drools.Utils;

import EngineSkill.Drools.Entity.Alert;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.testng.annotations.Test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class CommonConstants {

    public static final String kieContainerMap_key = "kieContainer_key";
    public final HashMap<String, KieContainer> kieContainerMap = new HashMap<String, KieContainer>();

    public static final String kieBaseMap_key = "kieBase_key";
    public static final HashMap<String, KieBase> kieBaseMap = new HashMap<String, KieBase>();

    public static final String kBaseMap_key = "kBase_key";
    public final HashMap<String, KnowledgeBaseImpl> kBaseMap = new HashMap<String, KnowledgeBaseImpl>();

    public static final String kbaseName = "FileSystemKBase";
    public static final String ksessionName = "FileSystemKSession";
    public static final String packagesName = "rules";

    public static final String SCRIPT_TEMPLATE_KEY = "ruleScriptTempalte";
    public static final String SCRIPT_RULE_KIE_PATH_PREFIX = "src/main/resources/rules/";
    public static final String SCRIPT_RULE_NAME_KEY = "ruleName";
    public static final String SCRIPT_RULE_AGENDAGROUP_KEY = "agendaGroup";
    public static final String SCRIPT_RULE_PATTERNEXPRESSION_KEY = "patternExpression";
    public static final String SCRIPT_RULE_ALERTSTATUS_KEY = "alertStatus";

    /***读取文本***/
    public static String readFileContent(String filePath,String fileEnCoding){
        StringBuilder sb = new StringBuilder();
        try{
            File file = new File(filePath);
            if(file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),fileEnCoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = null;
                while ((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                    sb.append("\r\n");
                }
                read.close();
            }else {
                System.out.println("找不到指定的文件:"+filePath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Test
    public void Test_readFileContent(){
        String filePath = SCRIPT_RULE_KIE_PATH_PREFIX+"multiAlert.drl";
        System.out.println(readFileContent(filePath,"UTF8"));
    }



















    /******多告警匹配语法【无法匹配的版本】******/
    public static String AlertRule(){
        String drl_str= "package rules\r\n"
                + "import EngineSkill.Drools.Entity.Alert; \r\n"
                + "rule \"rule-MultiAlert\"\r\n"
                + "\twhen\r\n"
                + "\t\t$alert1: Alert(getAlertStatus()<7) \r\n"
                + "\t\t$alert2: Alert(getText() contains \" aaa \") \r\n"
                + "\tthen\r\n"
                + "\t\tSystem.out.println(\"Matched Fact is \"+$alert1);\n"
                + "end\r\n"
                ;
        return drl_str;
    }


    /*****随机生成告警对象*****/
    public Alert randomAlert(){


        return null;
    }
























}
