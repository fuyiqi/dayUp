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

        //System.out.println(Math.min(f[cost.length-1],f[cost.length-2]));
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




    @Test
    public void test(){
        //fib(4);
        //tribonacci(25);
        int[] a1={10, 15, 20};//15
        int[] a2={1, 100, 1, 1, 1, 100, 1, 1, 100, 1};//6
        int[] a3={0,0,1,1};
        //minCostClimbingStairs(a3);

/*********************************************************/
        String s1="AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        String s2="AAAAAAAAAAAAA";
        //findRepeatedDnaSequences(s2);
        String p1="1011";String t1="1001001100";
        BruceMatch1(p1,t1);
    }




}
