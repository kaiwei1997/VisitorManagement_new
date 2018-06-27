package ch.makery.vm.util

import java.time.{LocalDateTime}
import java.time.format.{DateTimeFormatter, DateTimeParseException}

object DateTimeUtil {
  val DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm"
  val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)

  implicit class DateTimeFormater(val dateTime: LocalDateTime) {

    def asString: String = {
      if (dateTime == null) {
        return null;
      }
      return DATE_TIME_FORMATTER.format(dateTime);
    }
  }

  implicit class StringFormater(val data: String) {
    /**
      * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN}
      * to a {@link LocalDateTime} object.
      *
      * Returns null if the String could not be converted.
      *
      * @param dateTimeString the date and time as String
      * @return the date object or null if it could not be converted
      */
    def parseLocalDateTime: LocalDateTime = {
      try {
        LocalDateTime.parse(data, DATE_TIME_FORMATTER)
      } catch {
        case e: DateTimeParseException => null
      }
    }

    def isValid: Boolean = {
      data.parseLocalDateTime != null
    }
  }

}
