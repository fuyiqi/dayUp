package EngineSkill.Flink.lesson07;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;


public class WordCount {
    public static void main(String[] args) throws Exception{
        //步骤一，获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(3);
        //步骤二，获取数据源
        DataStreamSource<String> data = env.socketTextStream("192.168.152.102", 9999);
        //source 1 task
        //步骤三：数据处理
        //flatMap 3 task
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = data.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] fields = s.split(",");
                for (String word : fields) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        })
                .keyBy(0)
                .sum(1); //keyby sum print 3 task
        //步骤四：数据输出
        result.print();//
        //步骤五：启动任务
        env.execute("word count");
        /**
         *
         * 有同学能计算出来吗？这个任务运行起来，应该有几个task
         *
         */
    }
}
