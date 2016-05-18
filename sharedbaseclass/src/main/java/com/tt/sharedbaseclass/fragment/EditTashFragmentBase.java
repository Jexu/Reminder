package com.tt.sharedbaseclass.fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.tt.sharedbaseclass.R;

/**
 * Created by Administrator on 2016/5/18.
 */
public class EditTashFragmentBase extends FragmentBaseWithSharedHeaderView implements
         TextWatcher, Animator.AnimatorListener {

    protected EditText mTaskContent;
    protected EditText mAlarmDate;
    protected EditText mAlarmTime;
    protected Spinner mRepeatSpinner;
    protected Spinner mGroupSpinner;
    protected ImageView mDatePickerBtn;
    protected ImageView mTimePickerBtn;
    protected ImageView mClearTimeBtn;
    protected ImageView mClearDateBtn;
    protected  ImageView mNewRepeatIntervalBtn;
    protected ImageView mNewGroupBtn;
    protected EDITED_VIEW mEditedView;

    protected enum EDITED_VIEW {
        TASK_CONTENT, PICKED_DATE, PICKED_TIME, DEFAULT;
    }

    public EditTashFragmentBase() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditedView = EDITED_VIEW.DEFAULT;
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
        mAlarmDate = (EditText) view.findViewById(R.id.edt_alarm_date);
        mAlarmTime = (EditText) view.findViewById(R.id.edt_alarm_time);
        mDatePickerBtn = (ImageView) view.findViewById(R.id.date_picker_dialog);
        mTimePickerBtn  = (ImageView) view.findViewById(R.id.time_picker_dialog);
        mClearDateBtn = (ImageView) view.findViewById(R.id.cross_to_clear_picked_date);
        mClearTimeBtn = (ImageView) view.findViewById(R.id.cross_to_clear_picked_time);
        mRepeatSpinner = (Spinner) view.findViewById(R.id.spinner_interval_to_repeat);
        mNewRepeatIntervalBtn = (ImageView) view.findViewById(R.id.new_interval);
        mGroupSpinner = (Spinner) view.findViewById(R.id.spinner_group);
        mNewGroupBtn = (ImageView) view.findViewById(R.id.new_group);
    }

    private void setListener() {
        mAlarmDate.addTextChangedListener(this);
        mAlarmTime.addTextChangedListener(this);
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
        if (mEditedView == EDITED_VIEW.PICKED_DATE) {
            if (TextUtils.isEmpty(mAlarmDate.getText().toString()) && mClearDateBtn.getVisibility() == View.VISIBLE) {
                mClearDateBtn.setVisibility(View.GONE);
            }
        } else if(mEditedView == EDITED_VIEW.PICKED_TIME) {
            if (TextUtils.isEmpty(mAlarmTime.getText().toString()) && mClearTimeBtn.getVisibility() == View.VISIBLE) {
                mClearTimeBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

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
}
