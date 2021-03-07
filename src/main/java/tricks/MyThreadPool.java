package tricks;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPool {


    public static void main(String[] args){
        //创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for(int i=0;i<6;i++){
            executorService.submit(new Task(""+i));
        }

        //关闭线程池
        executorService.shutdown();



    }


    static class Task implements Runnable{
        private final String taskName;
        public Task(String name){
            this.taskName=name;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName()+" StartTask==>"+taskName);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){

            }
            System.out.println(Thread.currentThread().getName()+" FinishTask==>"+taskName);
        }
    }











}
