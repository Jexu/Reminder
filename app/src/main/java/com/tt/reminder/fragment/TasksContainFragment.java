package com.tt.reminder.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;
import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.reminder.adapter.RenderRecycleViewAdapter;
import com.tt.reminder.notification.RenderAlarm;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.RenderLruCache;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedutils.DeviceUtil;
import com.tt.sharedutils.StringUtil;

import java.util.Calendar;

/**
 * Created by zhengguo on 6/7/16.
 */
public class TasksContainFragment extends FragmentBaseWithSharedHeaderView
  implements RenderRecycleViewAdapter.OnItemClickListener, View.OnClickListener
              , SearchView.OnQueryTextListener {

  protected RenderLruCache<String, RenderObjectBeans> mLruCache;

  protected TextView mNoTaskPage;
  protected RecyclerView mTasksContainerRecycleView;
  protected RenderRecycleViewAdapter mTasksContainerAdapter;
  protected RenderObjectBeans mRenderObjectBeansGroups;
  protected UpdateBeanCallback mUpdateBeanCallback;
  private SearchBeanCallback mSearchBeanCallback;

  protected DefaultItemAnimator mRecycleViewIteamAnimator;

  public TasksContainFragment() {
    // Required empty public constructor
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()
        && args != null) {
      mLruCache = (RenderLruCache<String, RenderObjectBeans>) args.getSerializable(Constant.BundelExtra.EXTRA_LRUCACHE);
      mRenderObjectBeansGroups = (RenderObjectBeans) mLruCache.get(Constant.BundelExtra.EXTRAL_GROUPS_BEANS);
    } else {
      mLruCache = new RenderLruCache<>((int) DeviceUtil.getMaxMemory() / 8);
    }
    mTasksContainerAdapter = new RenderRecycleViewAdapter(getActivity(), Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER);
    mRecycleViewIteamAnimator = new DefaultItemAnimator();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View contentView;
    if (mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()) {
      contentView = inflater.inflate(R.layout.shared_tasks_container_view, container, false);
    } else {
      contentView = inflater.inflate(R.layout.fragment_tasks_containt_with_drawer_view, container, false);
    }
    mTasksContainerRecycleView = (RecyclerView) contentView.findViewById(R.id.list);
    mNoTaskPage = (TextView) contentView.findViewById(R.id.no_task_page);
    return contentView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()) {
      mHeaderViewLeftArrow.setVisibility(View.VISIBLE);
      mHeaderViewMainMenu.setVisibility(View.GONE);
      mHeaderViewVoiceInput.setVisibility(View.GONE);
      mHeaderViewTitle.setVisibility(View.GONE);
      mHeaderViewSearchBtn.setVisibility(View.GONE);
      mHeaderViewAddNewTask.setVisibility(View.GONE);
      mHeaderViewSaveTask.setVisibility(View.GONE);
      mHeaderViewSearch.setVisibility(View.VISIBLE);
      mHeaderViewLeftArrow.setOnClickListener(this);
      mHeaderViewSearch.setOnQueryTextListener(this);
    } else {
      mHeaderViewLeftArrow.setVisibility(View.GONE);
    }

    mTasksContainerRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mTasksContainerRecycleView.setItemAnimator(new DefaultItemAnimator());
    mTasksContainerRecycleView.setAdapter(mTasksContainerAdapter);
    mTasksContainerAdapter.setOnItemClickListener(this);
  }

  @Override
  public void initServices() {
    super.initServices();
    if (mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()) {
      mSearchBeanCallback = new SearchBeanCallback(this);
      mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_SEARCH_BEANS.toString()
        , mSearchBeanCallback);
    }
    mUpdateBeanCallback = new UpdateBeanCallback(this);
    mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.toString()
      , mUpdateBeanCallback);
    mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_DELETE_TASK.toString(),
      mUpdateBeanCallback);
  }

  @Override
  public void fetchData() {

  }

  protected void updateRecycleView(RenderObjectBeans renderObjectBeans) {
    if (renderObjectBeans != null) {
      mTasksContainerAdapter.addAllBeans(renderObjectBeans);
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.header_view_left_arrow:
        if (mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()) {
          finishWithResultCode(Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS,null);
        }
        break;
      case R.id.header_view_search:
        break;
    }
  }

  @Override
  public void onItemClickListener(View view, Constant.RENDER_ADAPTER_TYPE adapterType, int positionClickedBefore, int position) {
    if (adapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
      TaskBean taskBean = (TaskBean) mTasksContainerAdapter.getBean(position);
      navigateToEditFragment(Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value(), taskBean, position);
    }
  }

  @Override
  public void onItemLongClickListener(View view, Constant.RENDER_ADAPTER_TYPE adapterType, final int position) {

    if (adapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
      String alertTitle = getResources().getString(R.string.alert_dialog_title_are_you_sure);
      String alertMessage = getResources().getString(R.string.alert_dialog_message_delete_this_task);
      int action = Constant.RenderServiceHelper.ACTION.ACTION_DELETE_TASK.value();
      int id = ((TaskBean) mTasksContainerAdapter.getBean(position)).getId();
      String[] wheres = new String[]{id + ""};
      showDeleteAlertDialog(action, alertTitle, alertMessage, wheres, position);
    }
  }

  protected void showDeleteAlertDialog(final int action, String alertTitle, String alertMessage, final String[] wheres, final int position) {
    AlertDialog.Builder builder = getDefaultAlertDialogBuilder(alertTitle
      , alertMessage);
    builder.setNegativeButton(R.string.alert_dialog_negative_button_cancel, null)
      .setPositiveButton(R.string.alert_dialog_negative_button_delete
        , new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          deleteBean(action
            , action == Constant.RenderServiceHelper.ACTION.ACTION_DELETE_TASK.value()
            ? Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS : Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP
            , wheres
            , position);
        }
      }).show();
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    if (StringUtil.isEmpty(query)) {
      return false;
    }
    mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_SEARCH_BEANS.value()
      , Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
      , null
      , null
      , new String[]{query}
      , Constant.RenderServiceHelper.REQUEST_CODE_SEARCH_BEANS);
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    return false;
  }

  private void searchSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
    mTasksContainerAdapter.clearAll();
    mTasksContainerAdapter.addAllBeans(renderObjectBeans);
  }

  private void searchFail(int requestCode, int resultCode) {
    mTasksContainerAdapter.addAllBeans(new RenderObjectBeans());
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, int position, boolean isChecked) {
    //int position = Integer.parseInt(buttonView.getTag(R.id.shared_list_item_right_checkbox).toString());
    //Log.i("Render", position+"");
    TaskBean oldTaskBean = (TaskBean) mTasksContainerAdapter.getBean(position);
    TaskBean newTaskBean = new TaskBean();
    newTaskBean.copy(oldTaskBean);
    if (isChecked && newTaskBean.getRepeatIntervalTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_INTERVAL) {
      //repeat --> reset alarm time
      resetTaskBeanAlarmDate(newTaskBean);
    } else {
      //no repeat
      newTaskBean.setIsFinished(isChecked ? TaskBean.VALUE_FINISHED : TaskBean.VALUE_NOT_FINISHED);
    }
    mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.value()
      , Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
      , oldTaskBean
      , newTaskBean
      , null
      , position);
  }

  private void resetTaskBeanAlarmDate(TaskBean taskBean) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis() + taskBean.getRepeatIntervalTimeInMillis());
    taskBean.setYear(calendar.get(Calendar.YEAR));
    taskBean.setMonth(calendar.get(Calendar.MONTH));
    taskBean.setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
    taskBean.setHour(calendar.get(Calendar.HOUR));
    taskBean.setMinuse(calendar.get(Calendar.MINUTE));
  }

  @Override
  public void onAdapterEmpty(Constant.RENDER_ADAPTER_TYPE adapterType, boolean isAdapterEmpty) {
    if (adapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER && isAdapterEmpty) {
      mTasksContainerRecycleView.setVisibility(View.GONE);
      mNoTaskPage.setVisibility(View.VISIBLE);
      if (mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()) {
        mNoTaskPage.setText(R.string.fragment_tasks_container_there_is_no_result);
      }
    } else if (adapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER && !isAdapterEmpty) {
      mTasksContainerRecycleView.setVisibility(View.VISIBLE);
      mNoTaskPage.setVisibility(View.GONE);
    }
  }

  protected void deleteBean(int action, String tableName, String[] wheres, int requestCode) {
    mRenderService.getOrUpdate(action
      , tableName
      , null
      , null
      , wheres
      , requestCode);
  }

  protected void onUpdateBeanSuccess(long row, int requestCode, int resultCode) {

    switch (resultCode) {
      case Constant.RenderServiceHelper.RESULT_CODE_DELETE_TASK_SUCCESS:
        if (requestCode >= 0) {
          // TODO: 2016/5/29 remove task bean
          taskDeleted(row, requestCode);
        }
        break;
      case Constant.RenderServiceHelper.RESULT_CODE_UPDATE_TASK_SUCCESS:
        // TODO: 5/31/16 task finished status changed
        taskFinishStatueChanged(row, requestCode);
        break;
    }

  }

  protected void taskDeleted(long row, int requestCode) {
    if (requestCode >= 0) {
      TaskBean taskBean = (TaskBean) mTasksContainerAdapter.getBean(requestCode);
      if (taskBean.isFinished() == TaskBean.VALUE_FINISHED) {
        RenderObjectBeans tasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_FINISHED);
        if (tasks != null) {
          tasks.remove(mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()
            ?tasks.indexOf(taskBean):requestCode);
        }
      } else {
        RenderObjectBeans tasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + taskBean.getGroup());
        if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
          RenderObjectBeans myTasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
          if (myTasks != null) {
            myTasks.remove(myTasks.indexOf(taskBean));
          }
        }
        if (tasks != null) {
          tasks.remove(tasks.indexOf(taskBean));
        }
        // TODO: 5/31/16 remove notification alarm
        if (taskBean.getTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_DATE_TIME) {
          RenderAlarm.removeAlarm(getActivity(), taskBean);
        }
      }
      if (mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()) {
        mTasksContainerAdapter.removeBean(requestCode, false);
      }
      mTasksContainerAdapter.notifyDataSetChanged();
    }
  }

  protected void taskFinishStatueChanged(long row, int requestCode) {
    if (requestCode >= 0) {
      TaskBean taskBean = (TaskBean) mTasksContainerAdapter.getBean(requestCode);
      if (taskBean.isFinished() == TaskBean.VALUE_FINISHED) {
        //finished --> unfinished
          TaskBean newTaskBean = new TaskBean();
          newTaskBean.copy(taskBean);
          newTaskBean.setIsFinished(TaskBean.VALUE_NOT_FINISHED);
          //应该根据组信息add两次；myTasks和自身groupName
          RenderObjectBeans tasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + taskBean.getGroup());
          if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
            RenderObjectBeans myTasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
            if (myTasks != null) {
              myTasks.addBeanInOrder(newTaskBean);
            }
          }
          if (tasks != null) {
            tasks.addBeanInOrder(newTaskBean);
          }
        RenderObjectBeans tasksFinished = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_FINISHED);
        if (tasksFinished != null) {
          tasksFinished.remove(mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()
            ?tasksFinished.indexOf(taskBean):requestCode);
        }
        // TODO: 5/31/16 create notification alarm again
        if (taskBean.getTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_DATE_TIME) {
          RenderAlarm.createAlarm(getActivity(), taskBean);
        }
      } else {
        //unfinished --> finished
        boolean isRepeat = taskBean.getRepeatIntervalTimeInMillis() == TaskBean.DEFAULT_VALUE_OF_INTERVAL ? false : true;
        if (!isRepeat && mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_FINISHED) != null) {
          TaskBean newTaskBean = new TaskBean();
          newTaskBean.copy(taskBean);
          newTaskBean.setIsFinished(TaskBean.VALUE_FINISHED);
          mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_FINISHED).addBeanInOrder(newTaskBean);
        }
        //应该根据组信息update or remove两次；myTasks和自身groupName
        RenderObjectBeans tasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + taskBean.getGroup());
        RenderObjectBeans myTasks = null;
        if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
          myTasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
          if (myTasks != null) {
            myTasks.remove(myTasks.indexOf(taskBean));
          }
        }
        if (tasks != null) {
          tasks.remove(tasks.indexOf(taskBean));
        }

        if (isRepeat) {
          resetTaskBeanAlarmDate(taskBean);
          if (tasks != null) {
            tasks.addBeanInOrder(taskBean);
          }
          if (myTasks != null) {
            myTasks.addBeanInOrder(taskBean);
          }
          // TODO: 5/31/16 reset notification alarm
          if (taskBean.getTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_DATE_TIME) {
            RenderAlarm.updateAlarm(getActivity(), taskBean);
          }
        } else {
          // TODO: 5/31/16 remove notification alarm
          if (taskBean.getTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_DATE_TIME) {
            RenderAlarm.removeAlarm(getActivity(), taskBean);
          }
        }
      }
      if (mFragmentType == Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value()) {
        mTasksContainerAdapter.removeBean(requestCode, false);
        if(taskBean.isFinished() == TaskBean.VALUE_NOT_FINISHED){
          taskBean.setIsFinished(TaskBean.VALUE_FINISHED);
        } else {
          taskBean.setIsFinished(TaskBean.VALUE_NOT_FINISHED);
        }
        mTasksContainerAdapter.addBeanInOrder(taskBean, false);
      }
      mTasksContainerAdapter.notifyDataSetChanged();
    }
  }

  protected void navigateToEditFragment(int fragmentType, TaskBean taskBean, int requestCode) {
    EditTaskFragment editTaskFragment = new EditTaskFragment();
    Bundle args = new Bundle();
    if (fragmentType == Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value()) {
      args.putSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN, taskBean);
    }
    args.putInt(Constant.BundelExtra.EXTRA_FRAGMENT_TYPE, fragmentType);
    // TODO: 2016/5/25 have to check null, if null then get groups again
    args.putSerializable(Constant.BundelExtra.EXTRAL_GROUPS_BEANS,
      mRenderObjectBeansGroups);
    editTaskFragment.setArguments(args);
    MainActivity.navigateToForResultCode(editTaskFragment
      , getFragmentManager()
      , requestCode);
  }

  @Override
  public void onFinishedWithResult(int requestCode, int resultCode, Bundle bundle) {
    super.onFinishedWithResult(requestCode, resultCode, bundle);
    Log.i("Render", "onFinishedWithResult");
    if (resultCode == Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS
      && requestCode == Constant.BundelExtra.FINISH_REQUEST_CODE_NEW_TASK) {
      TaskBean newTaskBean = (TaskBean) bundle.get(Constant.BundelExtra.EXTRA_TASK_BEAN);
      RenderObjectBeans myTasks = mLruCache.getFromCache(
        Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
      RenderObjectBeans tasks;
      if (myTasks != null) {
        myTasks.addBeanInOrder(newTaskBean);
      }
      if (!newTaskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
        tasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + newTaskBean.getGroup());
        if (tasks != null) {
          tasks.addBeanInOrder(newTaskBean);
        }
      }
      mTasksContainerAdapter.notifyDataSetChanged();

    } else if (resultCode == Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS
      && requestCode >= 0) {
      //request code is old task position
      TaskBean newBean = (TaskBean) bundle.get(Constant.BundelExtra.EXTRA_TASK_BEAN);
      TaskBean oldBean = (TaskBean) mTasksContainerAdapter.getBean(requestCode);
      RenderObjectBeans tasksOldBean = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + oldBean.getGroup());
      RenderObjectBeans myTasks = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);

      if (myTasks != null) {
        myTasks.remove(myTasks.indexOf(oldBean));
        myTasks.addBeanInOrder(newBean);
      }
      if (oldBean.getGroup().equals(newBean.getGroup())) {
        if (oldBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
          //group of oldbean and newbean are equal and are MyTasks
        } else {
          //group of oldbean and newbean are equal and both are not MyTasks
          if (tasksOldBean != null) {
            tasksOldBean.remove(tasksOldBean.indexOf(oldBean));
            tasksOldBean.addBeanInOrder(newBean);
          }
        }
      } else {
        //group of oldben and newbean are not equal
        if (oldBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
          //group of oldbean is myTasks
          RenderObjectBeans tasksNewBean = mLruCache.getFromCache(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + newBean.getGroup());
          if (tasksNewBean != null) {
            tasksNewBean.addBeanInOrder(newBean);
          }
        } else {
          //group of oldbean is not myTasks
          if (tasksOldBean != null) {
            tasksOldBean.remove(tasksOldBean.indexOf(oldBean));
          }
        }
      }
      mTasksContainerAdapter.notifyDataSetChanged();
    } else if (resultCode == Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS
        && requestCode == Constant.BundelExtra.FINISH_REQUEST_CODE_SEARCH_BEAN) {
      mTasksContainerAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public Transition enterTransition() {
    return new Fade().setDuration(0xfaL);
  }

  @Override
  public Transition exitTransition() {
    return new Fade().setDuration(0xfaL);
  }

  @Override
  public boolean onBackPressed() {
    finishWithResultCode(Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS, null);
    return true;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mTasksContainerAdapter.clearAll();
  }

  private static class UpdateBeanCallback extends RenderCallback {
    private TasksContainFragment mContext;

    private UpdateBeanCallback(TasksContainFragment context) {
      mContext = context;
    }

    @Override
    public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {

    }

    @Override
    public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {
      Log.i("Render", "update successfully");
      //delete and update
      mContext.onUpdateBeanSuccess(row, requestCode, resultCode);
    }

    @Override
    public void onHandleFail(int requestCode, int resultCode) {
      Log.e("Render", "fail to update");
    }
  }

  private static class SearchBeanCallback extends RenderCallback {
    private TasksContainFragment mContext;
    private SearchBeanCallback(TasksContainFragment context) {
      mContext = context;
    }
    @Override
    protected void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
      Log.i("Render", "success to search");
      mContext.searchSuccess(renderObjectBeans, requestCode, resultCode);
    }

    @Override
    protected void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {

    }

    @Override
    protected void onHandleFail(int requestCode, int resultCode) {
      Log.i("Render", "fail to search");
      mContext.searchFail(requestCode, resultCode);
    }
  }


}
