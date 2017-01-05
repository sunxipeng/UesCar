package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/5.
 */
public class CommonUtil {
    public static String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

   /* //16进制转换为二进制
    public static String hexString2binaryString(String paramString) {
        String str1 = null;
        if ((paramString == null) || (paramString.length() % 2 != 0)) {

            for (int i = 0; i < paramString.length(); i++) {
                String str2 = "0000" + Integer.toBinaryString(Integer.parseInt(paramString.substring(i, i + 1), 16));
                str1 = str1 + str2.substring(-4 + str2.length());
            }
        }
        return str1;

    }*/

    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static boolean ismodifyDB() {
        return SharePreferenceUtils.getUserPermision().equals("1");
    }
}