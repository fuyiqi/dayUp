package EngineSkill.Drools;

import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonConstants {
    private static final Logger log = LoggerFactory.getLogger(CommonConstants.class);
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
    public static final String SCRIPT_RULE_KIE_PATH_PREFIX = "src/main/resources";
    public static final String SCRIPT_RULE_NAME_KEY = "ruleName";
    public static final String SCRIPT_RULE_AGENDAGROUP_KEY = "agendaGroup";
    public static final String SCRIPT_RULE_PATTERNEXPRESSION_KEY = "patternExpression";
    public static final String SCRIPT_RULE_ALERTSTATUS_KEY = "alertStatus";

    public static final String defaultKiePackageName = "java.util";
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
                log.error("找不到指定的文件:"+filePath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static List<String> readFileNames_On_OneDir(String dir_path_str){
        List<String> res = new ArrayList<>();
        File file = new File(dir_path_str);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++){
            if (array[i].isFile()){
                String tmp = array[i].getPath();
                tmp = tmp.replace("\\","/");
                res.add(tmp);
            }
        }
        return res;
    }

    /*
     * 读取文件夹中的规则内容,以规则名为key，内容为value
     * @param dir_path
     * @return
     */
    public static Map<String,String> getRuleInfoList(String dir_path){
        Map<String,String> res = new HashMap<>();
        List<String> name_list = readFileNames_On_OneDir(dir_path);
        for(String name:name_list){
            String content = CommonConstants.readFileContent(name,"UTF8");
            res.put(name,content);
        }
        return res;
    }

















}
