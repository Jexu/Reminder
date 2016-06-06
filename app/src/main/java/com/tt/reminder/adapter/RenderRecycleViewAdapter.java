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
import com.tt.sharedbaseclass.model.GroupBean;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;

/**
 * Created by zhengguo on 5/27/16.
 */
public class RenderRecycleViewAdapter extends RenderRecycleViewAdapterBase
        implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener{

  public static final float FINISHED_ITEM_ALPHA = (float) 0.7;
  public static final float UNFINISHED_ITEM_ALPHA = (float) 1;

  public RenderRecycleViewAdapter() {
    super();
  }

  public RenderRecycleViewAdapter(Context context, Constant.RENDER_ADAPTER_TYPE adapterType) {
    super(context);
    mAdapterType = adapterType;
  }

  public RenderRecycleViewAdapter(Context context, Constant.RENDER_ADAPTER_TYPE adapterType,RenderObjectBeans renderObjectBeans) {
    super(context, renderObjectBeans);
    mAdapterType = adapterType;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RenderViewHolder viewHolder;
    if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
      viewHolder = new RenderViewHolder(
              LayoutInflater.from(mContext).inflate(R.layout.shared_list_item_view, null, false));
    } else {
      viewHolder = new RenderViewHolder(
              LayoutInflater.from(mContext).inflate(R.layout.shared_list_item_in_drawer, null, false));
    }

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    RenderViewHolder h = (RenderViewHolder) holder;
    if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
      TaskBean taskBean = (TaskBean) mRenderObjectBeans.get(position);
      if (taskBean.isFinished() == TaskBean.VALUE_NOT_FINISHED) {
        h.mItemRootView.setAlpha(UNFINISHED_ITEM_ALPHA);
      } else {
        h.mItemRootView.setAlpha(FINISHED_ITEM_ALPHA);
      }
      h.mRightGroupName.setText(taskBean.getGroup());
      h.mRightTaskContent.setText(taskBean.getTaskContent());
      setDateTime(h, taskBean);
      setCheckBox(h, taskBean, position);
    } else if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY){
      GroupBean groupBean = (GroupBean) mRenderObjectBeans.get(position);
      h.mLeftSymbol.setText(groupBean.getGroup()+"                                                                          ");
      //h.mRightTaskContent.setText();
    }
  }

  private void setDateTime(RenderViewHolder h, TaskBean taskBean) {
    if (!taskBean.isClearedPickedDate() && !taskBean.isClearedPickedTime()) {
      if (taskBean.isDeadline() && taskBean.isFinished() == TaskBean.VALUE_NOT_FINISHED) {
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
      if (taskBean.getRepeatIntervalTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_INTERVAL) {
        h.mRightRepeat.setVisibility(View.VISIBLE);
        h.mRightRepeat.setText(mContext.getResources().getString(
                R.string.repeat_every_interval_unit, taskBean.getRepeatInterval()
                , Constant.REPEAT_UNIT.valueOf(taskBean.getRepeatUnit())));
      } else {
        h.mRightRepeat.setText("");
        h.mRightRepeat.setVisibility(View.GONE);
      }
    } else {
      if (mRenderObjectBeans.getCountTaskHasDate() > 0) {
        h.mLeftSymbol.setVisibility(View.INVISIBLE);
        h.mRightTime.setVisibility(View.INVISIBLE);
      } else {
        h.mLeftSymbol.setVisibility(View.GONE);
        h.mRightTime.setVisibility(View.GONE);
      }
      h.mRightRepeat.setText("");
      h.mRightRepeat.setVisibility(View.GONE);
    }
  }

  private void setCheckBox(RenderViewHolder h, TaskBean taskBean, int position) {
    if (taskBean.isFinished() == TaskBean.VALUE_FINISHED) {
      h.mRightCheckBox.setChecked(true);
    } else {
      h.mRightCheckBox.setChecked(false);
    }
    //h.mRightCheckBox.setOnCheckedChangeListener(this);
    h.mRightCheckBox.setOnClickListener(this);
    h.mRightCheckBox.setTag(R.id.shared_list_item_right_checkbox, position);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//    if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
//      TaskBean taskBean = (TaskBean) getBean(
//              Integer.parseInt(buttonView.getTag(R.id.shared_list_item_right_checkbox).toString()));
//      if ((taskBean.isFinished() == TaskBean.VALUE_FINISHED && isChecked)
//              || (taskBean.isFinished() == TaskBean.VALUE_NOT_FINISHED && !isChecked)) {
//        return;
//      }
//      mOnItemClickListener.onCheckedChanged(buttonView, isChecked);
//    }
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.shared_list_item_right_checkbox) {
      if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
      TaskBean taskBean = (TaskBean) getBean(
              Integer.parseInt(v.getTag(R.id.shared_list_item_right_checkbox).toString()));
//      if ((taskBean.isFinished() == TaskBean.VALUE_FINISHED && isChecked)
//              || (taskBean.isFinished() == TaskBean.VALUE_NOT_FINISHED && !isChecked)) {
//        return;
//      }
        boolean isChecked;
        if (taskBean.isFinished() == TaskBean.VALUE_NOT_FINISHED) {
          isChecked = true;
        } else {
          isChecked = false;
        }
      mOnItemClickListener.onCheckedChanged((CompoundButton) v, isChecked);
    }
    }
  }

  class RenderViewHolder extends RenderViewHolderBase {

    TextView mLeftDateFrom;
    TextView mLeftSymbol;
    TextView mLeftDateTo;
    TextView mRightGroupName;
    CheckBox mRightCheckBox;
    TextView mRightTaskContent;
    TextView mRightTime;
    TextView mRightRepeat;

    public RenderViewHolder(View itemRootView) {
      super(itemRootView);
      if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
        mLeftDateFrom = (TextView) itemRootView.findViewById(R.id.shared_list_item_left_date_from);
        mLeftSymbol = (TextView) itemRootView.findViewById(R.id.shared_list_item_left_date_symbol);
        mLeftDateTo = (TextView) itemRootView.findViewById(R.id.shared_list_item_left_date_to);
        mRightGroupName = (TextView) itemRootView.findViewById(R.id.shared_list_item_right_task_group_name);
        mRightCheckBox = (CheckBox) itemRootView.findViewById(R.id.shared_list_item_right_checkbox);
        mRightTaskContent = (TextView) itemRootView.findViewById(R.id.shared_list_item_right_task_content);
        mRightTime = (TextView) itemRootView.findViewById(R.id.shared_list_item_right_task_time);
        mRightRepeat = (TextView) itemRootView.findViewById(R.id.shared_list_item_right_repeat);
      } else {
        mLeftSymbol = (TextView) itemRootView.findViewById(R.id.shared_list_item_in_drawer_item_subtitle);
        mRightTaskContent = (TextView) itemRootView.findViewById(R.id.shared_list_item_in_drawer_item_count);
      }

    }
  }

}
