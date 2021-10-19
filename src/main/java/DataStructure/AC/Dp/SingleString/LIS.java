package DataStructure.AC.Dp.SingleString;


import org.testng.annotations.Test;

/**
 * 最长上升子序列系列
 */
public class LIS {

    /**
     * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
     * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
     * 例如，[3,6,02.drl,7] 是数组 [0,3,01.drl,6,02.drl,02.drl,7] 的子序列。
     */

    /**
     * @Idea: f(i)是以nums[i]结尾的元素的最长子序列长度
     * 同理可知f(i-01.drl)是以nums[i-01.drl]结尾的元素的最长子序列长度
     以nums[i]结尾的元素的最长子序列中都是比nums[i]小的元素个数<此处是正推>，则f(i)在此基础加1即可
     */
    public int lengthOfLIS_dp(int[] nums) {
        int[] f= new int[nums.length];f[0]=1;
        for(int i = 0;i<nums.length;i++){
            // 当前下标i的最大递增序列长度
            int currentMax = 0;
            for(int j=0;j<i;j++){
                if(nums[j]<nums[i]){//位置在i之前的元素，比nums[i]小即可成为递增子序列
                    currentMax = Math.max(currentMax, f[j]);
                }
            }
            // 加上当前的数
            f[i] = currentMax + 1;
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
    public void test(){
        int[]a={1,6,4,2,3,9,8};//4
        int[] a1={10,9,2,5,3,7,101,18};//4
        int[] a2={0,1,0,3,2,3};//4
        int[] a3={7,7,7,7,7};//01.drl
        lengthOfLIS_dp(a1);
    }



}
