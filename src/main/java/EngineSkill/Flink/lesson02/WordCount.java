package EngineSkill.Flink.lesson02;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * SparkStreaming: 完全没有状态也不对
 *
 *   2s:
 *      hadoop,hadoop,hive
 *
 *      hadoop2,hive1
 *   2s:
 *     hadoop,hive,hive
 *
 *     hadoop1,hive2
 *
 * SparkStreaming里面有两个算子是有状态的？
 * 1. mapWithState
 * 2. updateStateByKey
 *  里面的状态使用起来不是很灵活
 *
 *
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        //步骤一：获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //步骤二：数据的输入
        DataStreamSource<String> data = env.socketTextStream("192.168.152.102", 9999);
        //步骤三：数据的处理
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne = data.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    out.collect(new Tuple2<String, Integer>(word, 1));
                }
            }
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> result = wordAndOne.keyBy(0)
                .sum(1);

        //步骤四：数据的输出
        result.print();
        //步骤五：启动任务
        env.execute("word count....");
    }
}
