package com.tt.sharedbaseclass.constant;

/**
 * Created by lab307 on 2016/5/16.
 */
public class Constant {
    public enum FRAGMENT_TYPE {
        TASKS_CONTAIN_WITH_DRAWER_VIEW_FRAGMENT, EDIT_TASK_FRAGMENT, NEW_EDIT_TASK_FRAGMENT;
    }

    public static class FragmentType {
        public static String FRAGMENT_TYPE = "fragment_type";
        public static int TASKS_CONTAIN_WITH_DRAWER_VIEW_FRAGMENT = 0;
        public static int EDIT_TASK_FRAGMENT = 1;
        public static int NEW_EDIT_TASK_FRAGMENT = 2;
    }
}
