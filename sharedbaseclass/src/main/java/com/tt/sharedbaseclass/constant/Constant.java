package com.tt.sharedbaseclass.constant;


/**
 * Created by zhengguo on 2016/5/16.
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

    public static class RenderServiceHelper {

        public enum ACTION {
            ACTION_DEFAULT(0), ACTION_GET_ALL_TASKS_BY_GROUP_NAME(1),
            ACTION_GET_ALL_GROUPS(2), ACTION__ADD_NEW_GROUP(3),
            ACTION_ADD_NEW_TASK(4), ACTION_UPDATE_GROUP_NAME(5),
            ACTION_UPDATE_TASK(6), ACTION_DELETE_GROUP(7),
            ACTION_DELETE_TASK(8);
            int value = 0;
            ACTION(int value) {
                this.value = value;
            }

            public static ACTION valueOf(int value) {
                ACTION result = ACTION_DEFAULT;
                switch (value) {
                    case 1:
                        result = ACTION_GET_ALL_TASKS_BY_GROUP_NAME;
                        break;
                    case 2:
                        result = ACTION_GET_ALL_GROUPS;
                        break;
                    case 3:
                        result = ACTION__ADD_NEW_GROUP;
                        break;
                    case 4:
                        result = ACTION_ADD_NEW_TASK;
                        break;
                    case 5:
                        result = ACTION_UPDATE_GROUP_NAME;
                        break;
                    case 6:
                        result = ACTION_UPDATE_TASK;
                        break;
                    case 7:
                        result = ACTION_DELETE_GROUP;
                        break;
                    case 8:
                        result = ACTION_DELETE_TASK;
                        break;
                }
                return result;
            }
            public int value() {
                return this.value;
            }
        }

        public static final int HANDLER_MSG_WHAT_ON_SELECT_SUCCESS = 1;
        public static final int HANDLER_MSG_WHAT_ON_UPDATE_SUCCESS = 2;
        public static final int HANDLER_MSG_WHAT_ON_HANDLE_FAIL = 3;


        public static final int REQUEST_CODE_DEFAULT = 0;
        public static final int REQUEST_CODE_INSERT_TASK_BEAN = 1;
        public static final int REQUEST_CODE__INSERT_NEW_GROUP = 2;
        public static final int REQUEST_CODE_UPDATE_TASK_BEAN = 3;
        public static final int REQUEST_CODE_UPDATE_GROUP_NAME = 4;
        public static final int REQUEST_CODE_DELETE_TASK_BEAN = 5;
        public static final int REQUEST_CODE_DELETE_GROUP = 6;
        public static final int REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED = 7;
        public static final int REQUEST_CODE_GET_TASKS_BEANS_BY_GROUP_NAME = 9;
        public static final int REQUEST_CODE_GET_GROUPS = 8;



        public static final int RESULT_CODE_GET_TASKS_SUCCESS = 0;
        public static final int RESULT_CODE_GET_TASKS_FAIL_NO_TASKS = 1;
        public static final int RESULT_CODE_GET_TASKS_FAIL_ERROR = 2;
        public static final int RESULT_CODE_GET_GROUPS_SUCCESS = 3;
        public static final int RESULT_CODE_GET_GROUPS_FAIL_NO_GROUPS = 4;
        public static final int RESULT_CODE_GET_GROUPS_FAIL_ERROR = 5;
        public static final int RESULT_CODE_UPDATE_SUCCESS = 6;
        public static final int RESULT_CODE_UPDATE_FAIL = 7;

    }

    public static class RenderDbHelper {

        public static final String GROUP_NAME_FINISHED = "Finished";
        public static final String GROUP_NAME_MY_TASK = "My Tasks";

        public static final String EXTRA_TABLE_TASKS_COLUM_TIMILLS = "colum_timills";
        public static String EXTRA_TABLE_TASKS_COLUM_ID = "_id";
        public static String EXTRA_TABLE_NAME_TASKS = "table_tasks";
        public static String EXTRA_TABLE_TASKS_COLUM_CONTENT = "colum_content";
        public static String EXTRA_TABLE_TASKS_COLUM_YEAR = "colum_year";
        public static String EXTRA_TABLE_TASKS_COLUM_MONTH = "colum_month";
        public static String EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH = "colum_day_of_month";
        public static String EXTRA_TABLE_TASKS_COLUM_HOUR = "colum_hour";
        public static String EXTRA_TABLE_TASKS_COLUM_MINUTE = "colum_minute";
        public static String EXTRA_TABLE_TASKS_COLUM_REPEAT_INTERVAL = "colum_repeat_interval";
        public static String EXTRA_TABLE_TASKS_COLUM_REPEAT_UNIT = "colum_repeat_unit";

        public static String EXTRA_TABLE_GROUP_COLUM_ID = "_id";
        public static String EXTRA_TABLE_NAME_GROUP = "table_group";
        public static String EXTRA_TABLE_GROUP_COLUM_GROUP = "colum_group";
    }

    public static class BundelExtra {
        public static final String EXTRA_TASK_BEAN = "extra_task_bean";
        public static final String EXTRAL_GROUPS_BEANS = "extra_groups_beans";
        public static final String EXTRA_RENDER_OBJECT_BEAN = "extra_object_bean";
        public static final String EXTRA_UPDATE_ROW = "extra_update_row";
        public static final String EXTRA_REQUEST_CODE = "extra_request_code";
        public static final String EXTRA_RESULT_CODE = "extra_result_code";

    }

    public enum WEEK {
        SUN(0), MON(1), TUE(2), WEN(3), THU(4), FRI(5), STA(6), ;
        private int value = 1;
        WEEK(int value) {
            this.value = value;
        }
        public static WEEK valueOf(int value) {
            WEEK result = null;
            switch (value) {
                case 1:
                    result = MON;
                break;
                case 2:
                    result = TUE;
                    break;
                case 3:
                    result = WEN;
                    break;
                case 4:
                    result = THU;
                    break;
                case 5:
                    result = FRI;
                    break;
                case 6:
                    result = STA;
                    break;
                case 0:
                    result = SUN;
                    break;
            }
            return result;
        }
        public int value() {
            return this.value;
        }
    }

    public enum MONTH {
        Jan(1), Feb(2), Mar(3), Apr(4), May(5), Jun(6), Jul(7), Aug(8), Sept(9), Oct(10), Nov(11), Dec(12);
        private int value = 1;
        MONTH(int value) {
            this.value = value;
        }
        public static MONTH valueOf(int value) {
            MONTH result = null;
            switch (value) {
                case 1:
                    result = Jan;
                    break;
                case 2:
                    result = Feb;
                    break;
                case 3:
                    result = Mar;
                    break;
                case 4:
                    result = Apr;
                    break;
                case 5:
                    result = May;
                    break;
                case 6:
                    result = Jun;
                    break;
                case 7:
                    result = Jul;
                    break;
                case 8:
                    result = Aug;
                    break;
                case 9:
                    result = Sept;
                    break;
                case 10:
                    result = Oct;
                    break;
                case 11:
                    result = Nov;
                    break;
                case 12:
                    result = Dec;
                    break;
            }
            return result;
        }
        public int value() {
            return this.value;
        }
    }

    public enum REPEAT_UNIT {
        MINUTE(7), HOUR(6), DAY(5), WEEK(4), MONTH(3), YEAR(2), NO_REPEAT(1);
        private int value = 0;
        REPEAT_UNIT(int value) {
            this.value = value;
        }
        public static REPEAT_UNIT valueOf(int value) {
            REPEAT_UNIT result = NO_REPEAT;
            switch (value) {
                case 2:
                    result = YEAR;
                    break;
                case 3:
                    result = MONTH;
                    break;
                case 4:
                    result = WEEK;
                    break;
                case 5:
                    result = DAY;
                    break;
                case 6:
                    result = HOUR;
                    break;
                case 7:
                    result = MINUTE;
                    break;
                default:
                    result = NO_REPEAT;
                    break;
            }
            return result;
        }
        public int value() {
            return this.value;
        }
    }

    public enum TASK_BEAN_STATUS {

        TASK_CONTENT_NULL(0), DATE_NOT_SET(1), TIME_NOT_SET(2), AVAILABLE_SAVE(3);
        private int value;
        TASK_BEAN_STATUS(int value) {
            this.value = value;
        }

        public static TASK_BEAN_STATUS valueOf(int value) {
            TASK_BEAN_STATUS result = TASK_CONTENT_NULL;
            switch (value) {
                case 0:
                    result = TASK_CONTENT_NULL;
                    break;
                case 1:
                    result = DATE_NOT_SET;
                    break;
                case 2:
                    result = TIME_NOT_SET;
                    break;
                case 3:
                    result = AVAILABLE_SAVE;
                    break;
            }
            return result;
        }

        public int value() {
            return this.value;
        }
    }
}
