#!/bin/bash

if [ $# -lt 1 ]
then
	echo "USAGE: `basename $0` username"
	exit 1
fi

logins=`last | grep $1`
times=`echo "$logins" | awk '{ print $10; }'`
cleanTimes=`echo "$times" | sed -e 's/(//' -e 's/)//'`
minutes=`echo "$cleanTimes" | awk -F : ' /[1-9]\+.*/ { split($1, arr, "+"); day=arr[1]; hour=arr[2]; print $day*24*60+$hour*60+$2; } !/[1-9]\+.*/ { print $1*60 + $2; }'`

total=0
for minute in $minutes
do
	total=$(($total + $minute))
done

echo $total > out.txt
cat out.txt
