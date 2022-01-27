package EngineSkill.Drools.Matcher;

import EngineSkill.Drools.demo.DroolsHelper;

public class MyMatcher1 {

    private static DroolsHelper droolsHelper = DroolsHelper.getInstance("MyMatcher1");

    public static void main(String[] args) {
        String[] res = MyMatcher1.class.getName().toString().split("\\.");
        System.out.println(res[res.length-1]);
    }

}
