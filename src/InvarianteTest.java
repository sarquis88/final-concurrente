import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InvarianteTest {

    static void checkInvariantes(String invariantesFile, int[][] invariantes)
    {
        String transString = getTransicionesString(invariantesFile);
        int[] transInt = getTransicionesInt(transString);

        for( int i = 0; i < 4; i++)
        {
            while( isInvariant( transInt, invariantes[i] ))
                transInt = removeOneInvariant(transInt, invariantes[i]);
        }

        for (int value : transInt) System.out.println(value);
    }

    static int[] removeOneInvariant( int[] trans, int[] invariant )
    {
        int[] posiciones = new int[ invariant.length ];
        int[] acotado;
        int c = 0;
        int i;

        for( i = 0; i < trans.length; i++ )
        {
            if( trans[i] == invariant[c] )
            {
                posiciones[c] = i;
                c++;
                if( c == invariant.length )
                    break;
            }
        }

        for( i = 0; i < posiciones.length; i++)
            trans[ posiciones[i] ] = 99;

        for( i = trans.length - 1; i >= 0 ; i-- )
        {
            if( trans[i] == 99 )
            {
                int j;
                for( j = i; j < trans.length; j++ )
                {
                    if( j == trans.length - 1 )
                        trans[j] = 99;
                    else
                        trans[j] = trans[j + 1];
                }
            }
        }

        for( i = 0; i < trans.length; i++ )
        {
            if( trans[i] == 99 )
                break;
        }
        acotado = new int[ i ];

        for( i = 0; i < acotado.length; i++ )
        {
            acotado[i] = trans[i];
        }

        return acotado;
    }

    static boolean isInvariant(int[] trans, int[] invariant)
    {
        int len;
        int c = 0;
        int i;

        for( len = 0; len < invariant.length; len++ )
        {
            if( invariant[len] == -1 )
                break;
        }

        for( i = 0; i < trans.length; i++ )
        {
            if( trans[i] == invariant[c] )
            {
                c++;
                if( c == len )
                {
                    return true;
                }
            }
        }
        return false;
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

    static int[] getTransicionesInt( String transiciones)
    {
        int i, ii, len, a , uni, dec;
        int[] trans;

        len = 0;
        for( i = 0; i < transiciones.length(); i++ )
        {
            if( transiciones.charAt(i) == ' ' )
                len++;
        }

        trans = new int[ len ];
        ii = 0;
        a = 0;
        uni = 0;
        dec = 0;
        for( i = 0; i < transiciones.length(); i++ )
        {
            if( transiciones.charAt(i) == ' ' )
            {
                trans[ii] = uni + 10 * dec;
                ii++;
                a = 0;
            }
            else if( a == 0)
            {
                uni = transiciones.charAt(i) - 48;
                a = 1;
            }
            else
            {
                dec = uni;
                uni = transiciones.charAt(i) - 48;
            }
        }
        return trans;
    }
}
