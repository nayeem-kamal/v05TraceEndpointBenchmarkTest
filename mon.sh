#!/bin/bash

if [ -f ~/result ]; then
	rm ~/result
fi
if [ -f ~/result-trace-agent ]; then
	rm ~/result-trace-agent
fi


if [ -f ~/result-java-agent ]; then
	rm ~/result-java-agent
fi

echo "tagentcpu,tagentmem,,agentcpu,agentmem,pagentcpu,pagentmem,javacpu,javamem" >> ~/result

service="v04"
v05="false"

if [ "$1" = "v04" ]; then
	v05="false"
	service="v04"
else
	v05="true"
	service="v05"
fi

javacpusum=0
javamemsum=0
agentcpusum=0
sgentmemsum=0
count=0

java -javaagent:$4 -Ddd.service=$service -Ddd.trace.agent.v0.5.enabled=$v05 -Ddd.trace.debug=true -jar ~/v05TraceEndpointBenchmarkTest/target/v05Test-0.0.1-SNAPSHOT.jar $2 $3 $5 &
JAVA_PID=$!
while ps -p $JAVA_PID >/dev/null
do
	count=$count+1
	ps -C trace-agent -o %cpu,%mem | tail -n1 | while read one two; do echo -n "$one,$two," >> ~/result; done;
	ps -C agent -o %cpu,%mem | tail -n1 | while read one two; do echo -n "$one,$two," >> ~/result; done;
	ps -C process-agent -o %cpu,%mem | tail -n1 | while read one two; do echo -n "$one,$two," >> ~/result; done;

	ps -p $JAVA_PID -o %cpu,%mem | tail -n1 | while read one two; do echo "$one,$two" >> ~/result; done; 
#	ps -C trace-agent -o %cpu,%mem | tail -n1 >> ~/result-trace-agent
#	ps -p $JAVA_PID -o %cpu,%mem | tail -n1 >> ~/result-java-agent
done
