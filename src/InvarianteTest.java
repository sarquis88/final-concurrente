import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvarianteTest {

    static boolean checkInvariantes(String invariantesFile, int[][] invariantes)
    {
        String transiciones = getTransiciones(invariantesFile);

        Pattern p = Pattern.compile("-0-");
        Matcher m = p.matcher(transiciones);
        return m.matches();
    }

    static String getTransiciones(String invariantesFile)
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
        return transiciones;
    }
}
