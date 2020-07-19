import fileinput
import re

def test( file ):

    line = file.readline()
    line = re.sub( "\s", "", line)
    init_len = len(line)

    while( True ):
        old = line
        line = re.sub(  r'0(.*?)((1(.*?)((D(.*?))|(2(.*?)3(.*?)))5(.*?)6((.*?)4)?)|(7(.*?)((E(.*?))|(8(.*?)9(.*?)))B(.*?)C((.*?)A)?))',
                        r'\g<1>\g<4>\g<7>\g<9>\g<10>\g<11>\g<13>\g<15>\g<18>\g<20>\g<21>\g<22>\g<24>',
                        line)
        if len(line) == 0:
            print("EXITO")
            break
        if line == old:
            print("FALLO")
            print("Transiciones restantes: \t\t" + line)
            print("Cantidad transiciones restantes: \t" + str(len(line)) )
            print("Cantidad transiciones totales: \t\t" + str(init_len) )
            break

file = open("./src/files/T-Invariantes.txt")
test( file )

#0(.*?)    (     (1(.*?)((D(.*?))|(2(.*?)3(.*?)))5(.*?)6((.*?)4)?)       |
#                (7(.*?)((E(.*?))|(8(.*?)9(.*?)))B(.*?)C((.*?)A)?)       )

