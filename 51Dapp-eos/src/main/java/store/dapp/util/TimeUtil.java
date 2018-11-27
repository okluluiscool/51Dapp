package store.dapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Auther: liulu3
 * @Date: 2018/11/21 11:25
 * @Description:
 */
public class TimeUtil {
    public static boolean compare(String time1, String time2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date a = sdf.parse(time1);
        Date b = sdf.parse(time2);

        if (a.before(b)){
            return true;
        }else {
            return false;
        }
    }

    public static String getDaysBefore(int days){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        calendar.add(Calendar.HOUR, -8);
        String result = dateFormat.format(calendar.getTime());
        return result;
    }


    public static void main(String[] args) throws ParseException {
        System.out.println(compare("2018-11-20T03:20:51.500", "2018-11-21T03:20:51.500"));
        System.out.println(getDaysBefore(1));
    }
}
