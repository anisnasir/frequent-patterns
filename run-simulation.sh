R="target/TopkGraphPatterns-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
input_dir="/home/anis/Datasets/"
input="patent-graph-stream.txt"
windowSize=1000000
simulatorType=2
epsilon=0.01
delta=0.2
Tk=101306
k=5
command="java -jar ${JAR} ${simulatorType} ${input_dir} ${input} ${windowSize} ${epsilon} ${delta} ${Tk} ${k} "

$command
