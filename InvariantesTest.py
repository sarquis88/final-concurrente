import fileinput
import re

def test_individual(file, cpu):

    print("")
    print("Test individial CPU " + cpu + ":")
    # definicion de valores de acuerdo al CPU
    if cpu == 'a':
        invariante = r'1(.*?)2(.*?)3(.*?)5(.*?)6(.*?)4'
        resto = r'1(.*?)5(.*?)6'
        garbage = r'D'
    elif cpu == 'b':
        invariante = r'7(.*?)8(.*?)9(.*?)B(.*?)C(.*?)A'
        resto = r'7(.*?)B(.*?)C'
        garbage = r'E'
    else:
        print( "CPU no conocido")
        return

    # lectura de archivo
    line = file.readline()
    line = re.sub("\s", "", line)

    # remplazo de invariante
    old = " "
    while old != line:
        old = line
        line = re.sub( invariante, r'\g<1>\g<2>\g<3>\g<4>\g<5>', line)

    # remplazo de invariante garbagecollector
    line = re.sub( garbage, r'', line)

    # remplazo de transicion incompleta
    old = " "
    while old != line:
        old = line
        line = re.sub( resto, r'\g<1>\g<2>', line)

    # chequeo final
    if len(line) == 0:
        print("VACIO")
    else:
        print("RESTANTES: " + str(len(line)) )
        print( line )

def test_colectivo( file ):

    print("")
    print("Test colectivo:")

    # definicion de valores #######################################################
    invariante = r'1(.*?)2(.*?)3(.*?)5(.*?)6(.*?)4|7(.*?)8(.*?)9(.*?)B(.*?)C(.*?)A'
    remplazo_invariante = r'\g<1>\g<2>\g<3>\g<4>\g<5>\g<6>\g<7>\g<8>\g<9>\g<10>'

    resto = r'1(.*?)5(.*?)6|7(.*?)B(.*?)C'
    remplazo_resto = r'\g<1>\g<2>\g<3>\g<4>'

    garbage = r'D|E'
    remplazo_garbage = r''
    ###############################################################################

    # lectura de archivo ########
    line = file.readline()
    line = re.sub("\s", "", line)
    #############################

    # remplazo de transicion compartida
    line = re.sub( r'0', r'', line)
    ###################################

    # remplazo de invariante garbagecollector #####
    line = re.sub( garbage, remplazo_garbage, line)
    ###############################################

    # remplazo de invariante ################################
    old = " "
    while old != line:
        old = line
        line = re.sub( invariante, remplazo_invariante, line)
    #########################################################

    # remplazo de invariante incompleta ###########
    old = " "
    while old != line:
        old = line
        line = re.sub( resto, remplazo_resto, line)
    ###############################################

    # chequeo final ##########################
    if len(line) == 0:
        print("VACIO")
    else:
        print("RESTANTES: " + str(len(line)) )
        print( line )
    ##########################################

def test_mono(file):

    print("")
    print("Test mono:")

    # definicion de valores
    invariante = r'(.*?)0(.*?)1(.*?)2(.*?)3(.*?)5(.*?)6(.*?)4(.*?)'
    remplazo_invariante = r'\g<1>\g<2>\g<3>\g<4>\g<5>\g<6>\g<7>\g<8>'
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

def __main__():
    file = open("./src/files/transiciones.txt")
    fileA = open("./src/files/transicionesA.txt")
    fileB = open("./src/files/transicionesB.txt")

    #test_mono( file )
    test_colectivo( file )
    test_individual( file, 'a' )
    test_individual( file, 'b' )

    print("")

__main__()
