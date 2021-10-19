package DataStructure.AC.Dp.SingleString;

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
        int maxProfit= getMaxSell(prices,0)-prices[0];
        while (++low<=high){
            if(maxProfit<getMaxSell(prices,low)-prices[low]){
                maxProfit=getMaxSell(prices,low)-prices[low];
            }
        }

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

    /**
     *  f(i)是第i天的最大利润，同理f(i-01.drl)是第i-1天的最大利润
     *  已知f(i-01.drl)时，f(i)是在price[i]-min(nums[0,i])和f(i-01.drl)中取最大值
     */
    public int maxProfit_dp(int[] prices){
        int[] f = new int[prices.length];
        int min_i=prices[0];
        for (int i=1;i<prices.length;i++){
            if(prices[i]<min_i){
                min_i=prices[i];
            }
            f[i]=Math.max(f[i-1],prices[i]-min_i);
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
        int[] a1={7,1,5,3,6,4};//5
        int[] a2={7,6,4,3,1};//0
        maxProfit_dp(a2);
    }

    /**
     * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
     * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
     * 贪心的思路
     */
    public int maxProfit2(int[] prices){

        int profit=0;
        for(int i=1;i<prices.length;i++){
            //当相邻俩数是递增关系则第i-1天买入，第i天卖出
            if(prices[i-1]<prices[i]){
                profit+=prices[i]-prices[i-1];
            }
        }
        //System.out.println(profit);

        return profit;
    }



    @Test
    public void test2(){
        int[] a1={7,1,5,3,6,4};//7
        int[] a2={1,2,3,4,5};//4
        int[] a3={7,6,4,3,1};//0
        maxProfit2(a3);
    }



}
