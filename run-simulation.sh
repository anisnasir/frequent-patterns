#!/bin/bash
JAR="target/TopkGraphPatterns-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
#input_dir="/home/anis/Datasets/"
input_dir="/Users/muhammadanisuddinnasir/Datasets/patents/"
input="patent-graph-stream.txt"
windowSize=100000
simulatorType=5
epsilon=0.01
delta=0.2
Tk=101306
k=5
command="java -Xmx4092M -jar ${JAR} ${simulatorType} ${input_dir} ${input} ${windowSize} ${epsilon} ${delta} ${Tk} ${k} "

$command
