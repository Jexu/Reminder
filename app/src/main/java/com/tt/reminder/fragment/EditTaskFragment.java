package com.tt.reminder.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tt.reminder.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.EditTashFragmentBase;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedbaseclass.service.RenderCallback;
import com.tt.sharedbaseclass.service.RenderService;

import java.util.Calendar;

public class EditTaskFragment extends EditTashFragmentBase implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        Spinner.OnItemSelectedListener {

    private static EditTaskFragment mEditTaskFragment;
    private TaskBean mTaskBean;
    private TaskBean mTaskBeanFromParent;
    private AddNewGroupCallBack mAddNewGroupCallBack;

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
        initServices();
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
        mAlarmDate.setOnClickListener(this);
        mTimePickerBtn.setOnClickListener(this);
        mAlarmTime.setOnClickListener(this);
        mClearDateBtn.setOnClickListener(this);
        mClearTimeBtn.setOnClickListener(this);
        mNewRepeatIntervalBtn.setOnClickListener(this);
        mNewGroupBtn.setOnClickListener(this);
        mRepeatSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void initServices() {
        super.initServices();
        mAddNewGroupCallBack = new AddNewGroupCallBack();
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION__ADD_NEW_GROUP.toString(),
                mAddNewGroupCallBack);
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
            case R.id.edt_alarm_date:
                datePickerDialog();
                break;
            case R.id.time_picker_dialog:
            case R.id.edt_alarm_time:
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
                showAddNewGroupDialog();
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
        new TimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true ).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mTaskBean.setYear(year);
        mTaskBean.setMonth(monthOfYear);
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

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        if (mTaskBean.isDeadline()) {
            mAlarmDate.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            mAlarmTime.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            mAlarmDate.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
            mAlarmTime.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
        }
        if (mEditedView == EDITED_VIEW.TASK_CONTENT) {
            if (TextUtils.isEmpty(mTaskContent.getText())) {
                mTaskBean.setTaskContent("");
            } else {
                mTaskBean.setTaskContent(mTaskContent.getText().toString());
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int repeatInterval = 0;
        switch (position) {
            case 1:
                repeatInterval = 1;
                break;
            case 2:
                repeatInterval = 7;
                break;
            case 3:
                repeatInterval = 30;
                break;
            case 4:
                repeatInterval = 365;
                break;
            default:
                repeatInterval = 0;
        }
        mTaskBean.setRepeatInterval(repeatInterval);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        if (mTaskBean.equals(mTaskBeanFromParent)) {
            finish();
        } else {
            String title = getResources().getString(R.string.edit_task_fragment_alert_dialog_title);
            String message = getResources().getString(R.string.edit_task_fragment_alert_dialog_message);
            SpannableString ssTitle = new SpannableString(title);
            SpannableString ssMessage = new SpannableString(message);
            ssTitle.setSpan(new ForegroundColorSpan(getResources()
                    .getColor(android.R.color.holo_green_dark)),
                    0,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssMessage.setSpan(new ForegroundColorSpan(getResources()
                    .getColor(android.R.color.holo_green_dark)),
                    0,message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            new AlertDialog.Builder(getActivity())
              .setTitle(ssTitle)
              .setMessage(ssMessage)
              .setNegativeButton(R.string.edit_task_fragment_alert_dialog_calcel, null)
              .setPositiveButton(R.string.edit_task_fragment_alert_dialog_save, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    finishWithResultCode(1, new Bundle());
                  }
              }).show();
        }
    }

    private static class AddNewGroupCallBack implements RenderCallback {

        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans,
                                          int requestCode, int resultCode) {

        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {
            if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP
                    && resultCode == Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS) {
                // TODO: 2016/5/23  update group list
                Log.i("Render", "add new group successfully");
            } else {
                Log.e("Render", "error request code or result code");
            }
        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {
            if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP
                    && resultCode == Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL) {
                // TODO: 2016/5/23 dismiss loading view
                Log.i("Render", "fail to add new group");
            } else {
                Log.e("Render", "error request code or result code");
            }
        }
    }

    private static class SaveTaskBeanCallback implements RenderCallback {

        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans,
                                          int requestCode, int resultCode) {

        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {

        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {

        }
    }
}
