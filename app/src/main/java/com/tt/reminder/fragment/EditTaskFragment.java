package com.tt.reminder.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tt.reminder.R;
import com.tt.reminder.notification.RenderAlarm;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.EditTaskFragmentBase;
import com.tt.sharedbaseclass.model.GroupBean;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedbaseclass.view.WheelView;

import java.util.Calendar;

public class EditTaskFragment extends EditTaskFragmentBase implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        Spinner.OnItemSelectedListener {

    private static EditTaskFragment mEditTaskFragment;
    private TaskBean mTaskBean;
    private TaskBean mTaskBeanFromParent;
    private RenderObjectBeans<GroupBean> mGroupsBean;
    private ArrayAdapter<String> mGroupsAdapter;
    private SaveTaskBeanCallback mSaveTaskBeanCallback;
    private AddNewGroupCallBack mAddNewGroupCallBack;
    private boolean mIsAddNewGroup = false;

    public EditTaskFragment() {
        // Required empty public constructor
        super();
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
        Bundle args = getArguments();
        if (mFragmentType == Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value()) {
            mTaskBeanFromParent = (TaskBean) args.getSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN);
            if (mTaskBeanFromParent == null) {
                mTaskBeanFromParent = new TaskBean();
            }
            mTaskBean.copy(mTaskBeanFromParent);
        } else if (mFragmentType == Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value()){
            mTaskBeanFromParent = new TaskBean();
        }
        if (args != null) {
            mGroupsBean = (RenderObjectBeans<GroupBean>) args.getSerializable(Constant.BundelExtra.EXTRAL_GROUPS_BEANS);
            mGroupsAdapter = new ArrayAdapter<>(getActivity(),
              R.layout.shared_spinner_simple_item,
              R.id.spinner_simple_item_view,
              mGroupsBean);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
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
        mTvRepeatInterval.setOnClickListener(this);
        mNewGroupBtn.setOnClickListener(this);
        mGroupSpinner.setOnItemSelectedListener(this);
        mGroupSpinner.setAdapter(mGroupsAdapter);
        initViewContent();
    }

    private void initViewContent() {
        if (mFragmentType == Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value()) {
            mHeaderViewTitle.setText(R.string.header_view_title_edit_task);
            mTaskContent.setText(mTaskBeanFromParent.getTaskContent());
            if (!mTaskBeanFromParent.isClearedPickedDate() && !mTaskBeanFromParent.isClearedPickedTime()) {
                updateEditedViewStatue(EDITED_VIEW.PICKED_DATE, mAlarmDate, mTaskBeanFromParent.getPickedDate(true));
                updateEditedViewStatue(EDITED_VIEW.PICKED_TIME, mAlarmTime, mTaskBeanFromParent.getPickedTime(true));
            }
            if (mTaskBeanFromParent.getRepeatInterval() == TaskBean.DEFAULT_VALUE_OF_INTERVAL) {
                mTvRepeatInterval.setText(mRepeatUnits[0]);
            } else {
                mTvRepeatInterval.setText(getResources().getString(R.string.every_interval_unit
                        , mTaskBeanFromParent.getRepeatInterval()
                        , mRepeatUnits[mTaskBeanFromParent.getRepeatUnit() - 1]));
            }
            mGroupSpinner.setSelection(mGroupsBean.indexOf(new GroupBean(mTaskBeanFromParent.getGroup())));
        } else if (mFragmentType == Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value()) {
            mHeaderViewTitle.setText(R.string.header_view_title_new_task);
        }
    }

    @Override
    public void initServices() {
        super.initServices();
        mAddNewGroupCallBack = new AddNewGroupCallBack(this);
        mSaveTaskBeanCallback = new SaveTaskBeanCallback(this);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION__ADD_NEW_GROUP.toString(),
          mAddNewGroupCallBack);
        if (mFragmentType == Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value()) {
            mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_ADD_NEW_TASK.toString(),
                    mSaveTaskBeanCallback);
        } else if(mFragmentType == Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value()) {
            mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.toString(),
                    mSaveTaskBeanCallback);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_view_left_arrow:
                onBackPressed();
                break;
            case R.id.header_view_save_task:
                saveTask();
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
            case R.id.repeat_interval:
                showSetRepeatIntervalDialog();
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

    protected AlertDialog.Builder showSetRepeatIntervalDialog() {
        AlertDialog.Builder builder = super.showSetRepeatIntervalDialog();
        mRepeatUnitWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                mSelectedWheelItemIndex = selectedIndex;
                Log.i("Render", selectedIndex + "");
                Log.i("Render", item + "");
            }
        });
        mRepeatUnitWheel.setSeletion(2);
        builder.setView(mRepeatIntervalDialogView).setNegativeButton("cancel", null)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mSelectedWheelItemIndex != Constant.REPEAT_UNIT.NO_REPEAT.value()
                            && !TextUtils.isEmpty(mEdtRepeatInterval.getText().toString().trim())) {
                            mTaskBean.setRepeatInterval(Integer.parseInt(mEdtRepeatInterval.getText().toString()));
                            mTaskBean.setRepeatUnit(mSelectedWheelItemIndex);
                            // TODO: 2016/5/23 set textview
                            mTvRepeatInterval.setText(getResources().getString(R.string.every_interval_unit,
                                    mTaskBean.getRepeatInterval(),mRepeatUnits[mSelectedWheelItemIndex - 1]));
                        }else if(mSelectedWheelItemIndex != Constant.REPEAT_UNIT.NO_REPEAT.value()
                                && TextUtils.isEmpty(mEdtRepeatInterval.getText().toString().trim())) {
                            Toast.makeText(getActivity(), "Please input repeat interval", Toast.LENGTH_SHORT).show();
                        } else if (mSelectedWheelItemIndex == Constant.REPEAT_UNIT.NO_REPEAT.value()) {
                            mTaskBean.setRepeatInterval(TaskBean.DEFAULT_VALUE_OF_INTERVAL);
                            mTaskBean.setRepeatUnit(mSelectedWheelItemIndex);
                            mTvRepeatInterval.setText(mRepeatUnits[mSelectedWheelItemIndex - 1]);
                        }
                    }
                }).show();
        return builder;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mTaskBean.setYear(year);
        mTaskBean.setMonth(monthOfYear);
        mTaskBean.setDayOfMonth(dayOfMonth);
        updateEditedViewStatue(EDITED_VIEW.PICKED_DATE, mAlarmDate, mTaskBean.getPickedDate(true));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTaskBean.setHour(hourOfDay);
        mTaskBean.setMinuse(minute);
        updateEditedViewStatue(EDITED_VIEW.PICKED_TIME, mAlarmTime, mTaskBean.getPickedTime(true));
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
            mAlarmDate.setTextColor(getResources().getColor(android.R.color.black));
            mAlarmTime.setTextColor(getResources().getColor(android.R.color.black));
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
        mTaskBean.setGroup(mGroupsBean.get(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onBackPressed() {
        if (mTaskBean.equals(mTaskBeanFromParent)) {
            finish();
        } else {
            String title = getResources().getString(R.string.alert_dialog_title_are_you_sure);
            String message = getResources().getString(R.string.edit_task_fragment_alert_dialog_message);
            final AlertDialog.Builder builder = getDefaultAlertDialogBuilder(title, message);
            builder.setNegativeButton(R.string.edit_task_fragment_alert_dialog_discard,
              new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.BundelExtra.EXTRA_IS_ADD_NEW_GROUP, mIsAddNewGroup);
                    finishWithResultCode(Constant.BundelExtra.FINISH_RESULT_CODE_DEFAULT, bundle);
                }
            })
              .setPositiveButton(R.string.edit_task_fragment_alert_dialog_save,
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    saveTask();
                  }
              }).show();
        }
        return true;
    }

    @Override
    public void fetchData() {

    }

    private void saveTask() {
        if (mTaskBean.checkTaskStatus() == Constant.TASK_BEAN_STATUS.TASK_CONTENT_NULL) {
            Toast.makeText(getActivity(), "please input task content", Toast.LENGTH_SHORT).show();
            return;
        } else if (mTaskBean.checkTaskStatus() == Constant.TASK_BEAN_STATUS.DATE_NOT_SET) {
            Toast.makeText(getActivity(), "please set alarm date", Toast.LENGTH_SHORT).show();
            return;
        } else if (mTaskBean.checkTaskStatus() == Constant.TASK_BEAN_STATUS.TIME_NOT_SET) {
            Toast.makeText(getActivity(), "please set alarm time", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mTaskBean.isClearedPickedDate() && mTaskBean.isClearedPickedTime()) {
            mTaskBean.setRepeatInterval(TaskBean.DEFAULT_VALUE_OF_INTERVAL);
            mTaskBean.setRepeatUnit(Constant.REPEAT_UNIT.NO_REPEAT.value());
        }
        if (mFragmentType == Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value()) {
            mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_ADD_NEW_TASK.value()
              ,Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
              ,null
              ,mTaskBean
              ,null
              ,Constant.RenderServiceHelper.REQUEST_CODE_INSERT_TASK_BEAN);
        } else if (mFragmentType == Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value()) {
            // TODO: 5/26/16 update origin task
            if (mTaskBean.equals(mTaskBeanFromParent)) {
                finish();
            } else {
                mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.value()
                , Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                , mTaskBeanFromParent
                , mTaskBean
                , null
                , Constant.RenderServiceHelper.REQUEST_CODE_UPDATE_TASK_BEAN);
            }
        }
    }

    private void onSaveTaskSuccess(long row, int requestCode, int resultCode) {
        Bundle bundle = new Bundle();
        // TODO: 5/26/16 update bundle and finishedCode
        if (mFragmentType == Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value()
                && requestCode == Constant.RenderServiceHelper.REQUEST_CODE_INSERT_TASK_BEAN
                && resultCode == Constant.RenderServiceHelper.RESULT_CODE_INSERT_TASK_SUCCESS) {
            Log.i("Render", "add new task successfully");
            bundle.putSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN, mTaskBean);
        } else if (mFragmentType == Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value()
                && requestCode == Constant.RenderServiceHelper.REQUEST_CODE_UPDATE_TASK_BEAN
                && resultCode == Constant.RenderServiceHelper.RESULT_CODE_UPDATE_TASK_SUCCESS) {
            Log.i("Render", "update task successfully");
            bundle.putSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN, mTaskBean);
        }
        bundle.putBoolean(Constant.BundelExtra.EXTRA_IS_ADD_NEW_GROUP, mIsAddNewGroup);
        setAlarm();
        finishWithResultCode(Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS, bundle);
    }

    private void setAlarm() {
        if (mFragmentType == Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value()) {
            if (!mTaskBean.isClearedPickedDate()) {
                //create
                RenderAlarm.createAlarm(getActivity(), mTaskBean);
            }
        } else if (mFragmentType == Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value()) {
            if (mTaskBean.isFinished() == TaskBean.VALUE_FINISHED) {
                return;
            }
            if (!mTaskBeanFromParent.isClearedPickedDate()
              && mTaskBean.isClearedPickedDate()) {
                //cancel
                RenderAlarm.removeAlarm(getActivity(), mTaskBean);
            } else if (mTaskBeanFromParent.isClearedPickedDate()
              && !mTaskBean.isClearedPickedDate()) {
                //create
                RenderAlarm.createAlarm(getActivity(), mTaskBean);
            } else if ((mTaskBeanFromParent.getTimeInMillis() != mTaskBeanFromParent.getTimeInMillis()
              || mTaskBeanFromParent.getRepeatIntervalTimeInMillis() != mTaskBean.getRepeatIntervalTimeInMillis())
              && !mTaskBeanFromParent.isClearedPickedDate()
              && !mTaskBean.isClearedPickedDate()) {
                //update
                RenderAlarm.updateAlarm(getActivity(), mTaskBean);
            }
        }
        Log.i("Render", "Alarm set");
    }

    @Override
    protected void addNewGroup(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
            GroupBean groupBean = new GroupBean();
            groupBean.setGroup(editText.getText().toString());
            mTaskBean.setGroup(editText.getText().toString());
            mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION__ADD_NEW_GROUP.value(),
              Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP, null, groupBean, null,
              Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP);
            // TODO: 2016/5/23 show loading view
        } else {
            Toast.makeText(getActivity(), com.tt.sharedbaseclass.R.string.edit_task_add_new_group_please_input_new_group_name,
              Toast.LENGTH_SHORT).show();
        }
    }

    private void onAddNewGroupSuccess(long row, int requestCode, int resultCode) {
        if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP
          && resultCode == Constant.RenderServiceHelper.RESULT_CODE_INSERT_GROUP_SUCCESS) {
            // TODO: 2016/5/23  update group list
            Log.i("Render", "add new group successfully");
            mGroupsBean.add(new GroupBean(mTaskBean.getGroup()));
            mGroupsAdapter.notifyDataSetChanged();
            mGroupSpinner.setSelection(mGroupsBean.indexOf(new GroupBean(mTaskBean.getGroup())));
            mIsAddNewGroup = true;
        } else {
            Log.e("Render", "error request code or result code");
        }
    }

    private static class AddNewGroupCallBack extends RenderCallback {

        EditTaskFragment mContext;
        private AddNewGroupCallBack(EditTaskFragment context) {
            mContext = context;
        }
        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans,
                                          int requestCode, int resultCode) {

        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {
            mContext.onAddNewGroupSuccess(row, requestCode, resultCode);
        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {
            if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP
                    && resultCode == Constant.RenderServiceHelper.RESULT_CODE_FAIL) {
                // TODO: 2016/5/23 dismiss loading view
                Log.i("Render", "fail to add new group");
            } else {
                Log.e("Render", "error request code or result code");
            }
        }
    }

    private static class SaveTaskBeanCallback extends RenderCallback {
        EditTaskFragment mContext;

        private SaveTaskBeanCallback(EditTaskFragment context) {
            mContext = context;
        }

        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans,
                                          int requestCode, int resultCode) {

        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {
            mContext.onSaveTaskSuccess(row, requestCode, resultCode);
        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {

        }
    }

}
