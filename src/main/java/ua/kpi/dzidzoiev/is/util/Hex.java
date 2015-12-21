package ua.kpi.dzidzoiev.is.util;

/**
 * Created by dzidzoiev on 12/22/15.
 */
public class Hex {
    public static String toHexString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
        {
            sb.append(String.format("%02X", b[i] & 0xFF));
        }
        return sb.toString();
    }
}
