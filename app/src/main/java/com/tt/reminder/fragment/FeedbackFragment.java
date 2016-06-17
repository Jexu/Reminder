package com.tt.reminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.tt.reminder.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.model.RenderFeedbackCallback;
import com.tt.sharedbaseclass.model.RenderFeedbackService;
import com.tt.sharedutils.StringUtil;

/**
 * Created by zhengguo on 6/16/16.
 */
public class FeedbackFragment extends FragmentBaseWithSharedHeaderView implements View.OnClickListener, TextWatcher {

  private Button mCancel;
  private Button mSend;
  private EditText mEditArea;
  private EditText mEmail;
  private SendFeedbackCallback mSendFeedbackCallback;
  private RenderFeedbackService mService;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View contentView = inflater.inflate(R.layout.shared_feedback_layout, container, false);
    return contentView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mHeaderViewMainMenu.setVisibility(View.GONE);
    mHeaderViewVoiceInput.setVisibility(View.GONE);
    mHeaderViewSearchBtn.setVisibility(View.GONE);
    mHeaderViewAddNewTask.setVisibility(View.GONE);
    mHeaderViewTitle.setText(R.string.shared_headerview_title_feedback);
    mEditArea = (EditText) view.findViewById(R.id.feedback_edit_area);
    mEmail = (EditText) view.findViewById(R.id.feedback_email_edit_text);
    mCancel = (Button) view.findViewById(R.id.feedback_cancel_btn);
    mSend = (Button) view.findViewById(R.id.feedback_send_btn);
    mEditArea.addTextChangedListener(this);
    mCancel.setOnClickListener(this);
    mSend.setOnClickListener(this);
    mSend.setClickable(false);
    mSend.setBackgroundResource(R.drawable.shared_button_gray);
  }

  @Override
  public void initServices() {
    super.initServices();
    mSendFeedbackCallback = new SendFeedbackCallback(this);
    mService = new RenderFeedbackService(getActivity());
    mService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_SEND_FEEDBACK.toString(),mSendFeedbackCallback);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.feedback_cancel_btn:
        finish();
        break;
      case R.id.feedback_send_btn:
        sendFeedback();
        break;
    }
  }

  private void sendFeedback() {
    if (TextUtils.isEmpty(mEditArea.getText())) {
      Toast.makeText(getActivity(), R.string.toast_message_please_input_a_comment, Toast.LENGTH_SHORT).show();
      return;
    } else if (!TextUtils.isEmpty(mEmail.getText()) && !StringUtil.emailPattern(mEmail.getText().toString().trim())) {
      Toast.makeText(getActivity(), R.string.toast_message_email_address_is_not_correct, Toast.LENGTH_SHORT).show();
      return;
    }
    mService.sendFeedback(Constant.RenderServiceHelper.ACTION.ACTION_SEND_FEEDBACK.value()
      , mEditArea.getText().toString()
      , mEmail.getText().toString()
      , Constant.RenderServiceHelper.REQUEST_CODE_SEND_FEEDBACK);
  }

  private void onSendFeedbackSuccess(int requestCode, int resultCode) {
    finish();
  }

  private void onSendFeedbackFail(int requestCode, int resuleCode) {
    finish();
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    if (TextUtils.isEmpty(s) && mSend.isClickable()) {
      mSend.setClickable(false);
      mSend.setBackgroundResource(R.drawable.shared_button_gray);
    } else if (!TextUtils.isEmpty(s) && !mSend.isClickable()) {
      mSend.setClickable(true);
      mSend.setBackgroundResource(R.drawable.shared_button_background);
    }
  }

  @Override
  public void afterTextChanged(Editable s) {

  }

  @Override
  public void fetchData() {

  }

  @Override
  public boolean onBackPressed() {
    finish();
    return true;
  }

  private static class SendFeedbackCallback extends RenderFeedbackCallback {
    private FeedbackFragment mContext;
    protected SendFeedbackCallback(FeedbackFragment context) {
      mContext = context;
    }

    @Override
    protected void onSendSuccess(int requestCode, int resultCode) {
      Log.i("Render", "success to send feedback");
      mContext.onSendFeedbackSuccess(requestCode, resultCode);
    }

    @Override
    protected void onSendFail(int requestCode, int resultCode) {
      Log.i("Render", "fail to send feedback");
      mContext.onSendFeedbackFail(requestCode, resultCode);
    }
  }
}
