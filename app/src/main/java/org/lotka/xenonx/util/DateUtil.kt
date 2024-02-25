package org.lotka.xenonx.util

import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

object DateUtil {

    // input format : 1970-01-01T00:00:00.000
    fun getRemainingDaysFromIso(dateString: String): String {

        return try {
            val date = LocalDate.parse(dateString.substring(0, 10))
            val today = LocalDate.now()
            val remainingDays = ChronoUnit.DAYS.between(today, date)
            remainingDays.toString()
        } catch (e:Exception) {
            ""
        }
    }

    fun minutesFromNowToMillis(minutes: Int): Long {
        return System.currentTimeMillis() + minutes * 60 * 1000L
    }

    fun getRemainingDaysFromUnixTimestamp(dateString: Long): String {

        return try {
            val currentTime = System.currentTimeMillis()
            val difference = dateString - currentTime
            val days = TimeUnit.MILLISECONDS.toDays(difference)

            days.toString()

        } catch (e:Exception) {
            ""
        }
    }
}