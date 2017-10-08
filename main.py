
import sys
import commands

f = open(sys.argv[1]).read().split("\n")

for (index, i) in enumerate(f):
    f_ = open('test.txt', 'w')
    f_.write(i)
    f_.close()
    output = commands.getoutput("java -jar build/libs/searchEachAlgorithm.jar test.txt " + sys.argv[2])
    # print output
    outputfile = open("./csv/" + sys.argv[2] + "_" + str(index) + ".csv", 'w')
    outputfile.write(output)
    outputfile.close()


