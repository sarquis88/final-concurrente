import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InvarianteTest {

    static void checkInvariantes(String invariantesFile, int[][] invariantes)
    {
        String transString = getTransicionesString(invariantesFile);

        int i;
        for( i = 0; i < invariantes.length; i++ )
        {
            String regex = getRegex(invariantes[i]);
            String replacement = getReplacement( regex );
            transString = replace(regex, replacement, transString);
            transString = cleanString(transString, "#");
        }

        transString = cleanString(transString, " ");
        System.out.println( transString );

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

    static String getRegex( int[] invariante ) {
        int i, len, digit;
        String regex = "";
        char parOpen = '(';
        char parClose = ')';

        for (len = 0; len < invariante.length; len++) {
            if (invariante[len] == -1)
                break;
        }

        for (i = 0; i < len; i++)
        {
            if (invariante[i] > 9)
                digit = invariante[i] + 55;
            else
                digit = invariante[i] + 48;
            regex = regex.concat(String.valueOf((char) digit));

            if (i < len - 1) {
                regex = regex.concat(String.valueOf(parOpen));
                regex = regex.concat(".*");
                regex = regex.concat(String.valueOf(parClose));
            }
        }
        return regex;
    }

    static String getReplacement( String regex )
    {
        int i, c = 0;
        String replacement = "";

        for(i = 0; i < regex.length(); i++)
        {
            if( regex.charAt( i ) == '*' )
                c++;
        }
        for(i = 0; i < c; i++)
        {
            replacement = replacement.concat( "#$" + (i + 1) );
        }
        replacement = replacement.concat( "#" );

        return replacement;
    }

    static String replace(String regex, String replacement, String text)
    {
        String aux;
        boolean end = false;

        System.out.println( "\n" + regex );
        System.out.println( replacement + "\n" );
        System.out.println( text );

        do
        {
            aux = text.replaceAll( regex, replacement);

            if( aux.equalsIgnoreCase( text ) )
                end = true;
            else
            {
                text = aux;
                System.out.println( text );
            }
        } while( !end );

        return text;
    }

    static String cleanString(String text, String durt)
    {
        text = text.replaceAll(durt, "");
        return text;
    }
}
