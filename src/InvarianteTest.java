import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InvarianteTest {

    static boolean checkInvariantes(String invariantesFile, int[][] invariantes)
    {
        int i;
        String transString;

        transString= getTransicionesString(invariantesFile);

        for( i = 0; i < invariantes.length; i++ )
            transString = replace( transString, getInvariantChar( invariantes[i] ));
        transString = transString.replaceAll("#", "");
        transString = transString.replaceAll(" ", "");

        return transString.length() == 0;

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
