package com.tt.sharedbaseclass.constant;


/**
 * Created by zhengguo on 2016/5/16.
 */
public class Constant {
    public enum FRAGMENT_TYPE {
        TASKS_CONTAIN_WITH_DRAWER_VIEW_FRAGMENT(0), EDIT_TASK_FRAGMENT(1), NEW_EDIT_TASK_FRAGMENT(2), TASKS_CONTAIN_SEARCH_FRAGMENT(3);

        int value;
        FRAGMENT_TYPE(int value) {
            this.value = value;
        }

        public static FRAGMENT_TYPE valueOf(int value) {
            FRAGMENT_TYPE result = TASKS_CONTAIN_WITH_DRAWER_VIEW_FRAGMENT;
            switch (value) {
                case 0:
                    result = TASKS_CONTAIN_WITH_DRAWER_VIEW_FRAGMENT;
                    break;
                case 1:
                    result = EDIT_TASK_FRAGMENT;
                    break;
                case 2:
                    result = NEW_EDIT_TASK_FRAGMENT;
                    break;
                case 3:
                    result = TASKS_CONTAIN_SEARCH_FRAGMENT;
                    break;
            }
            return result;
        }
        public int value() {
            return this.value;
        }
    }

    public static class RenderServiceHelper {

        public enum ACTION {
            ACTION_DEFAULT(0), ACTION_GET_ALL_TASKS_BY_GROUP_NAME(1),
            ACTION_GET_ALL_GROUPS(2), ACTION__ADD_NEW_GROUP(3),
            ACTION_ADD_NEW_TASK(4), ACTION_UPDATE_GROUP_NAME(5),
            ACTION_UPDATE_TASK(6), ACTION_DELETE_GROUP(7),
            ACTION_DELETE_TASK(8), ACTION_SEARCH_BEANS(9),
            ACTION_SEND_FEEDBACK(10);
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
                    case 9:
                        result = ACTION_SEARCH_BEANS;
                        break;
                    case 10:
                        result = ACTION_SEND_FEEDBACK;
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
        public static final int HANDLER_MSG_WHAT_ON_HANDLE_FEEDBACK_SUCCESS = 4;



        public static final int REQUEST_CODE_DEFAULT = -1;
        public static final int REQUEST_CODE_INSERT_TASK_BEAN = -2;
        public static final int REQUEST_CODE__INSERT_NEW_GROUP = -3;
        public static final int REQUEST_CODE_UPDATE_TASK_BEAN = -4;
        public static final int REQUEST_CODE_UPDATE_GROUP_NAME = -5;
        public static final int REQUEST_CODE_DELETE_TASK_BEAN = -6;
        public static final int REQUEST_CODE_DELETE_GROUP = -7;
        public static final int REQUEST_CODE_SEARCH_BEANS = -8;
        public static final int REQUEST_CODE_SEND_FEEDBACK = -9;



        //this value must equal to zero
        public static final int REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED = 0;
        public static final int REQUEST_CODE_GET_TASKS_BEANS_BY_GROUP_NAME = -9;
        public static final int REQUEST_CODE_GET_GROUPS = -10;



        public static final int RESULT_CODE_FAIL = -1;
        public static final int RESULT_CODE_GET_TASKS_SUCCESS_HAS_DATE_ONLY = 0;
        public static final int RESULT_CODE_GET_TASKS_SUCCESS_NO_DATE_ONLY = 8;
        public static final int RESULT_CODE_GET_TASKS_SUCCESS_HAS_DATE_AND_NO_DATE = 9;
        public static final int RESULT_CODE_GET_TASKS_FAIL_NO_TASKS = 1;
        public static final int RESULT_CODE_GET_TASKS_FAIL_ERROR = 2;
        public static final int RESULT_CODE_GET_GROUPS_SUCCESS = 3;
        public static final int RESULT_CODE_GET_GROUPS_FAIL_NO_GROUPS = 4;
        public static final int RESULT_CODE_GET_GROUPS_FAIL_ERROR = 5;
        public static final int RESULT_CODE_INSERT_TASK_SUCCESS = 6;
        public static final int RESULT_CODE_INSERT_GROUP_SUCCESS = 7;
        public static final int RESULT_CODE_DELETE_TASK_SUCCESS = 8;
        public static final int RESULT_CODE_DELETE_GROUP_SUCCESS = 9;
        public static final int RESULT_CODE_UPDATE_TASK_SUCCESS = 10;
        public static final int RESULT_CODE_UPDATE_GROUP_SUCCESS = 11;

        public static final int RESULT_CODE_SEND_FEEDBACK_SUCCESS = 12;
        public static final int RESULT_CODE_SEND_FEEDBACK_FAIL = 13;


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
        public static String EXTRA_TABLE_TASKS_COLUM_IS_FINISHED = "colum_is_finished";


        public static String EXTRA_TABLE_GROUP_COLUM_ID = "_id";
        public static String EXTRA_TABLE_NAME_GROUP = "table_group";
        public static String EXTRA_TABLE_GROUP_COLUM_GROUP = "colum_group";
        public static final String EXTRA_UNFINISHED_TASKS_COUNT_EACH_GROUP = "extra_unfinished_count";
    }

    public static class BundelExtra {

        public static final int FINISH_REQUEST_CODE_DEFAULT = -1;
        public static final int FINISH_RESULT_CODE_DEFAULT = -2;
        public static final int FINISH_RESULT_CODE_SUCCESS = -5;
        public static final int FINISH_REQUEST_CODE_NEW_TASK = -3;
        public static final int FINISH_REQUEST_CODE_EDIT_TASK = -4;
        public static final int FINISH_REQUEST_CODE_SEARCH_BEAN = -5;



        public static final String EXTRA_FRAGMENT_TYPE = "extra_fragment_type";
        public static final String EXTRA_TASK_BEAN = "extra_task_bean";
        public static final String EXTRAL_GROUPS_BEANS = "extra_groups_beans";
        public static final String EXTRA_RENDER_OBJECT_BEAN = "extra_object_bean";
        public static final String EXTRA_UPDATE_ROW = "extra_update_row";
        public static final String EXTRA_REQUEST_CODE = "extra_request_code";
        public static final String EXTRA_RESULT_CODE = "extra_result_code";

        public static final String EXTRA_LRUCACHE = "extra_lrucache";

        public static final String EXTRA_START_FROM = "extra_start_from";

        public static final String EXTRA_IS_ADD_NEW_GROUP = "extra_is_add_new_group";


        public static final int START_FROM_NOTIFICATION = -1;
        public static final int START_FROM_DEFAULT = -100;

    }

//    public enum WEEK {
//        Sun(0), Mon(1), Tue(2), Wen(3), Thu(4), Fri(5), Sta(6), ;
//        private int value = 1;
//        WEEK(int value) {
//            this.value = value;
//        }
//        public static WEEK valueOf(int value) {
//            WEEK result = null;
//            switch (value) {
//                case 1:
//                    result = Mon;
//                break;
//                case 2:
//                    result = Tue;
//                    break;
//                case 3:
//                    result = Wen;
//                    break;
//                case 4:
//                    result = Thu;
//                    break;
//                case 5:
//                    result = Fri;
//                    break;
//                case 6:
//                    result = Sta;
//                    break;
//                case 0:
//                    result = Sun;
//                    break;
//            }
//            return result;
//        }
//        public int value() {
//            return this.value;
//        }
//    }
//
//    public enum MONTH {
//        Jan(1), Feb(2), Mar(3), Apr(4), May(5), Jun(6), Jul(7), Aug(8), Sept(9), Oct(10), Nov(11), Dec(12);
//        private int value = 1;
//        MONTH(int value) {
//            this.value = value;
//        }
//        public static MONTH valueOf(int value) {
//            MONTH result = null;
//            switch (value) {
//                case 1:
//                    result = Jan;
//                    break;
//                case 2:
//                    result = Feb;
//                    break;
//                case 3:
//                    result = Mar;
//                    break;
//                case 4:
//                    result = Apr;
//                    break;
//                case 5:
//                    result = May;
//                    break;
//                case 6:
//                    result = Jun;
//                    break;
//                case 7:
//                    result = Jul;
//                    break;
//                case 8:
//                    result = Aug;
//                    break;
//                case 9:
//                    result = Sept;
//                    break;
//                case 10:
//                    result = Oct;
//                    break;
//                case 11:
//                    result = Nov;
//                    break;
//                case 12:
//                    result = Dec;
//                    break;
//            }
//            return result;
//        }
//        public int value() {
//            return this.value;
//        }
//    }

    public enum REPEAT_UNIT {
        Minute(7), Hour(6), Day(5), Week(4), Month(3), Year(2), NO_REPEAT(1);
        private int value = 0;
        REPEAT_UNIT(int value) {
            this.value = value;
        }
        public static REPEAT_UNIT valueOf(int value) {
            REPEAT_UNIT result = NO_REPEAT;
            switch (value) {
                case 2:
                    result = Year;
                    break;
                case 3:
                    result = Month;
                    break;
                case 4:
                    result = Week;
                    break;
                case 5:
                    result = Day;
                    break;
                case 6:
                    result = Hour;
                    break;
                case 7:
                    result = Minute;
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

    public enum RENDER_ADAPTER_TYPE {
        TASKS_CONTAINER, LEFT_DRAWER_AGENDA, LEFT_DRAWER_TASKS_CATEGORY;
    }
}
