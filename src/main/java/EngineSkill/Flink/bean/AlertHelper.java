package EngineSkill.Flink.bean;

import java.util.Random;

public class AlertHelper {
    private static SnowflakeIdWorker ider = new SnowflakeIdWorker(0, 0);
    public static AlertHelper getInstance(){
        return Holder._instance;
    }
    private static class Holder{
        private static final AlertHelper _instance = new AlertHelper();
    }

    private AlertHelper(){

    }

    private Alert buildAlert(){
        long eventid = ider.nextId();
        String title = "alert_title";
        String text = "alert_text";
        String ipv4 = getRandomIpv4();
        return new Alert(eventid,title,text,ipv4);
    }

    public Alert getMajorAlert(){
        Alert alert = buildAlert();
        alert.setStatus(2);
        return alert;
    }

    public Alert getMinorAlert(){
        Alert alert = buildAlert();
        alert.setStatus(3);
        return alert;
    }

    public Alert getWarnAlert(){
        Alert alert = buildAlert();
        alert.setStatus(4);
        return alert;
    }

    public Alert getClearAlert(){
        Alert alert = buildAlert();
        alert.setStatus(6);
        return alert;
    }


    /**
     * 获取一个随机IP
     */
    private static String getRandomIpv4() {

        // 指定 IP 范围
        int[][] range = {
                {607649792, 608174079}, // 36.56.0.0-36.63.255.255
                {1038614528, 1039007743}, // 61.232.0.0-61.237.255.255
                {1783627776, 1784676351}, // 106.80.0.0-106.95.255.255
                {2035023872, 2035154943}, // 121.76.0.0-121.77.255.255
                {2078801920, 2079064063}, // 123.232.0.0-123.235.255.255
                {-1950089216, -1948778497}, // 139.196.0.0-139.215.255.255
                {-1425539072, -1425014785}, // 171.8.0.0-171.15.255.255
                {-1236271104, -1235419137}, // 182.80.0.0-182.92.255.255
                {-770113536, -768606209}, // 210.25.0.0-210.47.255.255
                {-569376768, -564133889}, // 222.16.0.0-222.95.255.255
        };

        Random random = new Random();
        int index = random.nextInt(10);
        String ip = num2ip(range[index][0] + random.nextInt(range[index][1] - range[index][0]));
        return ip;
    }

    /*
     * 将十进制转换成IP地址
     */
    private static String num2ip(int ip) {
        int[] b = new int[4];
        b[0] = (ip >> 24) & 0xff;
        b[1] = (ip >> 16) & 0xff;
        b[2] = (ip >> 8) & 0xff;
        b[3] = ip & 0xff;
        // 拼接 IP
        String x = b[0] + "." + b[1] + "." + b[2] + "." + b[3];
        return x;
    }


/**
 * Twitter_Snowflake
 * SnowFlake的结构如下(每部分用-分开):
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 * 加起来刚好64位，为一个Long型。
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
static class SnowflakeIdWorker {

        // ==============================Fields===========================================
        /** 开始时间截 (2020-01-01) */
        private final long twepoch = 1577808000000L;

        /** 机器id所占的位数 */
        private final long workerIdBits = 5L;

        /** 数据标识id所占的位数 */
        private final long datacenterIdBits = 5L;

        /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
        private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

        /** 支持的最大数据标识id，结果是31 */
        private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

        /** 序列在id中占的位数 */
        private final long sequenceBits = 12L;

        /** 机器ID向左移12位 */
        private final long workerIdShift = sequenceBits;

        /** 数据标识id向左移17位(12+5) */
        private final long datacenterIdShift = sequenceBits + workerIdBits;

        /** 时间截向左移22位(5+5+12) */
        private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

        /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
        private final long sequenceMask = -1L ^ (-1L << sequenceBits);

        /** 工作机器ID(0~31) */
        private long workerId;

        /** 数据中心ID(0~31) */
        private long datacenterId;

        /** 毫秒内序列(0~4095) */
        private long sequence = 0L;

        /** 上次生成ID的时间截 */
        private long lastTimestamp = -1L;

        //==============================Constructors=====================================
        /**
        * 构造函数
        * @param workerId 工作ID (0~31)
        * @param datacenterId 数据中心ID (0~31)
        */
        public  SnowflakeIdWorker(long workerId, long datacenterId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
            }
            if (datacenterId > maxDatacenterId || datacenterId < 0) {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
            }
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }

        // ==============================Methods==========================================
        /**
        * 获得下一个ID (该方法是线程安全的)
        * @return SnowflakeId
        */
        public synchronized long nextId() {
            long timestamp = timeGen();

            //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(
                String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }

            //如果是同一时间生成的，则进行毫秒内序列
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                //毫秒内序列溢出
                if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
                }
            }
            //时间戳改变，毫秒内序列重置
            else {
                sequence = 0L;
            }

            //上次生成ID的时间截
            lastTimestamp = timestamp;

            //移位并通过或运算拼到一起组成64位的ID
            return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
        }

        /**
        * 阻塞到下一个毫秒，直到获得新的时间戳
        * @param lastTimestamp 上次生成ID的时间截
        * @return 当前时间戳
        */
        protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        /**
        * 返回以毫秒为单位的当前时间
        * @return 当前时间(毫秒)
        */
        protected long timeGen() {
            return System.currentTimeMillis();
        }


    }




}
