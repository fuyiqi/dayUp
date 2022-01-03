package EngineSkill.Concurrency;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class AccountTransfer {
    private static final AtomicLong totalInit = new AtomicLong();

    private static final Logger log= LoggerFactory.getLogger(AccountTransfer.class);

    public static void main(String[] args) {
        new AccountTransfer().do_Contransfer_onAll();
    }



//蚂蚁面试

    private void do_Contransfer_onAll(){
        List<Account> accounts = createAccounts(9);
        log.info("初始总账：{}",totalInit.get());
        for(int i=0;i<5;i++){
            new DoTransfer(accounts,3,10,10).start();
        }
        long sum = accounts.stream().map(account -> account.remains.get()).mapToLong(t -> t).sum();
        log.info("交易后总账：{}",sum);
        if(totalInit.get() == sum){
            log.info("总账平");
        }


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
            int asset = new Random().nextInt(300);//Integer.MAX_VALUE);
            totalInit.addAndGet(asset);
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

        @Override
        public void run() {
            super.run();
            if(dealAmount>0){
                log.info("[start]处理{}向{}转账，交易金额为{},当前{}余额{}，{}余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
            }else {
                log.info("[start]处理{}向{}转账，交易金额为{},当前{}余额{}，{}余额{}",come.name,out.name,-dealAmount,come.name,come.remains,out.name,out.remains);
            }
            out.toOut(dealAmount);
            come.toCome(dealAmount);
            if(dealAmount>0){
                log.info("[end]处理{}向{}转账，交易金额为{},当前{}余额{}，{}余额{}",out.name,come.name,dealAmount,out.name,out.remains,come.name,come.remains);
            }else {
                log.info("[end]处理{}向{}转账，交易金额为{},当前{}余额{}，{}余额{}",come.name,out.name,-dealAmount,come.name,come.remains,out.name,out.remains);
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
