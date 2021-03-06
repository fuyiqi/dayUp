package tricks;


import org.testng.annotations.Test;

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

    }


/************************************************************************************/
    private void selectSort(int[] nums){

    }





    @Test
    public void main(){
        int[] a1 = {4,7,9,1,5,99,-98};
        show(a1);


    }







}










