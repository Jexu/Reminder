package com.tt.reminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.tt.reminder.R;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.model.ConfigBean;
import com.tt.sharedbaseclass.utils.RenderSharedPreference;

/**
 * Created by zhengguo on 6/23/16.
 */
public class SettingsFragment extends FragmentBaseWithSharedHeaderView implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

  private CheckBox mIsNotificationEnable;
  private CheckBox mIsNotificationLightDisable;
  private TextView mPrivacyPolicy;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View contentView = inflater.inflate(R.layout.setting_layout, container, false);
    mIsNotificationEnable = (CheckBox) contentView.findViewById(R.id.is_notification_enable);
    mIsNotificationLightDisable = (CheckBox) contentView.findViewById(R.id.is_notification_light_disable);
    mIsNotificationEnable.setChecked(ConfigBean.isNotificationEnable());
    mIsNotificationLightDisable.setChecked(ConfigBean.isNotificationLightDisable());
    mPrivacyPolicy = (TextView) contentView.findViewById(R.id.privacy_policy);
    return contentView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mHeaderViewMainMenu.setVisibility(View.GONE);
    mHeaderViewVoiceInput.setVisibility(View.GONE);
    mHeaderViewSearchBtn.setVisibility(View.GONE);
    mHeaderViewAddNewTask.setVisibility(View.GONE);
    mHeaderViewLeftArrow.setVisibility(View.VISIBLE);
    mHeaderViewTitle.setGravity(Gravity.START);
    mHeaderViewTitle.setText(R.string.shared_list_item_in_drawer_subtitle_setting);

    mHeaderViewLeftArrow.setOnClickListener(this);
    mIsNotificationEnable.setOnCheckedChangeListener(this);
    mIsNotificationLightDisable.setOnCheckedChangeListener(this);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    switch (buttonView.getId()) {
      case R.id.is_notification_enable:
        ConfigBean.setIsNotificationEnable(isChecked);
        RenderSharedPreference.getInstance(getActivity()).putBoolean(ConfigBean.IS_NOTIFICATION_ENABLE, isChecked);
        break;
      case R.id.is_notification_light_disable:
        ConfigBean.setIsNotificationLightDisable(isChecked);
        RenderSharedPreference.getInstance(getActivity()).putBoolean(ConfigBean.IS_NOTIFICATION_LIGHT_DISABLE, isChecked);
        break;
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.header_view_left_arrow:
        finish();
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
