package demo.gateway;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZoneDateTimeUtils {
    public static void main(String[] args) {
        ZonedDateTime zonedDateTime1 = ZonedDateTime.now();
        System.out.println(zonedDateTime1);

        ZonedDateTime zonedDateTime2 = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        System.out.println(zonedDateTime2);
    }
}
