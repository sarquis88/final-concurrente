import java.io.*;

public class InvarianteTest {

    static private String invariantesLog = "./src/T-InvariantesErr.txt";

    static boolean checkInvariantes(String invariantesFile, int[][] invariantes)
    {
        int i, lenOri, firstPosErr, lastPosErr;
        String transString;
        String log;

        transString = getTransicionesString(invariantesFile);
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

    static String getTransicionesString(String invariantesFile)
    {
        String transiciones = "";
        try
        {
            File file = new File(invariantesFile);
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

    static String replace(String text, char[] invariant)
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

    static char[] getInvariantChar( int[] invariant )
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
