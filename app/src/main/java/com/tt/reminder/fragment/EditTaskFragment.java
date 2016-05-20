package com.tt.reminder.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.tt.reminder.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.EditTashFragmentBase;
import com.tt.sharedbaseclass.model.TaskBean;

import java.util.Calendar;

public class EditTaskFragment extends EditTashFragmentBase implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static EditTaskFragment mEditTaskFragment;
    private TaskBean mTaskBean;
    private TaskBean mTaskBeanFromParent;

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
        Bundle args = getArguments();
        if (args != null) {
            mTaskBeanFromParent = (TaskBean) args.getSerializable(Constant.EXTRA_TASK_BEAN);
            if (mTaskBeanFromParent == null) {
                mTaskBeanFromParent = new TaskBean();
            }
        } else {
            mTaskBeanFromParent = new TaskBean();
        }
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
        mTaskBean.setYear(year);
        mTaskBean.setMonth(monthOfYear + 1);
        mTaskBean.setDayOfMonth(dayOfMonth);
        updateEditedViewStatue(EDITED_VIEW.PICKED_DATE, mAlarmDate, mTaskBean.getPickedDate());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTaskBean.setHour(hourOfDay);
        mTaskBean.setMinuse(minute);
        updateEditedViewStatue(EDITED_VIEW.PICKED_TIME, mAlarmTime, mTaskBean.getPickedTime());
    }

    private void clearPickedDate() {
        mTaskBean.clearPickedDate();
        updateEditedViewStatue(EDITED_VIEW.PICKED_DATE, mAlarmDate, "");
    }

    private void clearPickedTime() {
        mTaskBean.clearPickedTime();
        updateEditedViewStatue(EDITED_VIEW.PICKED_TIME, mAlarmTime, "");
    }

    private void updateEditedViewStatue(EDITED_VIEW edited_view, EditText editView, String editViewStr) {
        mEditedView = edited_view;
        editView.setText(editViewStr);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        if (mTaskBean.isDeadline()) {
            mAlarmDate.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            mAlarmTime.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            mAlarmDate.setTextColor(getResources().getColor(android.R.color.black));
            mAlarmTime.setTextColor(getResources().getColor(android.R.color.black));
        }

    }

    @Override
    public void onBackPressed() {
        if (mTaskBean.equals(mTaskBeanFromParent)) {
            getFragmentManager().popBackStack();
        } else {
            new AlertDialog.Builder(getActivity())
              .setTitle(R.string.edit_task_fragment_alert_dialog_title)
              .setMessage(R.string.edit_task_fragment_alert_dialog_message)
              .setNegativeButton(R.string.edit_task_fragment_alert_dialog_calcel, null)
              .setPositiveButton(R.string.edit_task_fragment_alert_dialog_save, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                  }
              }).show();
        }
    }

}
