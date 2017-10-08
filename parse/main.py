import sys


f = open(sys.argv[1]).read().split("compareResult")

searchTime = "";
compareTime = "";
flag = False
searchResult = []
compareResult = []

for index, i in enumerate(f):
    result = i.split("\n")
    if index == 0:
        del result[0]
        del result[0]
        del result[0]
    for l in result:
        if "searchTime" in l:
            searchTime = l
            print l
        elif "CompareTime" in l:
            compareTime = l
            print l
        elif "" == l:
            continue
        else:
            splitstr = l.split(",")
            if index == 0:
                searchResult.append(float(splitstr[len(splitstr) - 1]))
            else:
                compareResult.append(float(splitstr[len(splitstr) - 1]))

# print searchResult
result = [x / max(searchResult) for x in searchResult]
result_threshold = [x / max(searchResult) for x in searchResult if x / max(searchResult) >= float(sys.argv[2])]
# print result
# print result_threshold
# print len(result)
print len(result_threshold)
print
# print compareResult
print len([i for i in compareResult[0:len(result_threshold) - 1] if i >= 0.75])
