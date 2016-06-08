package com.tt.sharedbaseclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.RenderObjectBeans;

/**
 * Created by zhengguo on 5/27/16.
 */
public abstract class RenderRecycleViewAdapterBase extends RecyclerView.Adapter
        implements RenderObjectBeans.OnRenderObjectBeansEmptyListener {

  public static final int POSITION_GROUP_MY_TASKS = 0;
  public static final int POSITION_GROUP_FINISHED = -100;

  protected RenderObjectBeans mRenderObjectBeans;
  protected OnItemClickListener mOnItemClickListener;
  protected Context mContext;
  protected Constant.RENDER_ADAPTER_TYPE mAdapterType;
  protected int mGroupPositionClickedBeforeThisTime = POSITION_GROUP_MY_TASKS;
  protected int mTaskPositionClickedBeforeThisTime = POSITION_GROUP_MY_TASKS;


  public interface OnItemClickListener {
    void onItemClickListener(View view, Constant.RENDER_ADAPTER_TYPE adapterType, int positionClickedBefore, int position);
    void onItemLongClickListener(View view,Constant.RENDER_ADAPTER_TYPE adapterType, int position);
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    void onAdapterEmpty(Constant.RENDER_ADAPTER_TYPE adapterType, boolean isAdapterEmpty);
  }

  public RenderRecycleViewAdapterBase() {
    super();
    this.mRenderObjectBeans = new RenderObjectBeans();
  }

  public RenderRecycleViewAdapterBase(Context context) {
    super();
    mContext = context;
    this.mRenderObjectBeans = new RenderObjectBeans();
  }

  public RenderRecycleViewAdapterBase(Context context, RenderObjectBeans renderObjectBeans) {
    super();
    mContext = context;
    this.mRenderObjectBeans = renderObjectBeans;
  }

  public void setContext(Context context) {
    mContext = context;
  }

  public void addBeanInOrder(Object bean, boolean isNotifyDataSetChanged) {
    if (mRenderObjectBeans != null) {
      mRenderObjectBeans.addBeanInOrder(bean);
      if (isNotifyDataSetChanged) {
        notifyDataSetChanged();
      }
    }
  }

  public void addBean(Object bean, boolean isNotifyDataSetChanged) {
    if (mRenderObjectBeans != null) {
      mRenderObjectBeans.add(bean);
      if (isNotifyDataSetChanged) {
        notifyDataSetChanged();
      }
    }
  }

  public void addAllBeans(RenderObjectBeans renderObjectBeans) {
    //mRenderObjectBeans.clear();
    mRenderObjectBeans = renderObjectBeans;
    if (renderObjectBeans.getListener() == null) {
      mRenderObjectBeans.setListener(this);
    }
    onRenderObjectBeansEmpty();
    notifyDataSetChanged();
  }

  public void removeBean(int position, boolean isNotifyDataSetChanged) {
    if (mRenderObjectBeans != null) {
      mRenderObjectBeans.remove(position);
      if (isNotifyDataSetChanged) {
        notifyDataSetChanged();
      }
    }
  }

  public void removeBean(Object bean, boolean isNotifyDataSetChanged) {
    if (mRenderObjectBeans != null) {
      int position = mRenderObjectBeans.indexOf(bean);
      mRenderObjectBeans.remove(position);
      if (isNotifyDataSetChanged) {
        notifyDataSetChanged();
      }
    }
  }

  public int findBeanPosition(Object bean) {
    if (mRenderObjectBeans != null) {
      return mRenderObjectBeans.indexOf(bean);
    }
    return -1;
  }

  public void clearAll() {
    if (mRenderObjectBeans != null) {
      mRenderObjectBeans.clear();
      mRenderObjectBeans.setCountTaskHasDate(0);
      mRenderObjectBeans.setCountTaskNoDate(0);
    }
  }

  public Object getBean(int position) {
    if (mRenderObjectBeans != null) {
      return mRenderObjectBeans.get(position);
    }
    return null;
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    if (onItemClickListener != null) {
      mOnItemClickListener = onItemClickListener;
    }
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
    ((RenderViewHolderBase)holder).mItemRootView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY) {
          mOnItemClickListener.onItemClickListener(v, mAdapterType, mGroupPositionClickedBeforeThisTime,
                  holder.getLayoutPosition() == getItemCount()? holder.getLayoutPosition() - 1: holder.getLayoutPosition());
          mGroupPositionClickedBeforeThisTime = position;
        } else if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
          mOnItemClickListener.onItemClickListener(v, mAdapterType, mTaskPositionClickedBeforeThisTime,
                  holder.getLayoutPosition() == getItemCount()? holder.getLayoutPosition() - 1: holder.getLayoutPosition());
          mTaskPositionClickedBeforeThisTime = position;
        }
      }
    });
    ((RenderViewHolderBase)holder).mItemRootView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        mOnItemClickListener.onItemLongClickListener(v, mAdapterType,
          holder.getLayoutPosition() == getItemCount()? holder.getLayoutPosition() - 1: holder.getLayoutPosition());
        return true;
      }
    });
  }

  @Override
  public int getItemCount() {
    return mRenderObjectBeans == null ? 0 : mRenderObjectBeans.size();
  }

  public int getPositionClickedBefore() {
    if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY) {
      return mGroupPositionClickedBeforeThisTime;
    } else {
      return mTaskPositionClickedBeforeThisTime;
    }
  }

  public void setPositionClicke(int positionClicked) {
    if (mAdapterType == Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY) {
      mGroupPositionClickedBeforeThisTime = positionClicked;
    } else {
      mTaskPositionClickedBeforeThisTime = positionClicked;
    }
  }

  @Override
  public void onRenderObjectBeansEmpty() {
    if (!mRenderObjectBeans.isEmpty()) {
      mOnItemClickListener.onAdapterEmpty(mAdapterType, false);
    } else {
      mOnItemClickListener.onAdapterEmpty(mAdapterType, true);
    }
  }

  protected class RenderViewHolderBase extends RecyclerView.ViewHolder {

    public View mItemRootView;

    public RenderViewHolderBase(View itemView) {
      super(itemView);
      mItemRootView = itemView;
    }
  }

}
