package AC.Dp.SingleString;

import org.testng.annotations.Test;

/**
 * 股票系列
 */
public class Stock {

    /**
     * @Description: 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
     * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
     * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
     *
     */

    public int maxProfit(int[] prices) {//实现了功能但超时
        int low=0,high=prices.length-1;

/**
            int buy=prices[i];
            int maxSell=getMaxSell(prices,i);

**/
        int maxProfit= getMaxSell(prices,0)-prices[0];
        while (++low<=high){
            if(maxProfit<getMaxSell(prices,low)-prices[low]){
                maxProfit=getMaxSell(prices,low)-prices[low];
            }
        }
        //System.out.println(maxProfit);
        return maxProfit;
    }
    public int getMaxSell(int[] nums,int low){
        int high=nums.length-1;
        int maxV=nums[low];
        while (low<=high){
            if(maxV<nums[low]){
                maxV=nums[low];
            }
            low++;
        }
        return maxV;
    }





    @Test
    public void test(){
        int[] a1={7,1,5,3,6,4};//5
        int[] a2={7,6,4,3,1};//0
        maxProfit(a1);
    }






}
