package com.tt.sharedbaseclass.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.view.WheelView;

import java.util.Arrays;

/**
 * Created by zhengguo on 2016/5/18.
 */
public abstract class EditTaskFragmentBase extends FragmentBaseWithSharedHeaderView implements
         TextWatcher {

    protected EditText mTaskContent;
    protected TextView mAlarmDate;
    protected TextView mAlarmTime;
    protected TextView mTvRepeatInterval;
    protected TextView mGroupList;
    protected ImageView mDatePickerBtn;
    protected ImageView mTimePickerBtn;
    protected ImageView mClearTimeBtn;
    protected ImageView mClearDateBtn;
    protected TextView mSubtitleRepeat;
    protected LinearLayout mLinearLayoutRepeat;
    protected  ImageView mNewRepeatIntervalBtn;
    protected ImageView mNewGroupBtn;
    protected EDITED_VIEW mEditedView;
    protected EditText mEdtRepeatInterval;
    protected WheelView mRepeatUnitWheel;
    protected String [] mRepeatUnits;

    protected enum EDITED_VIEW {
        TASK_CONTENT, PICKED_DATE, PICKED_TIME, DEFAULT;
    }

    public EditTaskFragmentBase() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mEditedView = EDITED_VIEW.TASK_CONTENT;
        mRepeatUnits = getResources().getStringArray(R.array.repeat_interval_units);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        setListener();
    }

    private void initView(View view, Bundle savedInstanceState) {
        mHeaderViewMainMenu.setVisibility(View.GONE);
        mHeaderViewLeftArrow.setVisibility(View.VISIBLE);
        mHeaderViewTitle.setText(R.string.header_view_title_new_task);
        mHeaderViewVoiceInput.setVisibility(View.GONE);
        mHeaderViewSearchBtn.setVisibility(View.GONE);
        mHeaderViewAddNewTask.setVisibility(View.GONE);
        mHeaderViewSaveTask.setVisibility(View.VISIBLE);

        mTaskContent = (EditText) view.findViewById(R.id.edt_subtitle_what_to_do);
        mAlarmDate = (TextView) view.findViewById(R.id.edt_alarm_date);
        mAlarmTime = (TextView) view.findViewById(R.id.edt_alarm_time);
        mDatePickerBtn = (ImageView) view.findViewById(R.id.date_picker_dialog);
        mTimePickerBtn  = (ImageView) view.findViewById(R.id.time_picker_dialog);
        mClearDateBtn = (ImageView) view.findViewById(R.id.cross_to_clear_picked_date);
        mClearTimeBtn = (ImageView) view.findViewById(R.id.cross_to_clear_picked_time);
        mSubtitleRepeat = (TextView) view.findViewById(R.id.subtitle_repeat);
        mLinearLayoutRepeat = (LinearLayout) view.findViewById(R.id.linearlayout_repeat);
        mTvRepeatInterval = (TextView) view.findViewById(R.id.repeat_interval);
        mNewRepeatIntervalBtn = (ImageView) view.findViewById(R.id.new_interval);
        mGroupList = (TextView) view.findViewById(R.id.group_list);
        mNewGroupBtn = (ImageView) view.findViewById(R.id.new_group);
    }

    private void setListener() {
        mTaskContent.addTextChangedListener(this);
        mAlarmDate.addTextChangedListener(this);
        mAlarmTime.addTextChangedListener(this);
    }

    protected View mRepeatIntervalDialogView;
    protected int mSelectedWheelItemIndex = 2;
    protected AlertDialog.Builder showSetRepeatIntervalDialog() {
        AlertDialog.Builder builder = getDefaultAlertDialogBuilder(
          getResources().getString(R.string.set_repeat_interval_dialog_title_set_repeat), "");
        mRepeatIntervalDialogView = getActivity().getLayoutInflater().inflate(R.layout.shared_wheel_view, null, false);
        mEdtRepeatInterval = (EditText) mRepeatIntervalDialogView.findViewById(R.id.edt_repeat_interval);
        mEdtRepeatInterval.setHint(R.string.hint_enter_repeat_interval_here);
        mRepeatUnitWheel = (WheelView) mRepeatIntervalDialogView.findViewById(R.id.wheel_repeat_unit);
        mRepeatUnitWheel.setItems(Arrays.asList(mRepeatUnits));
        return  builder;
    }

    protected void showAddNewGroupDialog() {
        String title = getResources().getString(R.string.alert_dialog_title_new_group);
        AlertDialog.Builder builder = getDefaultAlertDialogBuilder(title, null);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.shared_dialog_edit_view, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_edit_view);
        builder.setView(view)
                .setNegativeButton(R.string.alert_dialog_negative_button_cancel, null)
                .setPositiveButton(R.string.alert_dialog_negative_button_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewGroup(editText);
                    }
                }).show();
    }

    protected abstract void addNewGroup(EditText editText);

    protected void updateEditedViewStatue(EDITED_VIEW edited_view, TextView editView,
                                        String editViewStr) {
        mEditedView = edited_view;
        editView.setText(editViewStr);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mEditedView == EDITED_VIEW.PICKED_DATE) {
            if (TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.VISIBLE) {
                mClearDateBtn.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.GONE) {
                mClearDateBtn.setVisibility(View.VISIBLE);
            }
        } else if (mEditedView == EDITED_VIEW.PICKED_TIME) {
            if (TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.VISIBLE) {
                mClearTimeBtn.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.GONE) {
                mClearTimeBtn.setVisibility(View.VISIBLE);
            }
        }
        if (TextUtils.isEmpty(mAlarmDate.getText().toString()) && TextUtils.isEmpty(mAlarmTime.getText().toString())) {
            showRepeatView(false);
        } else if (!TextUtils.isEmpty(mAlarmDate.getText().toString()) && !TextUtils.isEmpty(mAlarmTime.getText().toString())) {
            showRepeatView(true);
        }
        mEditedView = EDITED_VIEW.TASK_CONTENT;
    }

    protected void showRepeatView(boolean isShow) {
        if (isShow) {
            mSubtitleRepeat.setVisibility(View.VISIBLE);
            mLinearLayoutRepeat.setVisibility(View.VISIBLE);
        } else {
            mSubtitleRepeat.setVisibility(View.GONE);
            mLinearLayoutRepeat.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFragmentRegister != null) {
            mFragmentRegister.onFragmentRegistered(this);
        }
    }

}
