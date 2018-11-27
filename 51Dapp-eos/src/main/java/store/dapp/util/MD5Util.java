package store.dapp.util;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * 字符串转成MD5:默认32位
     * @param plainStr
     * @param digit
     * @return
     */
    public static String string2MD5(String plainStr,int digit) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            //默认为32位加密
            String result = buf.toString();
            if(digit == 16){
                //16位的加密
                result = buf.toString().substring(8, 24);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 非标准验证
     * @param str
     * @return
     */
    public static String customMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //16位加密a->b d->m b->a 0->2
            String result = buf.toString().substring(8, 24).replaceFirst("a","b").replaceFirst("d","m").replaceFirst("b","a").replaceFirst("0","2");
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(System.currentTimeMillis());
        System.out.println(string2MD5("MAILA",32).substring(0,6).toUpperCase());
        System.out.println(string2MD5("MEINENGGOU",32).substring(0,6).toUpperCase());

        long time_stamp = System.currentTimeMillis();
        String key = "push_goods"+time_stamp+"IFENGA59FE37";
        String keyUTF8 = URLEncoder.encode(key, "UTF-8");
        System.out.println(time_stamp);
        System.out.println(keyUTF8);
        String enc = MD5Util.string2MD5(keyUTF8,32).toLowerCase();
        System.out.println(enc);
    }

}
