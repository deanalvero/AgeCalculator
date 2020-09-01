package com.lowbottgames.agecalculator.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.lowbottgames.agecalculator.R
import org.joda.time.LocalDate
import org.joda.time.LocalTime

class InputDialogFragment : DialogFragment() {

    private lateinit var listener: DPFOnDateSetListener
    private var name: String = ""
    private var year: Int = 2001
    private var month: Int = 0
    private var day: Int = 1
    private var hour: Int = 0
    private var minute: Int = 0

    companion object {
        const val KEY_ID = "KEY_ID"
        const val KEY_NAME = "KEY_NAME"
        const val KEY_YEAR = "KEY_YEAR"
        const val KEY_MONTH = "KEY_MONTH"
        const val KEY_DAY = "KEY_DAY"
        const val KEY_HOUR = "KEY_HOUR"
        const val KEY_MINUTE = "KEY_MINUTE"

        fun newInstance() : DialogFragment {
            return newInstance(0, "", 2001, 0, 1, 0, 0)
        }

        fun newInstance(id: Long, name: String, year: Int, month: Int, day: Int, hour: Int, minute: Int) : DialogFragment {
            val fragment = InputDialogFragment()

            val bundle = Bundle()
            bundle.putLong(KEY_ID, id)
            bundle.putString(KEY_NAME, name)
            bundle.putInt(KEY_YEAR, year)
            bundle.putInt(KEY_MONTH, month)
            bundle.putInt(KEY_DAY, day)
            bundle.putInt(KEY_HOUR, hour)
            bundle.putInt(KEY_MINUTE, minute)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as DPFOnDateSetListener
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(KEY_YEAR, year)
            putInt(KEY_MONTH, month)
            putInt(KEY_DAY, day)
            putInt(KEY_HOUR, hour)
            putInt(KEY_MINUTE, minute)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_input, null)
        val editTextName: EditText = view.findViewById(R.id.editText_name)
        val buttonBirthDate: Button = view.findViewById(R.id.button_birthdate)
        val buttonBirthTime: Button = view.findViewById(R.id.button_birthtime)

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                year = getInt(KEY_YEAR)
                month = getInt(KEY_MONTH)
                day = getInt(KEY_DAY)
                hour = getInt(KEY_HOUR)
                minute = getInt(KEY_MINUTE)
            }
        } else if (bundle != null) {
            with(bundle) {
                name = getString(KEY_NAME) ?: ""
                year = getInt(KEY_YEAR)
                month = getInt(KEY_MONTH)
                day = getInt(KEY_DAY)
                hour = getInt(KEY_HOUR)
                minute = getInt(KEY_MINUTE)
            }
        }

        updateUIBirthdate(buttonBirthDate, year, month, day)
        buttonBirthDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                context!!,
                {
                    _, year, month, dayOfMonth ->
                    this.year = year
                    this.month = month
                    this.day = dayOfMonth
                    updateUIBirthdate(buttonBirthDate, year, month, dayOfMonth)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        updateUIBirthtime(buttonBirthTime, hour, minute)
        buttonBirthTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context,
                {
                    _, hourOfDay, minute ->
                        this.hour = hourOfDay
                        this.minute = minute
                        updateUIBirthtime(buttonBirthTime, hourOfDay, minute)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        editTextName.setText(name)
        editTextName.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    editTextName.clearFocus()
                    true
                }
                else -> false
            }
        })

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
            .setPositiveButton(R.string.button_save, DialogInterface.OnClickListener() { _,_ ->
                listener.onInputSet(
                    editTextName.text.toString(),
                    this.year,
                    this.month,
                    this.day,
                    this.hour,
                    this.minute
                )
            })
            .setNegativeButton(android.R.string.cancel, null)

        return builder.create()
    }

    private fun updateUIBirthdate(button: Button, year: Int, month: Int, day: Int) {
        val birthdate = LocalDate(year, month + 1, day)
        button.text = birthdate.toString("dd MMMM YYYY")
    }

    private fun updateUIBirthtime(button: Button, hour: Int, minute: Int) {
        val birthtime = LocalTime(hour, minute)
        button.text = birthtime.toString("hh:mm aa")
    }

    interface DPFOnDateSetListener {
        fun onInputSet(name: String, year: Int, month: Int, day: Int, hour: Int, minute: Int)
    }

}