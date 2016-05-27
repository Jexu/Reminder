package com.tt.reminder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tt.sharedbaseclass.adapter.RenderRecycleViewAdapterBase;

/**
 * Created by zhengguo on 5/27/16.
 */
public class RenderRecycleViewAdapter extends RenderRecycleViewAdapterBase {

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RenderViewHolder viewHolder = new RenderViewHolder(
      LayoutInflater.from(mContext).inflate(R.layout.shared_list_item_view, null, false));
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
  }

  class RenderViewHolder extends RenderViewHolderBase {

    public RenderViewHolder(View itemRootView) {
      super(itemRootView);

    }
  }


}
