package com.tt.sharedbaseclass.fragment;

import android.animation.Animator;
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
import android.widget.Spinner;
import android.widget.TextView;
import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.view.WheelView;

import java.util.Arrays;

/**
 * Created by zhengguo on 2016/5/18.
 */
public abstract class EditTaskFragmentBase extends FragmentBaseWithSharedHeaderView implements
         TextWatcher, Animator.AnimatorListener {

    protected EditText mTaskContent;
    protected TextView mAlarmDate;
    protected TextView mAlarmTime;
    protected TextView mTvRepeatInterval;
    protected Spinner mGroupSpinner;
    protected ImageView mDatePickerBtn;
    protected ImageView mTimePickerBtn;
    protected ImageView mClearTimeBtn;
    protected ImageView mClearDateBtn;
    protected  ImageView mNewRepeatIntervalBtn;
    protected ImageView mNewGroupBtn;
    protected EDITED_VIEW mEditedView;
    protected EditText mEdtRepeatInterval;
    protected WheelView mRepeatUnitWheel;
    protected String [] mRepeatUnits;
    protected int mFragmentType;

    protected enum EDITED_VIEW {
        TASK_CONTENT, PICKED_DATE, PICKED_TIME, DEFAULT;
    }

    public EditTaskFragmentBase() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mFragmentType = args.getInt(Constant.BundelExtra.EXTRA_FRAGMENT_TYPE);
        }
        mEditedView = EDITED_VIEW.DEFAULT;
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
        mHeaderViewSearch.setVisibility(View.GONE);
        mHeaderViewAddNewTask.setVisibility(View.GONE);
        mHeaderViewSaveTask.setVisibility(View.VISIBLE);

        mTaskContent = (EditText) view.findViewById(R.id.edt_subtitle_what_to_do);
        mAlarmDate = (TextView) view.findViewById(R.id.edt_alarm_date);
        mAlarmTime = (TextView) view.findViewById(R.id.edt_alarm_time);
        mDatePickerBtn = (ImageView) view.findViewById(R.id.date_picker_dialog);
        mTimePickerBtn  = (ImageView) view.findViewById(R.id.time_picker_dialog);
        mClearDateBtn = (ImageView) view.findViewById(R.id.cross_to_clear_picked_date);
        mClearTimeBtn = (ImageView) view.findViewById(R.id.cross_to_clear_picked_time);
        mTvRepeatInterval = (TextView) view.findViewById(R.id.repeat_interval);
        mNewRepeatIntervalBtn = (ImageView) view.findViewById(R.id.new_interval);
        mGroupSpinner = (Spinner) view.findViewById(R.id.spinner_group);
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
        mRepeatUnitWheel = (WheelView) mRepeatIntervalDialogView.findViewById(R.id.wheel_repeat_unit);
        if (mRepeatUnits == null) {
            mRepeatUnits = getResources().getStringArray(R.array.repeat_interval_units);
        }
        mRepeatUnitWheel.setItems(Arrays.asList(mRepeatUnits));
        return  builder;
    }

    protected void showAddNewGroupDialog() {
        String title = getResources().getString(R.string.alert_dialog_title_new_group);
        String message = getResources().getString(R.string.alert_dialog_message_add_new_group);
        AlertDialog.Builder builder = getDefaultAlertDialogBuilder(title, message);
        final EditText editText = new EditText(getActivity());
        editText.setSingleLine(true);
        builder.setView(editText)
                .setNegativeButton(R.string.edit_task_fragment_alert_dialog_discard, null)
                .setPositiveButton(R.string.edit_task_fragment_alert_dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewGroup(editText);
                    }
                }).show();
    }

    protected abstract void addNewGroup(EditText editText);

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
        if (mEditedView == EDITED_VIEW.PICKED_DATE) {
            if (TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.VISIBLE) {
                mClearDateBtn.setVisibility(View.GONE);
            }
        } else if(mEditedView == EDITED_VIEW.PICKED_TIME) {
            if (TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.VISIBLE) {
                mClearTimeBtn.setVisibility(View.GONE);
            }
        }
        mEditedView = EDITED_VIEW.TASK_CONTENT;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

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
                mClearDateBtn.animate().alpha(0).translationX(mAlarmDate.getX() + mClearDateBtn.getWidth()).setDuration(100).setListener(this).start();
            } else if (!TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.GONE) {
                mClearDateBtn.animate().alpha(1).translationX(mAlarmDate.getX()).setDuration(100).setListener(this).start();
            }
        } else if (mEditedView == EDITED_VIEW.PICKED_TIME) {
            if (TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.VISIBLE) {
                mClearTimeBtn.animate().alpha(0).translationX(mAlarmTime.getX() + mClearTimeBtn.getWidth()).setDuration(100).setListener(this).start();
            } else if (!TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.GONE) {
                mClearTimeBtn.animate().alpha(1).translationX(mAlarmTime.getX()).setDuration(100).setListener(this).start();
            }
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
