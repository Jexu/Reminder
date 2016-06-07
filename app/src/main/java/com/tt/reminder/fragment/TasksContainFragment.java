package com.tt.reminder.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.reminder.adapter.RenderRecycleViewAdapter;
import com.tt.reminder.notification.RenderAlarm;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedutils.DeviceUtil;

import java.util.Calendar;

/**
 * Created by zhengguo on 6/7/16.
 */
public class TasksContainFragment extends FragmentBaseWithSharedHeaderView
  implements RenderRecycleViewAdapter.OnItemClickListener, View.OnClickListener {

  protected LruCache<String, RenderObjectBeans> mLruCache;

  protected TextView mNoTaskPage;
  protected RecyclerView mTasksContainerRecycleView;
  protected RenderRecycleViewAdapter mTasksContainerAdapter;
  protected RenderObjectBeans mRenderObjectBeansGroups;
  protected UpdateBeanCallback mUpdateBeanCallback;

  protected int mFragmentType;

  public TasksContainFragment() {
    // Required empty public constructor
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      mFragmentType = args.getInt(Constant.BundelExtra.EXTRA_FRAGMENT_TYPE);
      //mLruCache = (LruCache<String, RenderObjectBeans>) args.getSerializable(Constant.BundelExtra.EXTRA_LRUCACHE);
    } else {
      mLruCache = new LruCache<>((int) DeviceUtil.getMaxMemory() / 8);
    }
    mTasksContainerAdapter = new RenderRecycleViewAdapter(getActivity(), Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER);
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
      mHeaderViewSearch.setVisibility(View.GONE);
      mHeaderViewAddNewTask.setVisibility(View.GONE);
      mHeaderViewSaveTask.setVisibility(View.GONE);
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
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    int position = Integer.parseInt(buttonView.getTag(R.id.shared_list_item_right_checkbox).toString());
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
        mTasksContainerAdapter.removeBean(requestCode, false);
      } else {
        RenderObjectBeans tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + taskBean.getGroup());
        if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
          RenderObjectBeans myTasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
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
      mTasksContainerAdapter.notifyDataSetChanged();
    }
  }

  protected void taskFinishStatueChanged(long row, int requestCode) {
    if (requestCode >= 0) {
      TaskBean taskBean = (TaskBean) mTasksContainerAdapter.getBean(requestCode);
      if (taskBean.isFinished() == TaskBean.VALUE_FINISHED) {
        //finished --> unfinished
        if (mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + taskBean.getGroup()) != null) {
          TaskBean newTaskBean = new TaskBean();
          newTaskBean.copy(taskBean);
          newTaskBean.setIsFinished(TaskBean.VALUE_NOT_FINISHED);
          //应该根据组信息add两次；myTasks和自身groupName
          RenderObjectBeans tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + taskBean.getGroup());
          if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
            RenderObjectBeans myTasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
            if (myTasks != null) {
              myTasks.addBeanInOrder(newTaskBean);
            }
          }
          if (tasks != null) {
            tasks.addBeanInOrder(newTaskBean);
          }
        }
        mTasksContainerAdapter.removeBean(requestCode, false);
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
          mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_FINISHED).addBeanInOrder(newTaskBean);
        }
        //应该根据组信息update or remove两次；myTasks和自身groupName
        RenderObjectBeans tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + taskBean.getGroup());
        RenderObjectBeans myTasks = null;
        if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
          myTasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
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
      mTasksContainerAdapter.notifyDataSetChanged();
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
    editTaskFragment.setEnterTransition(new Slide().setDuration(300L));
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
      RenderObjectBeans myTasks = mLruCache.get(
        Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
      RenderObjectBeans tasks;
      if (myTasks != null) {
        myTasks.addBeanInOrder(newTaskBean);
      }
      if (!newTaskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
        tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + newTaskBean.getGroup());
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
      RenderObjectBeans tasksOldBean = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + oldBean.getGroup());
      RenderObjectBeans myTasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);

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
          RenderObjectBeans tasksNewBean = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + newBean.getGroup());
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
    }
  }

  @Override
  public boolean onBackPressed() {
    finish();
    return true;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mLruCache != null) {
      mLruCache.evictAll();
      mLruCache = null;
    }
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


}
