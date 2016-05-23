package com.tt.sharedbaseclass.fragment;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedbaseclass.service.RenderService;

/**
 * Created by Administrator on 2016/5/18.
 */
public abstract class EditTashFragmentBase extends FragmentBaseWithSharedHeaderView implements
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
    protected RenderService mRenderService;

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
        mRepeatSpinner.setAdapter(new ArrayAdapter<>(getActivity()
          , R.layout.shared_simple_list_item
          ,R.id.simple_list_item_view
          , getResources().getStringArray(R.array.repeat_interval_spinner_list)));
        mNewRepeatIntervalBtn = (ImageView) view.findViewById(R.id.new_interval);
        mGroupSpinner = (Spinner) view.findViewById(R.id.spinner_group);
        mNewGroupBtn = (ImageView) view.findViewById(R.id.new_group);
    }

    private void setListener() {
        mTaskContent.addTextChangedListener(this);
        mAlarmDate.addTextChangedListener(this);
        mAlarmTime.addTextChangedListener(this);
    }

    protected void initServices() {
        mRenderService = new RenderService(getActivity());
    }

    protected void destroyServices() {
        if (mRenderService != null) {
            mRenderService.removeAllHandlers();
            mRenderService.destoryService();
        }
    }

    protected void showAddNewGroupDialog() {
        String title = getResources().getString(R.string.alert_dialog_title_new_group);
        String message = getResources().getString(R.string.alert_dialog_message_add_new_group);
        SpannableString ssTitle = new SpannableString(title);
        SpannableString ssMessage = new SpannableString(message);
        ssTitle.setSpan(new ForegroundColorSpan(getResources()
                        .getColor(android.R.color.holo_green_dark)),
                0,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssMessage.setSpan(new ForegroundColorSpan(getResources()
                        .getColor(android.R.color.holo_green_dark)),
                0,message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        final EditText editText = new EditText(getActivity());
        editText.setSingleLine(true);
        new AlertDialog.Builder(getActivity()).setTitle(ssTitle)
                .setMessage(ssMessage)
                .setView(editText)
                .setNegativeButton(R.string.edit_task_fragment_alert_dialog_calcel, null)
                .setPositiveButton(R.string.edit_task_fragment_alert_dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewGroup(editText);
                    }
                }).show();
    }

    protected void addNewGroup(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
            TaskBean taskBean = new TaskBean();
            taskBean.setGroup(editText.getText().toString());
            mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION__ADD_NEW_GROUP.value(),
                    Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP, null, taskBean, null,
                    Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP);
            // TODO: 2016/5/23 show loading view
        } else {
            Toast.makeText(getActivity(), R.string.edit_task_add_new_group_please_input_new_group_name,
                    Toast.LENGTH_SHORT).show();
        }
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

    protected void updateEditedViewStatue(EDITED_VIEW edited_view, EditText editView,
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
        } else {
            mEditedView = EDITED_VIEW.TASK_CONTENT;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onFragmentSelected(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyServices();
    }
}
