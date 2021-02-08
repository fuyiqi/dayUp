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


    public int maxSubArray(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = nums[0];

        for(int i=1;i<nums.length;i++){
            dp[i] = nums[i]+Math.max(dp[i-1],0);
        }

        int maxV = dp[0];
        for(int e:dp){
            if(maxV<e){
                maxV = e;
            }
        }
        //System.out.println(maxV);
        return maxV;
    }

    @Test
    public void test53(){
        int[] a1 = {-2,1};
        maxSubArray(a1);
    }


    public int lengthOfLIS(int[] nums){
        int[] dp = new int[nums.length];
        dp[0] = 1;
        for(int i = 1;i<nums.length;i++){
            dp[i] = 1;//最小值
            for (int j=0;j<i;j++){
                if(nums[i]>nums[j]) {//以nums[i]结尾的子序列是递增子序列
                    dp[i] = Math.max(dp[i], dp[j] + 1);//要么原地踏步，要么前进一步
                }
            }
        }

        int maxV = dp[0];
        for(int e:dp){
            if(maxV<e){
                maxV = e;
            }

        }
        for (int e:dp){
            System.out.println(e);
        }

        return maxV;
    }

    @Test
    public void test300(){
        int[] a1 = {10,9,2,5,3,7,101,18};
        int[] a2 = {1,3,5,4,7};
        int[] a3 = {7,7,7,7,7,7,7};

        lengthOfLIS(a2);
    }


    public int findNumberOfLIS(int[] nums) {
        //TODO



        return 0;
    }

    @Test
    public void test673(){
        int[] a1 = {1,3,2};
        int[] a2 = {1,3,5,4,7};
        int[] a3 = {2,2,2,2,2};

        findNumberOfLIS(a1);
    }





}
