package com.lowbottgames.agecalculator

import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomappbar.BottomAppBar
import com.lowbottgames.agecalculator.database.PersonModel
import com.lowbottgames.agecalculator.dialog.InputDialogFragment
import com.lowbottgames.agecalculator.util.DataHelper
import com.lowbottgames.agecalculator.viewmodel.AgeCalculatorViewModel
import com.lowbottgames.agecalculator.viewmodel.AgeCalculatorViewModelFactory
import org.joda.time.*

class InfoActivity : AppCompatActivity(), InputDialogFragment.DPFOnDateSetListener {

    companion object {
        const val KEY_ID = "KEY_ID"
    }

    private lateinit var textViewName: TextView
    private lateinit var textViewBirthdate: TextView
    private lateinit var textViewToday: TextView
    private lateinit var textViewAge: TextView
    private lateinit var textViewNextBirthday: TextView
    private lateinit var textViewAgeYears: TextView
    private lateinit var textViewAgeMonths: TextView
    private lateinit var textViewAgeWeeks: TextView
    private lateinit var textViewAgeDays: TextView
    private lateinit var textViewAgeHours: TextView
    private lateinit var textViewAgeMinutes: TextView

    private var id: Long = 0

    private lateinit var viewModel: AgeCalculatorViewModel
    private var personModel: PersonModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        viewModel = ViewModelProvider(this, AgeCalculatorViewModelFactory(application)).get(AgeCalculatorViewModel::class.java)

        val bottomAppBar: BottomAppBar = findViewById(R.id.bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            finish()
        }
        bottomAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_edit -> {
                    showInputFragment()
                    true
                }
                R.id.action_delete -> {
                    showDeleteDialog()
                    true
                }
                else -> false
            }
        }

        intent.extras?.let { bundle ->
            id = bundle.getLong(KEY_ID)

            viewModel.loadPersonById(id).observe(this, Observer {
                personModel = it
                refreshUIViews(personModel)
            })
        }

        textViewName = findViewById(R.id.textView_name)
        textViewBirthdate = findViewById(R.id.textView_birthdate)
        textViewToday = findViewById(R.id.textView_today)
        textViewAge = findViewById(R.id.textView_age)
        textViewNextBirthday = findViewById(R.id.textView_next_birthday)
        textViewAgeYears = findViewById(R.id.textView_age_years)
        textViewAgeMonths = findViewById(R.id.textView_age_months)
        textViewAgeWeeks = findViewById(R.id.textView_age_weeks)
        textViewAgeDays = findViewById(R.id.textView_age_days)
        textViewAgeHours = findViewById(R.id.textView_age_hours)
        textViewAgeMinutes = findViewById(R.id.textView_age_minutes)
    }

    private fun refreshUIViews(personModel: PersonModel?) {
        personModel?.let {
            textViewName.text = it.name

            val birthdate = LocalDateTime(it.year, it.month + 1, it.day, 0, 0)
            val now = LocalDateTime()

            textViewBirthdate.text = birthdate.toString("dd MMMM YYYY")
            textViewToday.text = now.toString("dd MMMM YYYY")

            val period = Period(birthdate, now, PeriodType.yearMonthDayTime())
            textViewAge.text = getString(R.string.format_age, period.years, period.months, period.days, period.hours, period.minutes)

            textViewNextBirthday.text = getString(R.string.format_next_birthday, DataHelper.daysUntilNextBirthday(birthdate, now))
            textViewAgeYears.text = "${Years.yearsBetween(birthdate, now).years}"
            textViewAgeMonths.text = "${Months.monthsBetween(birthdate, now).months}"
            textViewAgeWeeks.text = "${Weeks.weeksBetween(birthdate, now).weeks}"
            textViewAgeDays.text = "${Days.daysBetween(birthdate, now).days}"
            textViewAgeHours.text = "${Hours.hoursBetween(birthdate, now).hours}"
            textViewAgeMinutes.text = "${Minutes.minutesBetween(birthdate, now).minutes}"
        }
    }

    fun showDeleteDialog() {
        personModel?.let {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.format_delete_person, it.name))
                .setPositiveButton(R.string.button_delete, DialogInterface.OnClickListener() { _,_ ->
                    deletePerson()
                })
                .setNegativeButton(android.R.string.cancel, null)
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun showInputFragment() {
        personModel?.let {
            val fragment = InputDialogFragment.newInstance(id, it.name, it.year, it.month, it.day)
            fragment.show(supportFragmentManager, "InputDialogFragment")
        }
    }

    fun deletePerson() {
        personModel?.let {
            viewModel.delete(it)
            finish()
        }
    }

    override fun onInputSet(name: String, year: Int, month: Int, day: Int) {
        personModel?.let {
            it.name = name
            it.year = year
            it.month = month
            it.day = day

            viewModel.update(personModel!!)
        }
    }
}