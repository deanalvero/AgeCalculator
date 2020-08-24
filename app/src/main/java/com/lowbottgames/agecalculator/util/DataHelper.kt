package com.lowbottgames.agecalculator.util

import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.Period
import org.joda.time.PeriodType

object DataHelper {

    fun daysUntilNextBirthday(birthdate: LocalDate, now: LocalDate) : Int {
        val currBirthday = birthdate.withYear(now.year)
        val daysCurrent = Days.daysBetween(now, currBirthday).days
        val daysNext = Days.daysBetween(now, currBirthday.plusYears(1)).days

        return when {
            daysCurrent < 0 -> daysNext
            else -> daysCurrent
        }
    }

    fun age(birthdate: LocalDate) : String {
        val now = LocalDate()
        val period = Period(birthdate, now, PeriodType.yearMonthDay())
        return "${period.years}Y ${period.months}M ${period.days}D"
    }

}