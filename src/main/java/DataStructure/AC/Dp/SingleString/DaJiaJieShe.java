package DataStructure.AC.Dp.SingleString;


import org.testng.annotations.Test;

/**
 * 打家劫舍主要是不相邻子序列的最大和问题，以及若干变形
 */
public class DaJiaJieShe {


    /**
     * @Description: 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
     * 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
     * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
     * @Idea: f(n)=Max(f(n-02.drl)+nums[n],f(n-01.drl))
     */
    private int rob(int[] nums){
        if(nums.length==1){
            return nums[0];
        }

        if(nums.length==2){
            return Math.max(nums[0],nums[1]);
        }
        int[] f = new int[nums.length];
        f[0]=nums[0];f[1]=Math.max(nums[0],nums[1]);
        for(int i=2;i<nums.length;i++){
            f[i]=Math.max(f[i-2]+nums[i],f[i-1]);
        }
        int maxV = f[0];
        for(int i=1;i<f.length;i++){
            if(maxV<f[i]){
                maxV=f[i];
            }
        }
        //System.out.println(maxV);
        return maxV;
    }




    @Test
    public void test1(){
        int[] a1={1,2,3,1};
        int[] a2={2,7,9,3,1};
        int[] a3={6,3,10,8,2,10,3,5,10,5,3};
        rob(a1);
    }



}
