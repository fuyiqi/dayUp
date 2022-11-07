package EngineSkill.Drools.bean;

public class Person {
    public int age;
    public String name;
    public String className;
    public Person(int age, String name,String className) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
