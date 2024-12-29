package demo.usul.utils;

import java.time.OffsetDateTime;

public class TimeUtil {

    private TimeUtil() {
    }

    public static boolean compareDayOfMonthStringWithOffsetDateTime(
            int dayOfMonth, OffsetDateTime target) {
//        ZoneId zone = target.toZonedDateTime().getZone();
//        OffsetDateTime represent = OffsetDateTime.of(
//                LocalDate.now().withDayOfMonth(Integer.parseInt(dayOfMonth)),
//                LocalTime.now(zone),
//                target.getOffset());
//        return represent.compareTo(target);

        int toCompare = target.getDayOfMonth();
        return toCompare < dayOfMonth;
    }
}
