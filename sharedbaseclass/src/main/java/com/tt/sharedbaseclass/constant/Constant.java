package com.tt.sharedbaseclass.constant;

import android.app.DownloadManager;

import java.net.PortUnreachableException;
import java.security.PublicKey;

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

    public static class RenderDbHelper {

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

    public static String EXTRA_TASK_BEAN = "extra_task_bean";

    public enum WEEK {
        MON(1), TUE(2), WEN(3), THU(4), FRI(5), STA(6), SUN(7);
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
                case 7:
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
        MINUTE(0), HOUR(1), DAY(2), WEEK(3), MONTH(4), YEAR(5);
        private int value = 0;
        REPEAT_UNIT(int value) {
            this.value = value;
        }
        public static REPEAT_UNIT valueOf(int value) {
            REPEAT_UNIT result = MINUTE;
            switch (value) {
                case 1:
                    result = HOUR;
                    break;
                case 2:
                    result = DAY;
                    break;
                case 3:
                    result = WEEK;
                    break;
                case 4:
                    result = MONTH;
                    break;
                case 5:
                    result = YEAR;
                    break;
                default:
                    result = MINUTE;
                    break;
            }
            return result;
        }
        public int value() {
            return this.value;
        }
    }
}
