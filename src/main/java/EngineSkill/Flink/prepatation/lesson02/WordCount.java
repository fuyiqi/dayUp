package EngineSkill.Flink.prepatation.lesson02;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WordCount {
    public static void main(String[] args) throws Exception {
        //步骤一：获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(2);
        //步骤二：获取数据源
        DataStreamSource<String> dataStream = env.socketTextStream("192.168.152.102", 9999);
        //步骤三：数据处理
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne = dataStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordCount = wordAndOne.keyBy(0)
                .sum(1);
        //步骤四：数据输出
        wordCount.print();
        //步骤五：启动任务
        env.execute("word count ...");
    }
}
