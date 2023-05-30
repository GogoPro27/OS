#!/bin/bash

# Calculate the average electricity consumption for the city Debar for the month of June
# Use the electricity.csv file

awk -F "|" 'BEGIN {sum=0; count=0;} { if ( $2 ~ "Debar" && $3 ~ "06") {sum+=$NF; count+=1;}; } END {print sum/count}' electricty.csv 
