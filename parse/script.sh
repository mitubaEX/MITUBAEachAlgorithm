for l in 2-gram 3-gram 4-gram 5-gram 6-gram uc ; do find ../csv -name "$l_*.csv" | while read file ; do for i in 0.75 0.5 0.25 ; do python main.py "$file" "$i" > ./result/`basename "$file"`_result_"$i".txt;done; done;done

