import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Test {
    public static void main(String ... args) {
        Date date = new GregorianCalendar(2019, Calendar.JUNE , 32).getTime();
        System.out.println(date);
    }
}
