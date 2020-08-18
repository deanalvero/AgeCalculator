package com.lowbottgames.agecalculator.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lowbottgames.agecalculator.InfoActivity;
import com.lowbottgames.agecalculator.MainActivity;
import com.lowbottgames.agecalculator.R;

public class InputDialogFragment extends DialogFragment {

    public interface DPFOnDateSetListener {
        void onInputSet(String name, int year, int month, int day);
    }

    static final String KEY_ID = "KEY_ID";
    static final String KEY_NAME = "KEY_NAME";
    static final String KEY_YEAR = "KEY_YEAR";
    static final String KEY_MONTH = "KEY_MONTH";
    static final String KEY_DAY = "KEY_DAY";

    DPFOnDateSetListener listener;

    public static InputDialogFragment newInstance(){
        InputDialogFragment f = new InputDialogFragment();

        Bundle bundle = new Bundle();
        f.setArguments(bundle);

        return f;
    }

    public static InputDialogFragment newInstance(long ID, String name, int year, int month, int day){
        InputDialogFragment f = new InputDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(KEY_ID, ID);
        bundle.putString(KEY_NAME, name);
        bundle.putInt(KEY_YEAR, year);
        bundle.putInt(KEY_MONTH, month);
        bundle.putInt(KEY_DAY, day);

        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            if (activity instanceof MainActivity)
                listener = (MainActivity) activity;
            else if (activity instanceof InfoActivity)
                listener = (InfoActivity) activity;
        } catch (ClassCastException e){
            throw new ClassCastException();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_input, null);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.updateDate(bundle.getInt(KEY_YEAR, 2001),
                bundle.getInt(KEY_MONTH, 0),
                bundle.getInt(KEY_DAY, 1));

        final EditText editText = (EditText) view.findViewById(R.id.editText_name);
        editText.setText(bundle.getString(KEY_NAME));

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    editText.clearFocus();
//                    editText.getParent(
//                    editText.setFocusable(false);
//                    editText.setFocusableInTouchMode(true);
//                    datePicker.requestFocus();
                    return true;
                }
                return false;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null)
                            listener.onInputSet(editText.getText().toString(),
                                    datePicker.getYear(),
                                    datePicker.getMonth(),
                                    datePicker.getDayOfMonth());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();



    }


//        // Use the current date as the default date in the picker
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        // Create a new instance of DatePickerDialog and return it
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.AppCompatDialogStyle, this, year, month, day);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            datePickerDialog.getDatePicker().setMaxDate(c.getTime().getTime());
//        }
//        return datePickerDialog;
//    }
//
//    public void onDateSet(DatePicker view, int year, int month, int day) {
//        if (listener != null){
//            listener.onDateSet(year, month, day);
//        }
//
//
////        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
////            Calendar newDate = Calendar.getInstance();
////            newDate.set(year, month, day);
////
////            Calendar nowDate = Calendar.getInstance();
////
////            if (nowDate.after(newDate)) {
////                view.init(nowDate.get(Calendar.YEAR), nowDate.get(Calendar.MONTH), nowDate.get(Calendar.DAY_OF_MONTH));
////
////            }
////        }
//    }

}
