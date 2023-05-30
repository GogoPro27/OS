#!/bin/bash

# Find the people that have the same surname but live in a different city
# Use the electricity.csv file

awk -F "|" '{ n=split($1, arr, " "); surname=arr[2]; if (surnames[surname] != 0) { if(surnames[surname] != $2) print surname; } else surnames[surname]=$2}' electricty.csv | sort | uniq
