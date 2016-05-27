package com.tt.sharedbaseclass.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Toast;

import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedutils.DeviceUtil;

/**
 * Created by Administrator on 2016/5/27.
 */
public abstract class TaskContainFragmentBase extends FragmentBaseWithSharedHeaderView
        implements View.OnClickListener {

    private static long INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK = 500;
    private long mFirstBackPressedTime = 0;

    protected LruCache<String, RenderObjectBeans> mLruCache;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLruCache = new LruCache<>((int) DeviceUtil.getMaxMemory()/8);
    }

    @Override
    public void fetchData() {
        Log.i("Render", "fetchData");
        mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_TASKS_BY_GROUP_NAME.value(),
                Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS,
                null,
                null,
                null,
                Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED);
        mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_GROUPS.value(),
                Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP,
                null,
                null,
                null,
                0);
    }

    protected abstract void getTasksSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);

    protected abstract void getGroupsSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);

    @Override
    public boolean onBackPressed() {
        if (mFirstBackPressedTime == 0
                || System.currentTimeMillis() - mFirstBackPressedTime >= INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK) {
            mFirstBackPressedTime = System.currentTimeMillis();
            Toast.makeText(getActivity(), R.string.toast_double_click_to_exit, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.header_view_main_menu) {
            onMainMenuClick();
        } else if (i == R.id.header_view_add_new_task) {
            navigateToEditFragment(Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value());
        }
    }

    protected abstract void onMainMenuClick();

    protected abstract void navigateToEditFragment(int fragmentType);

    protected void onMainMenuAnimation(View mainMenu, float drawerViewSlideOffset) {
        mainMenu.setRotation(drawerViewSlideOffset * 90);
    }

}