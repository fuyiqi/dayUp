package tricks;


import org.testng.annotations.Test;

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
     * 第i次遍历的时候通过两两比较将大数沉底找到第n-i大的数并放在数组第n-i的位置上
     * **/
    private void bubbleSort1_1(int[] nums){
        int nums_len = nums.length;
        for(int i=0;i<nums_len;i++){
            for(int j=0;j<nums_len;j++){
                if(nums[i]<nums[j])//出现后面比前面数值大的逆序对情况
                    swap(nums,i,j);
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
     * @Description: 插入排序
     * **/
    private void insertSort(int[] nums){

    }


    /**
     * @Description: 快速排序
     * **/
    private void quickSort(int[] nums){

    }

    /**
     * @Description: 堆排序
     * **/
    private void heapSort(int[] nums){

    }



    @Test
    public void main(){
        int[]a2={10,11,13,14,15,12};
        /**排序**/
        //selectSort(a2);
        //bubbleSort1_1(a2);
        //bubbleSort1_2(a2);

        System.out.println("\n =====排序后======");
        show(a2);

    }







}










