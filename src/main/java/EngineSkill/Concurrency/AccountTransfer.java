package EngineSkill.Concurrency;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AccountTransfer {

    private static final Logger log= LoggerFactory.getLogger(AccountTransfer.class);

    public static void main(String[] args) throws InterruptedException {
        new AccountTransfer().do_mytransfer_on_all();
    }






    private void do_mytransfer_on_all() throws InterruptedException {
        List<Account> accounts = createAccounts(1000);
        checkAccountDetail(accounts);
        for(int i=0;i<1000;i++){
            new DoTransfer(accounts,3,100,100).start();
        }
        Thread.sleep(200);
        checkAccountDetail(accounts);
    }

    private void checkAccountDetail(List<Account> accounts){
        String str = "";
        long sum = 0;
        for(Account a:accounts){
            String tmp = "%s拥有%s\n";
            str+=String.format(tmp,a.name,a.remains.get());
            sum+=a.remains.get();
        }
        log.info("{}市场上资产总额:{}",str,sum);
    }


    /**
     * 多人交易类
     */
    static class DoTransfer extends Thread{
        private final Account first;
        private final Account second;
        //选取两人交易的次数
        private final int dealRounds;
        //交易数额
        private final int dealAmount;
        public DoTransfer(List<Account> accounts,int dealRounds,int dealPositive,int dealNegative){
            List<Account> doDealAccounts = getDealAccounts(accounts);
            first=doDealAccounts.get(0);
            second=doDealAccounts.get(1);
            this.dealRounds=dealRounds;
            this.dealAmount = new Random().nextInt(dealPositive)-new Random().nextInt(dealNegative);
        }

        public void run(){
            for(int i=0;i<dealRounds;i++){
                new DoDeals(first,second,dealAmount).start();
            }
        }
    }



    private static List<Account> createAccounts(int accountNum){
        List<Account> accounts = new ArrayList<>();
        for(int i=0;i<accountNum;i++){
            int asset = 100;//new Random().nextInt(100);
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
            HashSet<Integer> set = new HashSet<>();
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
        private final Account out;
        private final Account come;
        //交易金额
        private final int dealAmount;
        public DoDeals(Account out,Account come, int dealAmount){
            this.out = out;
            this.come = come;
            this.dealAmount = dealAmount;
        }
        private final Lock lock  = new ReentrantLock();
        private final Condition condition = lock.newCondition();
        @Override
        public void run() {
            boolean getLockFlag = false;
            try {
                getLockFlag = lock.tryLock(200, TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(getLockFlag){
                //log.info("获锁成功");
                try{
                    //保障出账不能成为负资产
                    boolean isOutValid = true;
                    if(dealAmount>0){
/*                        log.info("[start]处理{}向{}转账，交易金额为{},当前{}余额{},{}余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
                        isOutValid = out.remains.get()- (long) dealAmount >=0;
                        if(isOutValid){//出账后不是负资产
                            out.toOut(dealAmount);
                            come.toCome(dealAmount);
                            log.info("[end]处理{}向{}转账，交易金额为{},当前{}余额{},{}余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
                        }else{//出账后是负资产
                            log.info("[end]===不足===无法处理{}向{}转账，当前{}余额{},转账金额{}",out.name,come.name,out.name,out.remains,dealAmount);
                        }*/
                        log.info("[start]处理{}向{}转账，交易金额为{},当前{}余额{},{}余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
                        out.toOut(dealAmount);
                        come.toCome(dealAmount);
                        log.info("[end]处理{}向{}转账，交易金额为{},当前{}余额{},{}余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
                    }else {
/*                        log.info("[start]处理{}向{}转账，交易金额为{},当前{}余额{},{}余额{}",come.name,out.name,-dealAmount,come.name,come.remains,out.name,out.remains);
                        isOutValid = come.remains.get()+ (long) dealAmount >=0;
                        if(isOutValid) {//出账后不是负资产
                            out.toOut(dealAmount);
                            come.toCome(dealAmount);
                            log.info("[end]处理{}向{}转账，交易金额为{},当前{}余额{},{}余额{}",come.name,out.name,-dealAmount,come.name,come.remains,out.name,out.remains);
                        }else {
                            log.info("[end]===不足===无法处理{}向{}转账，当前{}余额{},转账金额{}",come.name,out.name,come.name,come.remains,-dealAmount);
                        }*/
                        log.info("[start]处理{}向{}转账，交易金额为{},当前{}余额{},{}余额{}",come.name,out.name,-dealAmount,come.name,come.remains,out.name,out.remains);
                        out.toOut(dealAmount);
                        come.toCome(dealAmount);
                        log.info("[end]处理{}向{}转账，交易金额为{},当前{}余额{},{}余额{}",come.name,out.name,-dealAmount,come.name,come.remains,out.name,out.remains);
                    }
                }catch (Exception e){
                    log.error("[DoDeals.run()]Exception->",e);
                }finally {
                    lock.unlock();
                }
            }else{
                log.warn("获锁失败");
            }

        }

    }


    /**
     * 账户类
     **/
    static class Account{
        //账户名
        private final String name;
        //账户余额
        private final AtomicLong remains;

        //出账
        private void toOut(int outAmount){
            remains.addAndGet(-outAmount);
        }

        //入账
        private void  toCome(int comeAmount){
            remains.addAndGet(comeAmount);
        }

        public Account (String name,int asset){
            this.name = name;
            this.remains = new AtomicLong(asset);
        }

    }

}
