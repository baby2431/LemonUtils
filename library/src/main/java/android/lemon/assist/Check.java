package android.lemon.assist;

import java.util.Collection;
import java.util.Map;

/**
 * 辅助判断
 *
 * @author mty
 * @date 2013-6-10下午5:50:57
 */
public class Check {


    public static String ipMatches = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    public static String emailMatcher = "\"[\\\\w!#$%&'*+/=?^_`{|}~-]+(?:\\\\.[\\\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\\\w](?:[\\\\w-]*[\\\\w])?\\\\.)+[\\\\w](?:[\\\\w-]*[\\\\w])?\"";
    public static String portMather = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]{1}|6553[0-5])$";
    public static String chineseMather = "[\\u4e00-\\u9fa5]+";


    public static boolean isEmpty(CharSequence str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isEmpty(Object[] os) {
        return isNull(os) || os.length == 0;
    }

    public static boolean isEmpty(Collection<?> l) {
        return isNull(l) || l.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> m) {
        return isNull(m) || m.isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isChinese(String str) {
        return str.matches(chineseMather);
    }

    public static boolean isEmail(String str) {
        return str.matches(emailMatcher);
    }

    public static boolean isIDNum(String str) {
        return str.matches("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$");
    }

    public static boolean isPort(String str) {
        return str.matches(portMather);
    }

    public static boolean isIp(String str) {
        return str.matches(ipMatches);
    }

}
