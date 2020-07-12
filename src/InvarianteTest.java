import java.io.*;

public class InvarianteTest {

    private String transicionesFile;
    private String invariantesLog;

    /**
     * Constructor de clase
     * @param transicionesFile path del archivo con las transiciones
     */
    public InvarianteTest(String transicionesFile)
    {
        this.transicionesFile = transicionesFile;
        this.invariantesLog = "./src/T-InvariantesErr.txt";
    }

    /**
     * Analisis de T-Invariantes
     * @param invariantes matriz de invariantes
     * @return true o false dependiendo si el analisis es correcto o incorrecto, respectivamente
     */
    public boolean checkInvariantes(int[][] invariantes)
    {
        int i, lenOri, firstPosErr, lastPosErr;
        String transString;
        String log;

        transString = getTransicionesString();
        lenOri = transString.length() / 2;
        firstPosErr = 9999;
        lastPosErr = 9999;

        for( i = 0; i < invariantes.length; i++ )
            transString = replace( transString, getInvariantChar( invariantes[i] ));

        for( i = 0; i < transString.length(); i++ )
        {
            if( transString.charAt(i) != '#' && transString.charAt(i) != ' ' )
            {
                firstPosErr = i / 2;
                break;
            }
        }
        for( i = transString.length() - 1; i >= 0; i-- )
        {
            if( transString.charAt(i) != '#' && transString.charAt(i) != ' ' )
            {
                lastPosErr = i / 2;
                break;
            }
        }

        transString = transString.replaceAll("#", "");
        transString = transString.replaceAll(" ", "");

        if( transString.length() == 0 )
            return true;
        else
        {
            log = "";
            log = log.concat( "Transiciones restantes:\t\t\t\t\t");
            for( i = 0; i < transString.length(); i++ )
                log = log.concat( transString.charAt(i) + " ");
            log = log.concat("\n\nCantidad de transiciones totales:\t\t" + lenOri + "\n");
            log = log.concat("Cantidad de transiciones restantes:\t\t" + transString.length() + "\n");
            log = log.concat("\nPrimera posicion restante:\t\t\t\t" + firstPosErr + "\n");
            log = log.concat("Ultima posicion restante:\t\t\t\t" + lastPosErr);


            try {
                File TInvariantesFile = new File(invariantesLog);

                TInvariantesFile.delete();
                TInvariantesFile.createNewFile();

                BufferedWriter bufferedWriter;
                bufferedWriter = new BufferedWriter(new FileWriter(TInvariantesFile, false));
                bufferedWriter.write( log );
                bufferedWriter.flush();
                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    /**
     * Convierte el log del archivo en un string
     * @return string que contiene las transiciones del log
     */
    public String getTransicionesString()
    {
        String transiciones = "";
        try
        {
            File file = new File(transicionesFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            transiciones = br.readLine();
            fr.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        transiciones = transiciones.concat(" ");
        return transiciones;
    }

    /**
     * Matchea las invariantes en el string que contiene las transiciones. En caso de match, reemplaza los
     * digitos de la invariante con el simbolo '#'
     * @param text texto que contiene las transiciones
     * @param invariant char array que representa una invariante
     * @return el texto original con la invariante remplazada. Si no hay match, el string es el mismo.
     */
    public String replace(String text, char[] invariant)
    {
        int i, j, k, c;
        int[] posiciones;

        c = 0;
        posiciones = new int[ invariant.length ];

        for( i = text.length() - 1 ; i >= 0; i-- )
        {
            if( text.charAt( i ) == invariant[c] )
            {
                posiciones[c] = i;
                c++;
                for( j = i + 1; j < text.length(); j++ )
                {
                    if( text.charAt( j ) == invariant[c] )
                    {
                        posiciones[c] = j;
                        c++;
                        if( c == invariant.length )
                        {
                            for( k = 0; k < posiciones.length; k++ )
                            {
                                if( posiciones[k] > 0 )
                                    text =  text.substring(0, posiciones[k] ) + '#' + text.substring( posiciones[k] + 1);
                                else if( posiciones[k] == 0)
                                    text =  text.substring(0, posiciones[k] ) + '#' + text.substring( posiciones[k] + 1);
                            }
                            break;
                        }
                    }
                }
                c = 0;
            }
        }
        return text;
    }

    /**
     * Convierte int array en char array
     * @param invariant int array con una invariante
     * @return char array con la invariante ingresada
     */
    public char[] getInvariantChar( int[] invariant )
    {
        char[] invariantChar;
        int i;

        for( i = 0; i < invariant.length; i++ )
        {
            if( invariant[i] == -1 )
                break;
        }

        invariantChar = new char[ i ];

        for( i = 0; i < invariantChar.length; i++ )
        {
            if( invariant[i] <= 9 )
                invariantChar[i] = (char) ( invariant[i] + 48 );
            else
                invariantChar[i] = (char) ( invariant[i] + 55 );
        }

        return invariantChar;
    }

}
