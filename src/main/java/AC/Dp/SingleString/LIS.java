package AC.Dp.SingleString;


import org.testng.annotations.Test;

/**
 * 最长上升子序列系列
 */
public class LIS {

    /**
     * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
     * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
     * 例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
     */

    /**
     * @Idea: f(i)是以nums[i]结尾的元素的最长子序列长度
     * 同理可知f(i-1)是以nums[i-1]结尾的元素的最长子序列长度
     * 当f(i-1)已知，推导f(i)的情况时，考虑到以nums[i-1]结尾的元素的最长子序列都是比nums[i-1]小的元素
     * 求解这个序列的长度是在上述情况的组合中找到最大值作为f(i-1)的值
     */
    public int lengthOfLIS(int[] nums) {
        int[] f= new int[nums.length];
        int cnt=0;
        for(int i = 1;i<nums.length;i++){
            cnt=0;
            for(int j=i-1;j>=0;j--){
                if(nums[j]<nums[i]){
                    cnt++;
                }
            }
            System.out.println("以nums["+i+"]="+nums[i]+"结尾的元素的序列中，比该元素值小的个数="+cnt);
        }



        return 0;
    }



    @Test
    public void test(){
        int[] a1={10,9,2,5,3,7,101,18};//4
        int[] a2={0,1,0,3,2,3};//4
        int[] a3={7,7,7,7,7};//1
        lengthOfLIS(a1);
    }



}
