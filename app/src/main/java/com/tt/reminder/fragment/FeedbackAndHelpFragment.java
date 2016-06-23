package com.tt.reminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedutils.AndroidUtil;
import com.tt.sharedutils.IntentUtil;

/**
 * Created by zhengguo on 6/22/16.
 */
public class FeedbackAndHelpFragment extends FragmentBaseWithSharedHeaderView implements View.OnClickListener {

  private TextView mSourceCode;
  private TextView mFeedback;
  private TextView mContactDeveloper;
  private TextView mAppVersionName;

  private final String MY_EMAIL = "ttjekux@gmail.com";
  private final String MY_GITHUB = "https://github.com/Jexu/Reminder";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View contentView = inflater.inflate(R.layout.feedback_and_help_layout, container, false);
    mSourceCode = (TextView) contentView.findViewById(R.id.source_code);
    mFeedback = (TextView) contentView.findViewById(R.id.feedback);
    mContactDeveloper = (TextView) contentView.findViewById(R.id.contact_developer);
    mAppVersionName = (TextView) contentView.findViewById(R.id.app_version_name);
    return contentView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mHeaderViewMainMenu.setVisibility(View.GONE);
    mHeaderViewLeftArrow.setVisibility(View.VISIBLE);
    mHeaderViewVoiceInput.setVisibility(View.GONE);
    mHeaderViewSearchBtn.setVisibility(View.GONE);
    mHeaderViewAddNewTask.setVisibility(View.GONE);
    mHeaderViewTitle.setText(R.string.shared_headerview_title_feedback_and_help);

    mHeaderViewLeftArrow.setOnClickListener(this);
    mSourceCode.setOnClickListener(this);
    mFeedback.setOnClickListener(this);
    mContactDeveloper.setOnClickListener(this);
    if (AndroidUtil.getAppVersionName(getActivity()) != null) {
      mAppVersionName.setText(getResources().getString(R.string.app_version, AndroidUtil.getAppVersionName(getActivity())));
    }
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.header_view_left_arrow:
        finish();
        break;
      case R.id.source_code:
        IntentUtil.openWebUrl(getActivity(), MY_GITHUB);
        break;
      case R.id.feedback:
        FeedbackFragment feedbackFragment = new FeedbackFragment();
        MainActivity.navigateTo(feedbackFragment, getFragmentManager());
        break;
      case R.id.contact_developer:
        if (!IntentUtil.sendEmail(getActivity(), getResources().getString(R.string.chooser_title_complete_action_using), new String[]{MY_EMAIL})) {
          Toast.makeText(getActivity(), R.string.toast_message_you_have_no_mail_app, Toast.LENGTH_SHORT).show();
        }
        break;
    }
  }

  @Override
  public void fetchData() {

  }

  @Override
  public boolean onBackPressed() {
    finish();
    return true;
  }

}
