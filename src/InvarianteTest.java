import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InvarianteTest {

    static void checkInvariantes(String invariantesFile, int[][] invariantes)
    {
        String transString = getTransicionesString(invariantesFile);
        String aux = "";
        String regex = "";
        char parOpen = '(';
        char parClose = ')';
        int i;
        int digit;
        boolean end = false;

        for(i = 0; i < invariantes[0].length; i++)
        {
            if( invariantes[0][i] == -1 )
                break;
            else
            {
                if( invariantes[0][i] > 9 )
                    digit = invariantes[0][i] + 55;
                else
                    digit = invariantes[0][i] + 48;
                regex = regex.concat( String.valueOf( (char) digit ) );

                if( i < invariantes[0].length - 1)
                {
                    regex = regex.concat( String.valueOf( parOpen ) );
                    regex = regex.concat( ".*" );
                    regex = regex.concat( String.valueOf( parClose ) );
                }
            }
        }

        System.out.println( regex );
        System.out.println( transString );
        do
        {
            aux = transString.replaceAll( regex, "#$1#$2#$3#$4#$5#$6#");

            if( aux.equalsIgnoreCase( transString ) )
                end = true;
            else
            {
                transString = aux;
                System.out.println( transString );
            }
        } while( !end );


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

}
