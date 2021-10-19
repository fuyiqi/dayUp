package EngineSkill.Relection.demo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RelectTest {


    /**
     * 创建一个和参数objcet同样类型的对象，然后把object对象中的所有属性拷贝到新建的对象中，并将其返回
     * @param object
     * @return
     * @throws Exception
     */
    public Object copy(Object object) throws Exception{
        //获得的对象类型
        Class class_type = object.getClass();
        System.out.println("Class:"+class_type.getName());

        //通过默认构造方法创建一个新的对象
        Object object_copy = class_type.getConstructor(new Class[]{}).newInstance(new Object[]{});

        //获得参照拷贝对象的所有属性
        Field fields[] = class_type.getDeclaredFields();

        for(int i = 0;i<fields.length;i++){
            Field field = fields[i];
            String filed_name = field.getName();
            String first_letter = filed_name.substring(0,1).toUpperCase();
            //获得属性对应的get方法名字
            String get_method_name = "get"+first_letter+filed_name.substring(1);
            //获得属性对应的set方法名字
            String set_method_name = "set"+first_letter+filed_name.substring(1);
            //获得属性对应的get方法
            Method get_method = class_type.getMethod(get_method_name,new Class[]{});
            //获得属性对应的set方法名字
            Method set_method = class_type.getMethod(set_method_name,new Class[]{field.getType()});
            //调用原对象的get方法
            Object value = get_method.invoke(object,new Object[]{});
            System.out.println("原field_name:"+value);
            //调用拷贝对象的set方法
            set_method.invoke(object_copy,new Object[]{value});
        }
        return object_copy;
    }
















}
