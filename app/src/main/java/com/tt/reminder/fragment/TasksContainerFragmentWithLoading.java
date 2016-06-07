package com.tt.reminder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.RenderObjectBeans;

/**
 * Created by zhengguo on 6/7/16.
 */
public abstract class TasksContainerFragmentWithLoading extends TasksContainFragment {

  private static long INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK = 500;
  private long mFirstBackPressedTime = 0;


  protected boolean mIsGroupCached = false;
  protected boolean mIsMyTasksCached = false;

  private GetTasksByGroupNameCallback mGetTasksByGroupNameCallback;
  private GetGroupsCallback mGetGroupsCallback;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (view != null) {

    }
  }

  @Override
  public void initServices() {
    super.initServices();
    mGetTasksByGroupNameCallback = new GetTasksByGroupNameCallback(this);
    mGetGroupsCallback = new GetGroupsCallback(this);
    mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_TASKS_BY_GROUP_NAME.toString(),
      mGetTasksByGroupNameCallback);
    mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_GROUPS.toString(),
      mGetGroupsCallback);
  }

  @Override
  public void fetchData() {
    Log.i("Render", "fetchData");
    mIsGroupCached = mLruCache.get(Constant.BundelExtra.EXTRAL_GROUPS_BEANS) == null ? false : true;
    mIsMyTasksCached = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+Constant.RenderDbHelper.GROUP_NAME_MY_TASK) == null ? false : true;
    getGroupsExceptFinished(Constant.RenderServiceHelper.REQUEST_CODE_DEFAULT);
  }

  protected void getTasksByGroupName(String GroupName, int requestCode) {
    if (!mIsMyTasksCached) {
      mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_TASKS_BY_GROUP_NAME.value(),
        Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS,
        null,
        null,
        new String[]{GroupName},
        requestCode);
    }
  }

  protected void getGroupsExceptFinished(int requestCode) {
    if (!mIsGroupCached) {
      mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_GROUPS.value(),
        Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP,
        null,
        null,
        null,
        requestCode);
    }
    //must getGroups firstly here, then get tasks
    getTasksByGroupName(Constant.RenderDbHelper.GROUP_NAME_MY_TASK
      , Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED);
  }

  protected abstract void getTasksSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);

  protected abstract void getGroupsSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);

  @Override
  public boolean onBackPressed() {
    if (mFirstBackPressedTime == 0
      || System.currentTimeMillis() - mFirstBackPressedTime >= INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK) {
      mFirstBackPressedTime = System.currentTimeMillis();
      Toast.makeText(getActivity(), com.tt.sharedbaseclass.R.string.toast_double_click_to_exit, Toast.LENGTH_SHORT).show();
      return true;
    }
    return false;
  }

  private static class GetTasksByGroupNameCallback extends RenderCallback {

    private TasksContainerFragmentWithLoading mContext;
    private GetTasksByGroupNameCallback(TasksContainerFragmentWithLoading context) {
      mContext = context;
    }
    @Override
    public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
      Log.i("Render", "get tasks successfully");
      mContext.getTasksSuccess(renderObjectBeans, requestCode, resultCode);
    }

    @Override
    public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {

    }

    @Override
    public void onHandleFail(int requestCode, int resultCode) {
      Log.e("Render", "fail to get tasks");
    }
  }

  private static class GetGroupsCallback extends RenderCallback {
    private TasksContainerFragmentWithLoading mContext;
    private GetGroupsCallback(TasksContainerFragmentWithLoading context) {
      mContext = context;
    }

    @Override
    public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
      Log.i("Render", "get group successfully");
      mContext.getGroupsSuccess(renderObjectBeans, requestCode, resultCode);
    }

    @Override
    public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {

    }

    @Override
    public void onHandleFail(int requestCode, int resultCode) {
      Log.e("Render", "fail to get group");
    }
  }

}
