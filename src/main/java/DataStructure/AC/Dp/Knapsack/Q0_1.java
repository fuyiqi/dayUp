package DataStructure.AC.Dp.Knapsack;

import org.testng.annotations.Test;

/**
 * 0-1背包问题
 */
public class Q0_1 {

    /**
     * 0-1背包问题
     * f(i,j)是考虑在编号0,...,i的物品中占用了空间j取到的最大价值
     * f(i,j)= Max(
     *  f(i-01.drl,j),//不放入编号为i的物品
     *  f(i-01.drl,j-weight[i])+value[i]//放入编号为i的物品
     * )
     */

    //递归版
    private int pack_0_1(int[]weight,int[]value,int validCapacity){
        int num = weight.length;
        return solveKS(weight,value,num-1,validCapacity);
    }

    private int solveKS(int[]weight,int[]value,int i,int validCapacity){
        if(i<0||validCapacity<=0){
            return 0;
        }
        //不放入编号为i的物品的价值
        int v = solveKS(weight,value,i-1,validCapacity);
        //放入编号为i的物品的价值
        if(weight[i]<=validCapacity){
            v = Math.max(solveKS(weight,value,i-1,validCapacity-weight[i])+value[i],v);
        }
        System.out.println("考虑到编号为"+i+"的物品，占用"+validCapacity+"个空间的最大价值是"+v);
        return v;
    }

    //递归版,增加记忆功能
    private int[][] memo;
    private int pack01_memo(int[]weight,int[]value,int validCapacity){
        int num = weight.length;
        memo=new int[num][validCapacity+1];
        return KS_memo(weight,value,num-1,validCapacity);
    }
    private int KS_memo(int[]weight,int[]value,int i,int validCapacity){
        if(i<0||validCapacity<=0){
            return 0;
        }
        if(memo[i][validCapacity]!=0){
            return memo[i][validCapacity];
        }
        //不放入编号为i的物品的价值
        int v = KS_memo(weight,value,i-1,validCapacity);
        //放入编号为i的物品的价值
        if(weight[i]<=validCapacity){
            v = Math.max(KS_memo(weight,value,i-1,validCapacity-weight[i])+value[i],v);
        }
        memo[i][validCapacity]=v;
        System.out.println("考虑到编号为"+i+"的物品，占用"+validCapacity+"个空间的最大价值是"+v);
        return v;
    }

    //非递归版
    private int pack01_dp(int[]weight,int[]value,int capacity){
        int num = weight.length;//物品编号
        int[][]f=new int[num][capacity+1];//转移方程

        //考虑编号为0的物品放入容量是capacity的最大价值
        for (int j=0;j<=capacity;j++){
            if(weight[0]<=j){//当前容量放得下编号为0的物品
                f[0][j]=value[0];
            }else {
                f[0][j]=0;
            }
        }

        //填充其他行和列
        for(int i = 1;i<num;i++){
            for(int j=0;j<=capacity;j++){
                int v = f[i-1][j];
                if(weight[i]<=j){//当前容量放得下编号为i的物品
                    f[i][j]=Math.max(v,f[i-1][j-weight[i]]+value[i]);
                }else {//放不下
                    f[i][j]=v;
                }
            }
        }

        for(int i=0;i<num;i++){
            for(int j=0;j<=capacity;j++){
                System.out.print(f[i][j]+"\t");
            }
            System.out.println();
        }


        return f[num-1][capacity];
    }



    @Test
    public void test(){
        int[] value = new int[]{1500, 3000, 2000};
        int[] weight = new int[]{1, 4, 3};
        int res = pack01_dp(weight,value,4);

        //System.out.print(res);
    }

    
}
