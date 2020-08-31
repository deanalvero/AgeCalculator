package com.lowbottgames.agecalculator.util

import org.joda.time.*

object DataHelper {

    fun daysUntilNextBirthday(birthdate: LocalDateTime, now: LocalDateTime) : Int {
        val currBirthday = birthdate.withYear(now.year)
        val daysCurrent = Days.daysBetween(now, currBirthday).days
        val daysNext = Days.daysBetween(now, currBirthday.plusYears(1)).days

        return when {
            daysCurrent < 0 -> daysNext
            else -> daysCurrent
        }
    }

    fun age(birthdate: LocalDateTime) : String {
        val now = LocalDateTime()
        val period = Period(birthdate, now, PeriodType.yearMonthDayTime())
        return "${period.years}Y ${period.months}M ${period.days}D ${period.hours}h ${period.minutes}m"
    }

}