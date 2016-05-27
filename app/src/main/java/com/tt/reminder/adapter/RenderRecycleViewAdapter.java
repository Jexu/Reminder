package com.tt.reminder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tt.reminder.R;
import com.tt.sharedbaseclass.adapter.RenderRecycleViewAdapterBase;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;

/**
 * Created by zhengguo on 5/27/16.
 */
public class RenderRecycleViewAdapter extends RenderRecycleViewAdapterBase implements CompoundButton.OnCheckedChangeListener {

  public RenderRecycleViewAdapter() {
    super();
  }

  public RenderRecycleViewAdapter(Context context) {
    super(context);
  }

  public RenderRecycleViewAdapter(Context context, RenderObjectBeans renderObjectBeans) {
    super(context, renderObjectBeans);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RenderViewHolder viewHolder = new RenderViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.shared_list_item_view, null, false));
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    RenderViewHolder h = (RenderViewHolder) holder;
    TaskBean taskBean = (TaskBean) mRenderObjectBeans.get(position);
    h.mRightGroupName.setText(taskBean.getGroup());
    h.mRightTaskContent.setText(taskBean.getTaskContent());
    setDateTime(h, taskBean);
    setCheckBox(h, taskBean);
  }

  private void setDateTime(RenderViewHolder h, TaskBean taskBean) {
    if (!taskBean.isClearedPickedDate() && !taskBean.isClearedPickedTime()) {
      if (taskBean.isDeadline()) {
        h.mLeftSymbol.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        h.mRightTime.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
      } else {
        h.mLeftSymbol.setTextColor(mContext.getResources().getColor(android.R.color.black));
        h.mRightTime.setTextColor(mContext.getResources().getColor(android.R.color.tertiary_text_dark));
      }
      h.mLeftSymbol.setText(taskBean.getPickedDate(false));
      h.mRightTime.setText(taskBean.getPickedTime(false));
      h.mLeftSymbol.setVisibility(View.VISIBLE);
      h.mRightTime.setVisibility(View.VISIBLE);
    } else {
      h.mLeftSymbol.setVisibility(View.GONE);
      h.mRightTime.setVisibility(View.GONE);
    }
  }

  private void setCheckBox(RenderViewHolder h, TaskBean taskBean) {
    if (taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_FINISHED)) {
      h.mRightCheckBox.setChecked(true);
    } else {
      h.mRightCheckBox.setChecked(false);
    }
    h.mRightCheckBox.setOnCheckedChangeListener(this);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

  }

  class RenderViewHolder extends RenderViewHolderBase {

    TextView mLeftDateFrom;
    TextView mLeftSymbol;
    TextView mLeftDateTo;
    TextView mRightGroupName;
    CheckBox mRightCheckBox;
    TextView mRightTaskContent;
    TextView mRightTime;

    public RenderViewHolder(View itemRootView) {
      super(itemRootView);
      mLeftDateFrom = (TextView) itemRootView.findViewById(R.id.shared_list_item_left_date_from);
      mLeftSymbol = (TextView) itemRootView.findViewById(R.id.shared_list_item_left_date_symbol);
      mLeftDateTo = (TextView) itemRootView.findViewById(R.id.shared_list_item_left_date_to);
      mRightGroupName = (TextView) itemRootView.findViewById(R.id.shared_list_item_right_task_group_name);
      mRightCheckBox = (CheckBox) itemRootView.findViewById(R.id.shared_list_item_right_checkbox);
      mRightTaskContent = (TextView) itemRootView.findViewById(R.id.shared_list_item_right_task_content);
      mRightTime = (TextView) itemRootView.findViewById(R.id.shared_list_item_right_task_time);

    }
  }

}
