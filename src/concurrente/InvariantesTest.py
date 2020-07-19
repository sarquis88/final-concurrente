import fileinput
import re

def test( file ):

    line = file.readline()

    line = re.sub( "\s", "", line)
    print( line )

    while( True ):
        input('').split(" ")[0]
        line = re.sub(  r'(.*?)0(.*?)1(.*?)2(.*?)3(.*?)5(.*?)6(.*?)4(.*?)|'
                        '(.*?)0(.*?)1(.*?)7(.*?)5(.*?)6(.*?)', 
                        r'\1\2\3\4\5\6\7\8\9\10\11\12\13\14',
                        line)
        if len(line) == 0:
            break
        print( line )

file = open("../files/T-Invariantes.txt")
test( file )