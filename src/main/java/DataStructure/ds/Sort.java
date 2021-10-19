package DataStructure.ds;


import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * @Description: 排升序
 * **/
public class Sort {

    private void show(int[] nums){
        for(int e:nums){
            System.out.print(e+"\t");
        }
    }

    private void swap(int[]nums, int pos1, int pos2){
        int tmp = nums[pos1];
        nums[pos1] = nums[pos2];
        nums[pos2] = tmp;
    }

    private void reverse(int[] nums,int left,int right){
        while (left<right){
            swap(nums,left++,right--);
        }
    }


    /**
     * @Description: 选择排升序
     * 有序部分nums[0,i) 无序部分nums[i,n)
     * 第i次遍历的时候找到第i小的数并放在数组第i的位置上
     * **/
    private void selectSort(int[] nums){
        int nums_len = nums.length;
        for(int i=0;i<nums_len;i++){
            for(int j=i+1;j<nums_len;j++){
                if(nums[j]<nums[i])//出现后面比前面数值小的逆序对情况
                    swap(nums,i,j);
            }
        }
    }

    /**
     * @Description: 冒泡排升序
     * 第i次遍历的时候通过两两比较将大数沉底找到第n-i-1大的数并放在数组第n-i-1的位置上
     * **/
    private void bubbleSort1_1(int[] nums){
        int nums_len = nums.length;
        for(int i=0;i<nums_len;i++){
            for(int j=0;j<nums_len-1-i;j++){//n-i-1的位置已有序
                if(nums[j+1]<nums[j])//出现前面比后面数值大的逆序对情况
                    swap(nums,j,j+1);
            }
        }
    }

    /**
     * @Description: 冒泡排升序
     * 考虑平凡的情况，将数组nums划分为了两部分，无序部分nums[0,i),有序部分nums[i,n)
     * 通过两两比较将大数沉底从而扩大有序部分，无序部分范围缩小，此消彼长完成排序
     * **/
    private void bubbleSort1_2(int[] nums){
        int num_len = nums.length;
        for(boolean globalSorted=false;globalSorted=!globalSorted;num_len--){
            for (int i=1;i<num_len;i++){
                if(nums[i]<nums[i-1]){
                    swap(nums,i-1,i);
                    globalSorted=false;//清除全局有序标志
                }
            }
        }
    }

    //TODO 冒泡排序优化版本


    /**
     * @Description: 二路归并排序
     * **/
    private void biMergeSort(){

    }




    /**
     * @Description: 插入排升序
     * 将数组nums[0,n)划分为有序的部分nums[0,i)，无序的部分nums[i,n)，
     * 第i次的迭代可以通过两两比较的方式将nums[i]插入到有序部分的合适位置上
     * **/
    private void insertSort(int[] nums){
        int low=0;int nums_len=nums.length-1;
        while (++low<=nums_len){
            int findPos = low;
            while (findPos>=1&&nums[findPos]<nums[findPos-1]){//通过逆序对由后向前找位置
               swap(nums,findPos-1,findPos);
                findPos--;//继续由后向前找位置
            }
        }
    }





    /**
     * @Description: 快速排序
     * **/
    private void quickSort(int[] nums,int low,int high){
        if(low>=high){
            return;
        }
        int pivot_index = getPivotIndex(nums,low,high);
        quickSort(nums,low,pivot_index-1);
        quickSort(nums,pivot_index+1,high);
    }

    private int getPivotIndex(int[] nums,int low,int high){
        int pivot = nums[low];
        while (low<high){
            while (pivot<=nums[high]&& low<high){//大于等于pivot的部分
                high--;
            }
            //不是这个部分的，划到小于pivot的部分
            nums[low]=nums[high];

            while (nums[low]<pivot&& low<high){//小于pivot的部分
                low++;
            }
            //不是这个部分的，划到大于等于pivot的部分
            nums[high]=nums[low];
            //pivot放到合适的位置
            nums[low]=pivot;
        }
        return low;
    }

 /*#*#*#*#*#*#*#*#*#*#*/

    /**
     * @Description: 堆排升序
     * 具有完全二叉树的小顶堆，它的每一个节点i，其值不大于它的左子节点(2i+01.drl)的值和右子节点(2i+02.drl)的值，
     * 它的最后一个非叶子节点n/02.drl-01.drl
     * **/
    private void heapSort(int[] nums){
        //初始化阶段--将数组调整为小顶堆
        int last_non_leaf=nums.length>>1-1;
        for(int i=last_non_leaf;i>=0;i--){

        }
    }

    /**
     * 调整数组为符合小顶堆结构
     * 由下至上，从右到左的调整
     * @param nums 数组头指针
     * @param non_leaf 非叶子节点
     * @param len 调整的数组区间长度
     */
    private void adjustHeap(int[] nums,int non_leaf,int len){
        int tmp = nums[non_leaf];
        for(int i=non_leaf<<1+1;i<len;i=i<<1+1){//该节点的左子节点开始
            //TODO
        }

    }


    /**
     * @Description: 希尔排序
     * **/
    private void shellSort(int[] nums){

    }

    @Test
    public void main(){
        int[]a2={10,11,13,14,15,12};
        /**排序**/
        //selectSort(a2);
        bubbleSort1_1(a2);
        //bubbleSort1_2(a2);
        //insertSort(a2);
        //quickSort(a2,0,5);
        int[] a={4,6,8,5,9};
        //heapSort(a);
        System.out.println("\n =====排序后======");
        show(a2);

    }

    @Test
    /**
     * @Description: 合并两个有序数组<占用空间>
     */
    private void twoSorted2One(){
        int[] a1 = {1,5,9};
        int[] a2 = {2,99,100,101};

        int[] longger,shorter;
        if(a1.length>a2.length){
            longger=a1;
            shorter=a2;
        }else {
            longger=a2;
            shorter=a1;
        }

        int shorter_low=0,shorter_len=shorter.length;
        int longger_low=0,longger_len=longger.length;
        int cnt=0;int[] res = new int[shorter_len+longger_len];

        while (shorter_low<shorter_len && longger_low<longger_len){

                while (shorter_low<shorter_len && shorter[shorter_low] < longger[longger_low]) {
                    res[cnt++]=shorter[shorter_low++];
                }
                while (shorter_low<shorter_len && longger[longger_low] <= shorter[shorter_low]) {
                    res[cnt++]=longger[longger_low++];
                }

        }
        while (longger_low<longger_len){
            res[cnt++]=longger[longger_low++];
        }

        //show(res);

    }

    @Test
    private void twoSorted2OneLocal(){

        int[] nums = {2,99,100,101,1,5,9};
        int low=3;int nums_len=nums.length;
        while (++low<nums_len){
            int findPos = low;
            while (findPos>=1&&nums[findPos]<nums[findPos-1]){//通过逆序对由后向前找位置
                swap(nums,findPos-1,findPos);
                findPos--;//继续由后向前找位置
            }
        }
        show(nums);

    }



    @Test
    /**
     * 通过pivot将nums[0,n)划分为小于pivot的部分和大于等于pivot的部分
     * **/
    private void getPartitionIndex(){
        int[] a = {66,99,102,4,67,8};

        //假设以首元素为pivot
        int pivot = a[0];

        int low=0,high=a.length-1;
        while (low<high){

            while (pivot<=a[high]&& low<high){//大于等于pivot的部分
                high--;
            }
            //不是这个部分的，划到小于pivot的部分
            a[low]=a[high];

            while (a[low]<pivot&& low<high){//小于pivot的部分
                low++;
            }
            //不是这个部分的，划到大于等于pivot的部分
            a[high]=a[low];
            //pivot放到合适的位置
            a[low]=pivot;


        }
        //System.out.println(low);

    }


    /**
     * @Description：最小k个数
     */
    public ArrayList<Integer> getLeastKNumber(int[] nums,int k){
        ArrayList<Integer> res = new ArrayList<Integer>();
        if(nums.length==0||nums.length<k||k<=0){
           return res;
        }
        int low=0,high=nums.length-1;
        int pivot_index=getPivotIndex(nums,low,high);
        while (pivot_index!=k-1){
            if(k-1<pivot_index){
                pivot_index=getPivotIndex(nums,low,pivot_index-1);
            }else {
                if(pivot_index<k-1){
                    pivot_index=getPivotIndex(nums,pivot_index+1,high);
                }
            }
        }

        for (int i =0;i<k;i++){
            //System.out.print(nums[i]+"\t");
            res.add(nums[i]);
        }

        return res;
    }


    @Test
    private void test_leats(){
        int[] a = {66,99,102,4,67,8};
        getLeastKNumber(a,3);
    }
















}










