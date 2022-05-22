package greenlink.utils;

public class Check {
    public static Boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
        } catch (Exception err) {
            return false;
        }
        return true;
    }
}
