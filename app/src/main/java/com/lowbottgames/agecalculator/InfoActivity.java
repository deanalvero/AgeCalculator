package com.lowbottgames.agecalculator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lowbottgames.agecalculator.dialog.InputDialogFragment;
import com.lowbottgames.agecalculator.util.DataHelper;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Weeks;
import org.joda.time.Years;

public class InfoActivity extends AppCompatActivity implements
        InputDialogFragment.DPFOnDateSetListener{

    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_DAY = "KEY_DAY";
    public static final String KEY_MONTH = "KEY_MONTH";
    public static final String KEY_YEAR = "KEY_YEAR";
    public static final int REQUEST_CODE = 69;
//    private static final String TAG = InfoActivity.class.getSimpleName();

    TextView textView_name, textView_birthdate, textView_today, textView_age, textView_next_birthday, textView_age_years, textView_age_months, textView_age_weeks, textView_age_days, textView_age_hours, textView_age_minutes;
    Toolbar toolbar;

    long ID;
    String name;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_birthdate = (TextView) findViewById(R.id.textView_birthdate);
        textView_today = (TextView) findViewById(R.id.textView_today);
        textView_age = (TextView) findViewById(R.id.textView_age);
        textView_next_birthday = (TextView) findViewById(R.id.textView_next_birthday);
        textView_age_years = (TextView) findViewById(R.id.textView_age_years);
        textView_age_months = (TextView) findViewById(R.id.textView_age_months);
        textView_age_weeks = (TextView) findViewById(R.id.textView_age_weeks);
        textView_age_days = (TextView) findViewById(R.id.textView_age_days);
        textView_age_hours = (TextView) findViewById(R.id.textView_age_hours);
        textView_age_minutes = (TextView) findViewById(R.id.textView_age_minutes);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            ID = bundle.getLong(KEY_ID);
            name = bundle.getString(KEY_NAME);
            year = bundle.getInt(KEY_YEAR);
            month = bundle.getInt(KEY_MONTH);
            day = bundle.getInt(KEY_DAY);

            reloadUIViews();
        }
    }

    void reloadUIViews(){

        textView_name.setText(name);

        LocalDate birthdate = new LocalDate(year, month, day);
        LocalDate now = new LocalDate();

        textView_birthdate.setText(birthdate.toString("MMMM dd, YYYY"));
        textView_today.setText(now.toString("MMMM dd, YYYY"));

        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        textView_age.setText(getString(R.string.format_age, period.getYears(), period.getMonths(), period.getDays()));

        textView_next_birthday.setText(String.valueOf(DataHelper.getDaysUntilNextBirthday(birthdate, now)) + "D");
        textView_age_years.setText(String.valueOf(Years.yearsBetween(birthdate, now).getYears()));
        textView_age_months.setText(String.valueOf(Months.monthsBetween(birthdate, now).getMonths()));
        textView_age_weeks.setText(String.valueOf(Weeks.weeksBetween(birthdate, now).getWeeks()));
        textView_age_days.setText(String.valueOf(Days.daysBetween(birthdate, now).getDays()));
        textView_age_hours.setText(String.valueOf(Hours.hoursBetween(birthdate, now).getHours()));
        textView_age_minutes.setText(String.valueOf(Minutes.minutesBetween(birthdate, now).getMinutes()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_edit){
            showInputFragment();
            return true;
        } else if (id == R.id.action_delete){
            showDeleteDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.format_delete_person, name))
                .setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePerson();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deletePerson() {
        DataHelper.deletePersonModelByKey(this, ID);
//        finishActivity(REQUEST_CODE);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }


    void showInputFragment(){
        InputDialogFragment fragment = InputDialogFragment.newInstance(ID, name, year, month - 1, day);
        fragment.show(getSupportFragmentManager(), "InputDialogFragment");
    }

    @Override
    public void onInputSet(String name, int year, int month, int day) {
        PersonModel personModel = DataHelper.getPersonModel(this, ID);
        personModel.setName(name);
        personModel.setYear(year);
        personModel.setMonth(month);
        personModel.setDay(day);

        DataHelper.inserOrUpdate(this, personModel);

        this.name = name;
        this.year = year;
        this.month = month + 1;
        this.day = day;

        reloadUIViews();
    }
}
