#!/bin/sh

arr=( * )
for file in "${arr[@]}"
do
    if [[ -d "$file" && "`ls \"$file\"`" = "strings.xml" ]]
    then
        rm -r "$file"
    fi
done
