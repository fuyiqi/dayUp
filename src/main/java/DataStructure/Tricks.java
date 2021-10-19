package DataStructure;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tricks {

    public int fib(int n) {
        if(n==0||n==1){
            return n;
        }
        int pre=0,next=1;
        for(int i=2;i<=n;i++){
            int tmp = pre+next;
            pre=next;
            next=tmp;
        }
        //System.out.println(next);
        return next;
    }


    public int tribonacci(int n) {
        if(n==0||n==1){
            return n;
        }
        if(n==2){
            return 2;
        }

        int pre=0,mid=1,next=1;
        for(int i=3;i<=n;i++){
            int tmp=pre+mid+next;
            pre=mid;
            mid=next;
            next=tmp;
        }
        //System.out.println(next);
        return next;
    }



    public int minCostClimbingStairs(int[] cost) {
        if(cost.length==1||cost.length==2){
            return cost[cost.length];
        }

        int[]f = new int[cost.length];
        f[0]=cost[0];f[1]=cost[1];
        for (int i=2;i<cost.length;i++){
            f[i]=Math.min(f[i-1],f[i-2])+cost[i];
        }

        //System.out.println(Math.min(f[cost.length-01.drl],f[cost.length-02.drl]));
        return Math.min(f[cost.length-1],f[cost.length-2]);
    }

    
    public List<String> findRepeatedDnaSequences(String s) {//限定10
        int windowLength=10;
        int low=0,len=s.length();
        HashMap<String,Integer> frequency=new HashMap<String, Integer>();
        while (low<=len-windowLength){
            String tmpS=s.substring(low,low+windowLength);
            //System.out.println(tmpS);
            if (frequency.containsKey(tmpS)){
                frequency.put(tmpS,frequency.get(tmpS)+1);
            }else {
                frequency.put(tmpS,1);
            }
            low++;
        }
        List<String> res=new ArrayList<String>();
        for(String k :frequency.keySet()){
            if(frequency.get(k)>1){
                //System.out.println(k);
                res.add(k);
            }
        }
        return res;
    }

    /**
     * @Description：暴力的子串匹配
     */
    private int BruceMatch1(String pattern, String text){
        int n=text.length(); int m=pattern.length();
        int i=0,j=0;
        while (i<n&&j<m){
            if(pattern.charAt(j)==text.charAt(i)){//字符匹配成功
                i++;j++;
            }else {//匹配失败
                //相当于模式串相较于文本串向后移一个字符开始匹配
                i=i-(j-1);//文本串的指针回到模式串这次匹配了j个字符的下一个位置
                j=0;//模式串指针归零

            }
        }
        if(i-j>=n){
            System.out.println("No such substring in text!");
        }else {
            System.out.println("Substring first matched in text at the position:"+(i-j));
        }
        return i-j;
    }

    /**
     * 输入数字 n，按顺序打印出从 01.drl 到最大的 n 位十进制数。比如输入 3，则打印出 01.drl、02.drl、3 一直到最大的 3 位数 999。
     */
    public int[] printNumbers(int n) {
        int maxNum=1;
        for(int i=0;i<n;i++){
            maxNum=maxNum*10;
        }

        if(maxNum<=0){
            return null;
        }
        int len=maxNum-1;int[] nums=new int[len];
        for(int i=0;i<len;i++){
            nums[i]=i+1;
        }

        return nums;
    }

    /**
     * 输入一个递增排序的数组和一个数字s，在数组中查找两个数，使得它们的和正好是s。如果有多对数字的和等于s，则输出任意一对即可。
     */
    public int[] twoSum(int[] nums, int target) {//实现了功能，但超时
        for(int i=0;i<nums.length;i++){
            int supposed = target-nums[i];
            int supposed_idx = binarySearch(nums,i+1,nums.length-1,supposed);
            if(supposed_idx==-1){
                continue;
            }else {
                System.out.print(nums[i]+","+supposed);
                return new int[]{nums[i], supposed};
            }
        }
        return null;
    }

    public int binarySearch(int[]nums,int low,int high,int target){//-1是没找到

        while (low<=high){
           int mid=(low+high)>>1;
           if(nums[mid]==target){
               //System.out.print(mid);
               return mid;
           } else {
               if(nums[mid]<target){
                   low++;
               }else {
                   if(target<nums[mid]){
                       high--;
                   }
               }
           }
        }
        return -1;
    }


    /**
     * 输入一个正整数 target ，输出所有和为 target 的连续正整数序列（至少含有两个数）。
     * 序列内的数字由小到大排列，不同序列按照首个数字从小到大排列。
     *
     */
    public int[][] findContinuousSequence(int target) {
        //TODO

        return null;
    }


    /**
     * 输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字。
     */
    public int[] spiralOrder(int[][] matrix) {
        int row_border = matrix.length-1;int row=0;
        int col_border = matrix[0].length-1;int col=0;
        int res_border =(row_border+1)*(col_border+1);
        int[] res=new int[res_border];
        int cnt=0;
        while (row<=row_border&&col<=col_border){
            //to right
            for(int c = col;c<=col_border;c++){
                System.out.print(matrix[row][c]+"\t");
                //res[cnt++]=matrix[row][c];
            }
            //to down
            for(int r=row+1;r<=row_border;r++){
                System.out.print(matrix[r][col_border]+"\t");
                //res[cnt++]=matrix[r][col_border];
            }
            if(row<row_border&&col<col_border){
                //to left
                for (int c=col_border-1;c>col;c--){
                    System.out.print(matrix[row_border][c]+"\t");
                    //res[cnt++]=matrix[row_border][c];
                }
                //to up
                for(int r=row_border;r>row;r--){
                    System.out.print(matrix[r][col]+"\t");
                    //res[cnt++]=matrix[r][col];
                }
            }
            row++;
            row_border--;
            col++;
            col_border--;
        }

        return res;
    }




    @Test
    public void test_int(){
        //fib(4);
        //tribonacci(25);
        int[] a1={10, 15, 20};//15
        int[] a2={1, 100, 1, 1, 1, 100, 1, 1, 100, 1};//6
        int[] a3={0,0,1,1};
        //minCostClimbingStairs(a3);
        int[] nums1={2,7,11,15};int target1=10;
        int[] nums2={10,26,30,31,47,60};int target2=40;
        //twoSum(nums1,target1);
        int[][]m1={{1,2,3},{4,5,6},{7,8,9}};
        int[][]m2={{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        spiralOrder(m2);

    }

    @Test
    public void test_string(){
        String s1="AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        String s2="AAAAAAAAAAAAA";
        //findRepeatedDnaSequences(s2);
        String p1="1011";String t1="1001001100";
        //BruceMatch1(p1,t1);
        //printNumbers(02.drl);
    }


}
