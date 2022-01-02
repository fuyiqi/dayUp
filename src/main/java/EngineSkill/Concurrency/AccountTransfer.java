package EngineSkill.Concurrency;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountTransfer {
    private static final Logger log= LoggerFactory.getLogger(AccountTransfer.class);

    public static void main(String[] args) {
        new AccountTransfer().do_Contransfer_onAll();
    }



//蚂蚁面试

    private void do_Contransfer_onAll(){
        List<Account> accounts = createAccounts(9);

        for(int i=0;i<5;i++){
            new DoTransfer(accounts,3,10,10,i).start();
        }

    }

    private static void checkAllAssets(List<Account> accounts){
        log.info("=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*");
        for(Account account:accounts){
            log.info("{} 拥有资产:{}",account.name,account.remains);
        }
        log.info("=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*");
    }

    /**
     * 多人交易类
     */
    static class DoTransfer extends Thread{
        private Account first;
        private Account second;
        private final int dealRounds;
        private final int dealPostive;
        private final int dealNegative;
        private int amount;
        private final int roundIdx;
        public DoTransfer(List<Account> accounts,int dealRounds,int dealPostive,int dealNegative,int roundIdx){
            List<Account> doDealAccounts = getDealAccounts(accounts);
            first=doDealAccounts.get(0);
            second=doDealAccounts.get(1);
            this.dealRounds=dealRounds;
            this.dealPostive=dealPostive;
            this.dealNegative=dealNegative;
            this.amount = new Random().nextInt(dealPostive)-new Random().nextInt(dealNegative);
            this.roundIdx = roundIdx;
        }

        public void run(){

            for(int i=0;i<dealRounds;i++){
                long before = first.remains+second.remains;
                log.info("<=第{}-{}轮两人交易前，{}拥有{},{}拥有{}======合计:{}=>",roundIdx,i,first.name,first.remains,second.name,second.remains,before);
                if(amount>=0){
                    new DoDeals(first,second,amount).start();
                    //log.info("{}:入帐金额={}",first.name,amount);
                }else {
                    amount = -amount;
                    new DoDeals(second,first,amount).start();
                    //log.info("{}:出帐金额={}",second.name,amount);
                }
                long after = first.remains+second.remains;
                log.info("<=第{}-{}轮两人交易后，{}拥有{},{}拥有{}======合计:{}=>",roundIdx,i,first.name,first.remains,second.name,second.remains,after);
                if(before==after){
                    log.info("{},{}经历{}-{}轮交易后，帐平",first.name,second.name,roundIdx,i);
                }else {
                    log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>{},{}经历{}-{}轮交易后，帐不平,",first.name,second.name,roundIdx,i);
                }
            }


        }

    }



    /**
     * 模拟两户并发多轮转账
     * **/
    private void do_Contransfer_onTwo(List<Account> accounts,int dealRounds,int dealPostive,int dealNegative,int roundIdx){
        List<Account> doDealAccounts = getDealAccounts(accounts);
        Account first = doDealAccounts.get(0);
        Account second = doDealAccounts.get(1);
        for(int i=0;i<dealRounds;i++){
            long before = first.remains+second.remains;
            log.info("<========第{}-{}轮两人交易前，{}拥有{},{}拥有{}======合计:{}========>",roundIdx,i,first.name,first.remains,second.name,second.remains,before);
            int amount = new Random().nextInt(dealPostive)-new Random().nextInt(dealNegative);
            if(amount>=0){
                new DoDeals(first,second,amount).start();
                log.info("{}:入帐金额={}",first.name,amount);
            }else {
                amount = -amount;
                new DoDeals(second,first,amount).start();
                log.info("{}:出帐金额={}",second.name,amount);
            }
            long after = first.remains+second.remains;
            log.info("<========第{}-{}轮两人交易后，{}拥有{},{}拥有{}======合计:{}========>",roundIdx,i,first.name,first.remains,second.name,second.remains,after);
            if(before==after){
                log.info("{},{}经历{}-{}轮交易后，帐平",first.name,second.name,roundIdx,i);
            }else {
                log.error(">>>>>>>>{},{}经历{}-{}轮交易后，帐不平,",first.name,second.name,roundIdx,i);
            }
        }

    }

    private static List<Account> createAccounts(int accountNum){
        List<Account> accounts = new ArrayList<>();
        for(int i=0;i<accountNum;i++){
            int asset = new Random().nextInt(300);//Integer.MAX_VALUE);
            String name = RandomStringUtils.randomAlphabetic(4);
            accounts.add(new Account(name,asset));
        }
        return accounts;
    }

    /**从交易账户选出两个账户交易*/
    private static List<Account> getDealAccounts(List<Account> accounts){
        List<Account>list = new ArrayList<>(2);
        if(accounts.size()<2){
           log.error("市场上人少于2人，无法进行交易");
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
     * 两户交易类
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
            log.info("[start]处理{}向{}转账，交易金额为{},当前{}余额{}，{}余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
            out.toOut(dealAmount);
            come.toCome(dealAmount);
            log.info("[end]处理{}向{}转账，交易金额为{},当前{}余额{}，{}余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
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
        }

    }





}
