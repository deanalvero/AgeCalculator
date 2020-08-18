package com.lowbottgames.agecalculator.util;

import android.content.Context;
import android.util.Log;

import com.lowbottgames.agecalculator.AgeCalculatorApplication;
import com.lowbottgames.agecalculator.PersonModel;
import com.lowbottgames.agecalculator.PersonModelDao;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.List;

public class DataHelper {

    private static final String TAG = DataHelper.class.getSimpleName();

//    public static String getTimeString(long time){
//        return DateFormat.format("hh:mm aa", new Date(time)).toString();
//    }
//
//    public static String getDateString(long time){
//        return DateFormat.format("MM/dd/yyyy", new Date(time)).toString();
//    }

    static PersonModelDao getPersonDao(Context c){
        return ((AgeCalculatorApplication) c.getApplicationContext()).getDaoSession().getPersonModelDao();
    }

    public static void inserOrUpdate(Context c, PersonModel personModel){
        getPersonDao(c).insertOrReplace(personModel);
    }


    public static void refresh(Context c, PersonModel personModel){
        getPersonDao(c).refresh(personModel);
    }

    public static void deletePersonModelByKey(Context c, Long ID){
        getPersonDao(c).deleteByKey(ID);
    }

    public static List<PersonModel> getAllPersonModel(Context c){
        return getPersonDao(c).loadAll();
    }

    public static PersonModel getPersonModel(Context c, long ID){
        return getPersonDao(c).load(ID);
    }

    public static int getDaysUntilNextBirthday(LocalDate birthdate, LocalDate now){
        LocalDate currBirthday = birthdate.withYear(now.getYear());

//        logLocalDate(birthdate, "birthdate");
//        logLocalDate(currBirthday, "currBirthday");
        int daysCurrent = Days.daysBetween(now, currBirthday).getDays();
        int daysNext = Days.daysBetween(now, currBirthday.plusYears(1)).getDays();

//        Log.e(TAG, "current = " + daysCurrent);
//        Log.e(TAG, "next = " + daysNext);

        if (daysCurrent < 0){
            return daysNext;
        } else {
            return daysCurrent;
        }
    }


    static void logLocalDate(LocalDate date, String text){
        Log.e(TAG, text + " = " + date.year().getAsString() + "/" + date.monthOfYear().getAsString() + "/" + date.dayOfMonth().getAsString());
    }


    public static String getAge(LocalDate birthdate) {
        LocalDate now = new LocalDate();
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        return period.getYears() + "Y " + period.getMonths() + "M " + period.getDays() + "D";
    }

}
