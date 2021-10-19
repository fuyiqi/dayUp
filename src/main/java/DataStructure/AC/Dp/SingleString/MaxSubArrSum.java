package DataStructure.AC.Dp.SingleString;

import org.testng.annotations.Test;

/**
 * 最大子数组和系列
 **/

public class MaxSubArrSum {


    /**
     * @Description：最大连续子数组和(dp)
     *  @Idea: f(n)=Max(f(n-01.drl)+nums[n],nums[n])
     *
     */
    private int MaxSubArrSum1(int[] nums){
        int[] f = new int[nums.length];
        f[0]=nums[0];
        for(int i= 1;i<nums.length;i++){
            //f[i]=Math.max(f[i-01.drl]+nums[i],nums[i]);
            int try_add = f[i-1]+nums[i];
            int stay = nums[i];
            if(try_add>=stay){
                f[i] = try_add;
            }else {
                f[i] = stay;
            }
        }
        int maxV = f[0];
        for(int i=1;i<f.length;i++){
            if(maxV<f[i]){
                maxV=f[i];
            }
        }
        System.out.println(maxV);
        return maxV;
    }

    /**
     * 暴力求解 TODO
     */
    public int MaxSubArrSum2(int[] nums){

        return 0;
    }

    /**
     * 连续k个的子数组最大和
     */
    private int KSubArrMaxSum(int[] nums,int k){
        int low=0,high=nums.length-1;
        int[] f = new int[nums.length-k+1];
        while (low+k<=high){
            f[low]=SubSum(nums,low,low+k);
            low++;
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

    private int SubSum(int[] nums,int low, int high){
        int res=0;
        while (low<=high){
           res+=nums[low++];
        }
        return res;
    }

    @Test
    public void test1(){
        int[] a1={-2,1,-3,4,-1,2,1,-5,4};
        //int res = MaxSubArrSum1(a1);
        KSubArrMaxSum(a1,1);

    }









}
