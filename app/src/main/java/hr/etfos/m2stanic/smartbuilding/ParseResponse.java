package hr.etfos.m2stanic.smartbuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mato on 19.04.16..
 */
public class ParseResponse {

    public static String iStream_to_String(InputStream is1)
    {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is1), 4096);
        String line;
        StringBuilder sb =  new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
