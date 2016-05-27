package com.tt.sharedbaseclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.tt.sharedbaseclass.model.RenderObjectBeans;

/**
 * Created by zhengguo on 5/27/16.
 */
public abstract class RenderRecycleViewAdapterBase extends RecyclerView.Adapter {

  protected RenderObjectBeans mRenderObjectBeans;
  protected OnItemClickListener mOnItemClickListener;
  protected View mItemRootView;
  protected Context mContext;

  public interface OnItemClickListener {
    void onItemClickListener(View view, int position);
    void onItemLongClickListener(View view, int position);
  }

  public RenderRecycleViewAdapterBase() {
    super();
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

  public void addBean(Object bean) {
    if (mRenderObjectBeans != null) {
      mRenderObjectBeans.add(bean);
    }
  }

  public void removeBean(int position) {
    if (mRenderObjectBeans != null) {
      mRenderObjectBeans.remove(position);
    }
  }

  public void removeBean(Object bean) {
    if (mRenderObjectBeans != null) {
      int position = mRenderObjectBeans.indexOf(bean);
      mRenderObjectBeans.remove(bean);
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
