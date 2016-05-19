package com.tt.reminder.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tt.reminder.R;
import com.tt.sharedbaseclass.fragment.EditTashFragmentBase;
import com.tt.sharedbaseclass.model.TaskBean;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditTaskFragment extends EditTashFragmentBase implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static EditTaskFragment mEditTaskFragment;
    private TaskBean mTaskBean;

    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static EditTaskFragment newInstance() {
        if (mEditTaskFragment == null) {
            mEditTaskFragment = new EditTaskFragment();
        }
        return mEditTaskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskBean = new TaskBean();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_edit_task, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeaderViewLeftArrow.setOnClickListener(this);
        mHeaderViewSaveTask.setOnClickListener(this);
        mDatePickerBtn.setOnClickListener(this);
        mTimePickerBtn.setOnClickListener(this);
        mClearDateBtn.setOnClickListener(this);
        mClearTimeBtn.setOnClickListener(this);
        mNewRepeatIntervalBtn.setOnClickListener(this);
        mNewGroupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_view_left_arrow:
                onBackPressed();
                break;
            case R.id.header_view_save_task:
                Log.i("TAG", "HHHHHHHH");
                break;
            case R.id.date_picker_dialog:
                datePickerDialog();
                break;
            case R.id.time_picker_dialog:
                timePickerDialog();
                break;
            case R.id.cross_to_clear_picked_date:
                clearPickedDate();
                break;
            case R.id.cross_to_clear_picked_time:
                clearPickedTime();
                break;
            case R.id.new_interval:
                break;
            case R.id.new_group:
                break;
            default:
                break;
        }
    }

    private void datePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR),
          calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void timePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true ).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        long currentTimeMill = System.currentTimeMillis();
        if (calendar.getTimeInMillis() <= currentTimeMill) {
            mAlarmDate.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            mAlarmDate.setTextColor(getResources().getColor(android.R.color.black));
        }
        mTaskBean.setYear(year);
        mTaskBean.setMonth(monthOfYear+1);
        mTaskBean.setDayOfMonth(dayOfMonth);
        updateEditedViewStatue(EDITED_VIEW.PICKED_DATE, mAlarmDate, mTaskBean.getPickedDate());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        updateEditedViewStatue(EDITED_VIEW.PICKED_TIME, mAlarmTime, hourOfDay + ":" + minute);
    }

    private void clearPickedDate() {
        updateEditedViewStatue(EDITED_VIEW.PICKED_DATE, mAlarmDate, "");
    }

    private void clearPickedTime() {
        updateEditedViewStatue(EDITED_VIEW.PICKED_TIME, mAlarmTime, "");
    }

    private void updateEditedViewStatue(EDITED_VIEW edited_view, EditText editView, String editViewStr) {
        mEditedView = edited_view;
        editView.setText(editViewStr);
    }

    protected boolean onBackPressed() {
        getFragmentManager().popBackStack();
        return true;
    }

}
