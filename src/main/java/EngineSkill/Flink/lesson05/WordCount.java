package EngineSkill.Flink.lesson05;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * flink 给了一个最佳实践
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        //步骤一：获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hostname = parameterTool.get("hostname");
        int port = parameterTool.getInt("port");
        //步骤二：数据的输入
        DataStreamSource<String> data = env.socketTextStream(hostname,port);
        //步骤三：数据的处理
        SingleOutputStreamOperator<WordAndCount> result = data.flatMap(new SplitLine()).keyBy("word")
                .sum("count");
        //步骤四：数据的输出
        result.print();
        //步骤五：启动任务
        env.execute("word count....");
    }




    public static class SplitLine implements  FlatMapFunction<String,WordAndCount>{
        @Override
        public void flatMap(String line, Collector<WordAndCount> out) throws Exception {
            String[] fields = line.split(",");
            for (String word : fields) {
                out.collect(new WordAndCount(word, 1));
            }
        }
    }



    public static class WordAndCount{
        private String word;
        private int count;

        public WordAndCount(){

        }

        @Override
        public String toString() {
            return "WordAndCount{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }

        public WordAndCount(String word, int count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
