package EngineSkill.Concurrency;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyParallel {
    private static final Logger log= LoggerFactory.getLogger(MyParallel.class);

    public static void main(String[] args) {
        new MyParallel().do_transfer();
    }

//蚂蚁面试

    /**模拟市场上人们转账**/
    public  void do_transfer(){
        List<Account> accounts = createAccounts(5);
        List<Account> doDealAccounts = getDealAccounts(accounts);
        Account out = doDealAccounts.get(0);
        Account come = doDealAccounts.get(1);
        int dealNum = 10;
        int randomPostive = 10;
        int randomNegative = 20;
        log.info("交易前，{}拥有{},{}拥有{}======合计:{}",out.name,out.remains,come.name,come.remains,out.remains+come.remains);
        for(int i=0;i<dealNum;i++){
            int amount = new Random().nextInt(randomPostive)-new Random().nextInt(randomNegative);
            int dealAmount = Math.abs(amount);
            if(amount>=0){
                log.info("{}:入帐金额={}",out.name,dealAmount);
            }else {
                log.info("{}:出帐金额={}",come.name,dealAmount);
            }
            new DoDeals(out,come,dealAmount).start();
        }
        log.info("交易后，{}拥有{},{}拥有{}======合计:{}",out.name,out.remains,come.name,come.remains,out.remains+come.remains);
    }

    private List<Account> createAccounts(int accountNum){
        List<Account> accounts = new ArrayList<>();
        for(int i=0;i<accountNum;i++){
            int asset = new Random().nextInt(Integer.MAX_VALUE);
            String name = RandomStringUtils.randomAlphabetic(4);
            accounts.add(new Account(name,asset));
        }
        return accounts;
    }

    /**从交易账户选出两个账户交易*/
    private List<Account> getDealAccounts(List<Account> accounts){
        List<Account>list = new ArrayList<>(2);
        if(accounts.size()<2){
           log.error("无法进行交易");
        }else {
            HashSet<Integer> set = new HashSet<Integer>();
            while(set.size()<2){
                int random = (int) (Math.random() * accounts.size());
                if (!set.contains(random)){
                    set.add(random);
                    list.add(accounts.get(random));
                }
            }
        }
        return list;
    }

    /**
     * 交易类
     */
    static class DoDeals extends Thread{
        private Account out;
        private Account come;
        private int dealAmount;
        public DoDeals(Account out,Account come, int dealAmount){
            this.out = out;
            this.come = come;
            this.dealAmount = dealAmount;
        }

        @Override
        public void run() {
            super.run();
            log.info("[start]处理{}向{}转账，交易金额为{},当前{}的余额{}，{}的余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
            out.toOut(dealAmount);
            come.toCome(dealAmount);
            log.info("[end]处理{}向{}转账，交易金额为{},当前{}的余额{}，{}的余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
        }
    }


    /**
     * 账户类
     **/
    static class Account{
        //账户名
        private final String name;
        //账户余额
        private volatile long remains ;
        //动帐锁
        private static final Lock lock = new ReentrantLock();
        //出账条件
        private static final Condition outCondition = lock.newCondition();

        //出账
        private void toOut(int outAmount){
           try{
                lock.lock();
                if(remains-outAmount>0){//允许出账的条件
                    remains = remains-outAmount;
                }else{//帐已空
                    log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!{}:余额为空",this.name);
                    outCondition.await();

                }
            }catch (Exception e){
                log.error("{}:出账Exception->",this.name,e);
            }finally {
                lock.unlock();
            }
        }

        //入账
        private void toCome(int comeAmount){
            remains = remains+comeAmount;
        }

        public Account (String name,int asset){
            this.name = name;
            this.remains = asset;
            log.info("{}现有金额{}",this.name,this.remains);
        }

    }





}
