package com.tt.reminder.fragment;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tt.reminder.R;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditTaskFragment extends FragmentBaseWithSharedHeaderView implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TextWatcher, Animator.AnimatorListener {

    private static EditTaskFragment mEditTaskFragment;
    private EditText mTaskContent;
    private EditText mAlarmDate;
    private EditText mAlarmTime;
    private Spinner mRepeatSpinner;
    private Spinner mGroupSpinner;
    private ImageView mDatePickerBtn;
    private ImageView mTimePickerBtn;
    private ImageView mClearTimeBtn;
    private ImageView mClearDateBtn;
    private  ImageView mNewRepeatIntervalBtn;
    private ImageView mNewGroupBtn;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_edit_task, container, false);
        mTaskContent = (EditText) contentView.findViewById(R.id.edt_subtitle_what_to_do);
        mAlarmDate = (EditText) contentView.findViewById(R.id.edt_alarm_date);
        mAlarmTime = (EditText) contentView.findViewById(R.id.edt_alarm_time);
        mDatePickerBtn = (ImageView) contentView.findViewById(R.id.date_picker_dialog);
        mTimePickerBtn  = (ImageView) contentView.findViewById(R.id.time_picker_dialog);
        mClearDateBtn = (ImageView) contentView.findViewById(R.id.cross_to_clear_picked_date);
        mClearTimeBtn = (ImageView) contentView.findViewById(R.id.cross_to_clear_picked_time);
        mRepeatSpinner = (Spinner) contentView.findViewById(R.id.spinner_interval_to_repeat);
        mNewRepeatIntervalBtn = (ImageView) contentView.findViewById(R.id.new_interval);
        mGroupSpinner = (Spinner) contentView.findViewById(R.id.spinner_group);
        mNewGroupBtn = (ImageView) contentView.findViewById(R.id.new_group);

        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeaderViewMainMenu.setVisibility(View.GONE);
        mHeaderViewLeftArrow.setVisibility(View.VISIBLE);
        mHeaderViewTitle.setText(R.string.header_view_title_new_task);
        mHeaderViewVoiceInput.setVisibility(View.GONE);
        mHeaderViewSearch.setVisibility(View.GONE);
        mHeaderViewAddNewTask.setVisibility(View.GONE);
        mHeaderViewSaveTask.setVisibility(View.VISIBLE);
        mHeaderViewLeftArrow.setOnClickListener(this);
        mDatePickerBtn.setOnClickListener(this);
        mTimePickerBtn.setOnClickListener(this);
        mClearDateBtn.setOnClickListener(this);
        mClearTimeBtn.setOnClickListener(this);
        mNewRepeatIntervalBtn.setOnClickListener(this);
        mNewGroupBtn.setOnClickListener(this);
        mAlarmDate.addTextChangedListener(this);
        mAlarmTime.addTextChangedListener(this);
    }

    public void onButtonPressed(Uri uri) {
        super.onButtonPressed(uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_view_left_arrow:
                onBack();
                break;
            case R.id.header_view_save_task:
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
            mAlarmDate.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        }
        mAlarmDate.setText(new SimpleDateFormat("dd MM, yyyy").format(new Date(calendar.getTimeInMillis())));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        mAlarmTime.setText(hourOfDay+":"+minute);
    }

    private void clearPickedDate() {
        mAlarmDate.setText("");
    }

    private void clearPickedTime() {
        mAlarmTime.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.VISIBLE) {
            mClearDateBtn.animate().alpha(0).translationX(mAlarmDate.getX()+mClearDateBtn.getWidth()).setDuration(100).setListener(this).start();
        } else if (!TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.GONE) {
            mClearDateBtn.animate().alpha(1).translationX(mAlarmDate.getX()).setDuration(100).setListener(this).start();
        }
        if (TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.VISIBLE) {
            mClearTimeBtn.animate().alpha(0).translationX(mAlarmTime.getX()+mClearTimeBtn.getWidth()).setDuration(100).setListener(this).start();
        } else if (!TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.GONE) {
            mClearTimeBtn.animate().alpha(1).translationX(mAlarmTime.getX()).setDuration(100).setListener(this).start();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void onBack() {
        getFragmentManager().popBackStack();
        //getActivity().onBackPressed();
    }

    @Override
    public void onAnimationStart(Animator animation) {

         if (!TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.GONE) {
            mClearDateBtn.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.GONE) {
            mClearTimeBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {

        if (TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.VISIBLE) {
            mClearDateBtn.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.VISIBLE) {
            mClearTimeBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


}
