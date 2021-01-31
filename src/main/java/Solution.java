import org.testng.annotations.Test;

public class Solution {

    public boolean isUnique(String astr) {

        int left=0,right=astr.length();
        while (++left<right){
            if(astr.charAt(left-1)==astr.charAt(left)){
                //System.out.println(false);
                return false;
            }
        }

        //System.out.println(true);
        return true;
    }


    @Test
    public void test01_01(){

        String s1 = "leetcode";
        String s2 = "abc";
        isUnique(s1);




    }


    













}
