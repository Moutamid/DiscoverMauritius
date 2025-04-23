package com.moutamid.sqlapp.activities.Organizer;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;

public class CustomDateInputWatcher implements TextWatcher {
    private final static String TAG = "Tag";
    private String currentString = "";
    private String separator = "/";
    private DateMaskingCallback dateMaskingCallback;
    private static final String INVALID_DATE_MSG = "Enter valid Date";
    private static final String INVALID_MONTH_MSG = "Enter valid Month";

    public void CustomDateInputWatcher(DateMaskingCallback dateMaskingCallback, String separator) {
        this.dateMaskingCallback = dateMaskingCallback;
        this.separator = separator;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            String dateString = s.toString();
            Log.e(TAG, "date current string "+currentString);
            if (!currentString.equalsIgnoreCase(dateString)) {
                currentString = dateString;
                String errorString = "";
                if (dateString.length() == 2 && before == 0) {
                    if (dateString.equalsIgnoreCase("00")) {
                        errorString = INVALID_MONTH_MSG;
                    } else if (Integer.parseInt(dateString) < 1 || Integer.parseInt(dateString) > 12) {
                        errorString = INVALID_MONTH_MSG;
                    }
                } else if (dateString.length() == 3 && before == 0) {
                    String day = dateString.substring(2, dateString.length()-1);
                    dateString = dateString.substring(0,2)+separator+day;
                } else if (dateString.length() == 4 && before == 0) {
                    String month = dateString.substring(3, 4);
                    if (Integer.parseInt(month) > 12) {
                        errorString = INVALID_DATE_MSG;
                    }
                } else if (dateString.length() == 6 && before == 0) {
                    String yearString = dateString.substring(3,5);
                    dateString = dateString.substring(0, 3)+ yearString+separator;
                    if (yearString.startsWith("0")) {
                        errorString = "Please start year with > 0";
                    }
                } else if (dateString.length() >= 7 && before == 0) {
                    Date currentDate = Calendar.getInstance().getTime();
                    Date enteredDate = new Date(dateString);
                    if (dateString.length() >= 10 && currentDate.getTime() < enteredDate.getTime()) {
                        errorString = "Future dates are not valid in dob";
                    }
                }
                Log.e(TAG, "dateString "+dateString +" error String "+errorString);
                if (errorString.isEmpty()) {
                    dateMaskingCallback.dateOfBirthValidation(true, dateString, errorString);
                } else {
                    dateMaskingCallback.dateOfBirthValidation(false, dateString, errorString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public interface DateMaskingCallback {
        void dateOfBirthValidation(final boolean isValid, final String dateOfBirth, final String error) throws Exception;
    }
}