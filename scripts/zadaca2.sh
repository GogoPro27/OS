#!/bin/bash

if [ $# -lt 1 ]
then
	echo "USAGE: `basename $0` username"
	exit 1
fi

if [ -f "out.txt" ]
then
	rm out.txt
fi

for proc in `ps -ef | grep $1 | awk '{print $2;}' | sort -n | uniq`
do
	count=0
	for pproc in `ps -ef | grep $1 | awk '{print $3;}'`
	do
		if [ $proc -eq $pproc ]
		then
			count=$(($count + 1))
		fi
	done
	echo "$proc $count" >> out.txt
done

cat out.txt


