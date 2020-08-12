import fileinput
import re

def test_colectivo_2inv( line ):
    # print mensaje
    print("\nTest colectivo (2 inv):")

    # definicion de valores
    invariante = r'0(.*?)(1(.*?)2(.*?)3(.*?)5(.*?)6(.*?)4|7(.*?)8(.*?)9(.*?)B(.*?)C(.*?)A)'
    remplazo_invariante = r'\g<1>\g<3>\g<5>\g<6>\g<7>\g<8>\g<9>\g<10>\g<11>\g<12>'
    garbage = r'D|E'
    remplazo_garbage = r''

    # remplazo de invariante garbagecollector
    line = re.sub( garbage, remplazo_garbage, line)

    # remplazo de invariante
    old = " "
    while old != line:
        old = line
        line = re.sub( invariante, remplazo_invariante, line)

    # chequeo final
    if len(line) == 0:
        print("VACIO")
    else:
        print("RESTANTES: " + str(len(line)) )
        print( line )

###############################################################################

def test_colectivo_4inv( line ):
    # print mensaje
    print("\nTest colectivo (4 inv):")

    # definicion de valores
    invariante = r'0(.*?)(1(.*?)(2(.*?)3(.*?)5(.*?)6(.*?)4|D(.*?)5(.*?)6)|7(.*?)(8(.*?)9(.*?)B(.*?)C(.*?)A|E(.*?)B(.*?)C))'
    remplazo_invariante = r'\g<1>\g<3>\g<5>\g<6>\g<7>\g<8>\g<9>\g<10>\g<11>\g<13>\g<14>\g<15>\g<16>\g<17>\g<18>'

    # remplazo de invariante
    old = " "
    while old != line:
        old = line
        line = re.sub( invariante, remplazo_invariante, line)

    # chequeo final
    if len(line) == 0:
        print("VACIO")
    else:
        print("RESTANTES: " + str(len(line)) )
        print( line )

###############################################################################

def test_mono(file):

    print("")
    print("Test mono:")

    # definicion de valores
    invariante = r'0(.*?)1(.*?)2(.*?)3(.*?)5(.*?)6(.*?)4'
    remplazo_invariante = r'\g<1>\g<2>\g<3>\g<4>\g<5>\g<6>'
    #invariante = r'0(.*?)1(.*?)(2(.*?)3(.*?)5(.*?)6(.*?)4|7(.*?)5(.*?)6(.*?))'
    #remplazo_invariante = r'\g<1>\g<2>\g<4>\g<5>\g<6>\g<7>\g<8>\g<9>\g<10>'
    resto = r'0(.*?)1(.*?)5(.*?)6'
    remplazo_resto = r'\g<1>\g<2>\g<3>'
    garbage = r'7'
    remplazo_garbage = r''

    # lectura de archivo
    line = file.readline()
    line = re.sub("\s", "", line)

    print( "Transiciones totales: " + str(len(line)) )

    # remplazo de invariante
    old = " "
    while old != line:
        old = line
        line = re.sub( invariante, remplazo_invariante, line)

    # remplazo de invariante garbagecollector
    line = re.sub( garbage, remplazo_garbage, line)

    # remplazo de invariante incompleta
    old = " "
    while old != line:
        old = line
        line = re.sub( resto, remplazo_resto, line)

    # chequeo final
    if len(line) == 0:
        print("VACIO")
    else:
        print("RESTANTES: " + str(len(line)) )
        print( line )

###############################################################################

def __main__():
    file = open("./src/files/transiciones.txt")
    fileA = open("./src/files/transicionesA.txt")
    fileB = open("./src/files/transicionesB.txt")

    line1 = file.readline()
    line1 = re.sub("\s", "", line1)
    line2 = line1
    print( "\nTransiciones totales: " + str( len( line1 ) ) )

    test_colectivo_2inv( line1 )
    test_colectivo_4inv( line2 )

    print("")

###############################################################################

__main__()