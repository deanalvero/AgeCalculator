package com.lowbottgames.agecalculator.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.lowbottgames.agecalculator.R

class InputDialogFragment : DialogFragment() {

    private lateinit var listener: DPFOnDateSetListener

    companion object {
        const val KEY_ID = "KEY_ID"
        const val KEY_NAME = "KEY_NAME"
        const val KEY_YEAR = "KEY_YEAR"
        const val KEY_MONTH = "KEY_MONTH"
        const val KEY_DAY = "KEY_DAY"

        fun newInstance() : DialogFragment {
            return newInstance(0, "", 2001, 0, 1)
        }

        fun newInstance(id: Long, name: String, year: Int, month: Int, day: Int) : DialogFragment {
            val fragment = InputDialogFragment()

            val bundle = Bundle()
            bundle.putLong(KEY_ID, id)
            bundle.putString(KEY_NAME, name)
            bundle.putInt(KEY_YEAR, year)
            bundle.putInt(KEY_MONTH, month)
            bundle.putInt(KEY_DAY, day)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as DPFOnDateSetListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_input, null)
        val datePicker: DatePicker = view.findViewById(R.id.datePicker)
        val editTextName: EditText = view.findViewById(R.id.editText_name)

        datePicker.updateDate(
            bundle?.getInt(KEY_YEAR) ?: 2001,
            bundle?.getInt(KEY_MONTH) ?: 0,
            bundle?.getInt(KEY_DAY) ?: 1
        )

        editTextName.setText(bundle?.getString(KEY_NAME) ?: "")
        editTextName.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    editTextName.clearFocus()
                    true
                }
                else -> false
            }
        });

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
            .setPositiveButton(R.string.button_save, DialogInterface.OnClickListener() { _,_ ->
                listener.onInputSet(
                    editTextName.text.toString(),
                    datePicker.year,
                    datePicker.month,
                    datePicker.dayOfMonth
                )
            })
            .setNegativeButton(android.R.string.cancel, null)

        return builder.create()
    }

    interface DPFOnDateSetListener {
        fun onInputSet(name: String, year: Int, month: Int, day: Int)
    }
}