package online.pok.defines;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class GlobalDefines {
   public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
   public static final int MAX_DAYS_BACK = 10;
   public static final String DOUBLE_TO_STRING_FORMAT_SINGLE = "%10.5f";

   private static final String DATE_TO_STRING_FORMAT = "%02d.%02d.%04d";

   public static String date2Str (LocalDate d) {
      return String.format(DATE_TO_STRING_FORMAT,  d.getDayOfMonth(), d.getMonthValue(), d.getYear());
   }
}
