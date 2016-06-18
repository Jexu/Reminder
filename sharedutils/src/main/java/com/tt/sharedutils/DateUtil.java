package com.tt.sharedutils;

import java.util.Calendar;

/**
 * Created by zhengguo on 2016/6/18.
 */
public class DateUtil {

    public enum WEEK {
        Sun(0), Mon(1), Tue(2), Wen(3), Thu(4), Fri(5), Sta(6), ;
        private int value = 1;
        WEEK(int value) {
            this.value = value;
        }
        public static WEEK valueOf(int value) {
            WEEK result = null;
            switch (value) {
                case 1:
                    result = Mon;
                    break;
                case 2:
                    result = Tue;
                    break;
                case 3:
                    result = Wen;
                    break;
                case 4:
                    result = Thu;
                    break;
                case 5:
                    result = Fri;
                    break;
                case 6:
                    result = Sta;
                    break;
                case 0:
                    result = Sun;
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

    //Mon,12 Mar,2015
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        StringBuffer date = new StringBuffer();
        date.append(WEEK.valueOf(calendar.get(Calendar.DAY_OF_WEEK) - 1))
                .append(",")
                .append(calendar.get(Calendar.DAY_OF_MONTH))
                .append(" ")
                .append(MONTH.valueOf(calendar.get(Calendar.MONTH)+1))
                .append(",")
                .append(calendar.get(Calendar.YEAR));
        return date.toString();
    }
}
