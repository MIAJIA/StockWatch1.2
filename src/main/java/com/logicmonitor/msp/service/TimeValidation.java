package com.logicmonitor.msp.service;

import java.util.Date;

public class TimeValidation {
	public static boolean isValidate() {
//		LocalDate ld = LocalDate.now();
//        DayOfWeek dayOfWeek = ld.getDayOfWeek();
       
        Date date = new Date(System.currentTimeMillis());
       
        int dayOfWeek = date.getDay();

        if(dayOfWeek == 6 || dayOfWeek == 7) return false;
        int hourOfDay = date.getHours();
        int minOfHour = date.getMinutes();

        if(hourOfDay > 13 || (hourOfDay <= 6 && minOfHour <30))  return false;
		return true;
	}

}
