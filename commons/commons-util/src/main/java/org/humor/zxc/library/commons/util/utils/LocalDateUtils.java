package org.humor.zxc.library.commons.util.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateUtils
 * Date: 2020/8/16
 * Time: 4:58 下午
 *
 * @author xuzz
 */
public class LocalDateUtils {

    public static LocalDateTime parse(String dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static LocalDateTime ofTimeStamp(long timeStamp) {
        Instant instant = Instant.ofEpochSecond(timeStamp);
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static long parseTimeStamp(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return instant.getEpochSecond();
    }


}
