package EngineSkill.Relection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RelectionImpl {
    private static Logger logger = LoggerFactory.getLogger(RelectionImpl.class);
    private static StringBuffer stringBuffer;

    public static void getJar(String jar_path) throws Exception{
        try{
            File file = new File(jar_path);
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[] {url},
                    Thread.currentThread().getContextClassLoader());
            JarFile jarFile = new JarFile(jar_path);
            Enumeration<JarEntry> enumeration = jarFile.entries();
            JarEntry jarEntry;
            stringBuffer = new StringBuffer();
            while (enumeration.hasMoreElements()){
                jarEntry = enumeration.nextElement();
                if(jarEntry.getName().indexOf("META-INF")<0){
                    String class_fullname = jarEntry.getName();
                    if(class_fullname.indexOf(".class")<0){
                        class_fullname = class_fullname.substring(0,class_fullname.length()-1);
                    }else {
                        //去除后缀".class"，获得类名
                        String class_name = class_fullname.substring(0,class_fullname.length()-6).replace("/",".");
                        Class<?> myClass = classLoader.loadClass(class_name);
                        stringBuffer.append("类名\t：" + class_name);
                        logger.info("类名\t：" + class_name);

                        //属性名
                        Class<?> clazz = Class.forName(class_name);
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields){
                            stringBuffer.append("属性名\t：" + field.getName() + "\n");
                            logger.info("属性名\t：" + field.getName());
                            stringBuffer.append("-属性类型\t：" + field.getType() + "\n");
                            logger.info("-属性类型\t：" + field.getType());
                        }

                        //方法名
                        Method[] methods = myClass.getMethods();
                        for(Method method:methods){
                            if (method.toString().indexOf(class_name) > 0) {
                                stringBuffer.append("方法名\t：" + method.toString().substring(method.toString().indexOf(class_name)) + "\n");
                                logger.info("方法名\t：" + method.toString().substring(method.toString().indexOf(class_name)));
                                }
                            }
                        stringBuffer.append("--------------------------------------------------------------------------------" + "\n");
                        logger.info("--------------------------------------------------------------------------------" + "\n");
                        }
                    }
                }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        getJar("D:\\bak\\workspace\\ideaProj\\dayUp\\target\\dayUp-0.1.jar");
    }

}
