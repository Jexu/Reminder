package com.tt.sharedbaseclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;

/**
 * Created by zhengguo on 5/27/16.
 */
public abstract class RenderRecycleViewAdapterBase extends RecyclerView.Adapter {

  protected RenderObjectBeans mRenderObjectBeans;
  protected OnItemClickListener mOnItemClickListener;
  protected Context mContext;

  public interface OnItemClickListener {
    void onItemClickListener(View view, int position);
    void onItemLongClickListener(View view, int position);
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

  public void addBean(Object bean) {
    if (mRenderObjectBeans != null) {
      mRenderObjectBeans.addBeanInOrder(bean);
      notifyDataSetChanged();
    }
  }

  public void addAllBeans(RenderObjectBeans renderObjectBeans) {
    mRenderObjectBeans.clear();
    mRenderObjectBeans = renderObjectBeans;
    notifyDataSetChanged();
  }

  public void removeBean(int position) {
    if (mRenderObjectBeans != null) {
       if (((TaskBean)mRenderObjectBeans.get(position)).isClearedPickedDate()) {
         mRenderObjectBeans.setCountTaskNoDate(mRenderObjectBeans.getCountTaskNoDate() - 1);
       } else {
         mRenderObjectBeans.setCountTaskHasDate(mRenderObjectBeans.getCountTaskHasDate() - 1);
       }
      mRenderObjectBeans.remove(position);
      notifyDataSetChanged();
    }
  }

  public void removeBean(Object bean) {
    if (mRenderObjectBeans != null) {
      int position = mRenderObjectBeans.indexOf(bean);
      mRenderObjectBeans.remove(bean);
    }
  }

  public void clearAll() {
    if (mRenderObjectBeans != null) {
      mRenderObjectBeans.clear();
    }
  }

  public Object getBean(int position) {
    if (mRenderObjectBeans != null) {
      return mRenderObjectBeans.get(position);
    }
    return null;
  }

  public void setmOnItemClickLitener(OnItemClickListener onItemClickListener) {
    if (onItemClickListener != null) {
      mOnItemClickListener = onItemClickListener;
    }
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    ((RenderViewHolderBase)holder).mItemRootView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mOnItemClickListener.onItemClickListener(v, holder.getLayoutPosition());
      }
    });
    ((RenderViewHolderBase)holder).mItemRootView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        mOnItemClickListener.onItemLongClickListener(v, holder.getLayoutPosition());
        return true;
      }
    });
  }

  @Override
  public int getItemCount() {
    return mRenderObjectBeans == null ? 0 : mRenderObjectBeans.size();
  }

  protected class RenderViewHolderBase extends RecyclerView.ViewHolder {

    protected View mItemRootView;

    public RenderViewHolderBase(View itemView) {
      super(itemView);
      mItemRootView = itemView;
    }
  }

}
