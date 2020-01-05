package dev.glinzac.automation.quartz;

import org.springframework.stereotype.Service;

@Service
public class CronExpressionGenerator {
	
	public String convertMinutes(String minutes) {
		String cronMin;
		if(minutes.equals("")) {
			cronMin=" 0 ";
		}else if(minutes.equals("every")) {
			cronMin=" * ";
		}else if(minutes.equals("even")) {
			cronMin=" */2 ";
		}else if(minutes.equals("odd")) {
			cronMin=" 1/2 ";
		}else if(minutes.equals("every 5")) {
			cronMin=" */5 ";
		}else if(minutes.equals("every 15")) {
			cronMin=" */15 ";
		}else if(minutes.equals("every 30")) {
			cronMin=" */30 ";
		}else {
			cronMin=" "+minutes+" ";
		}
		return cronMin;
	}
	
	public String convertHours(String hours) {
		String cronHour;
		if(hours.equals("")) {
			cronHour="0 ";
		}else if(hours.equals("every")) {
			cronHour="* ";
		}else if(hours.equals("even")) {
			cronHour="*/2 ";
		}else if(hours.equals("odd")) {
			cronHour="1/2 ";
		}else if(hours.equals("every 6")) {
			cronHour="*/6 ";
		}else if(hours.equals("every 12")) {
			cronHour="*/12 ";
		}else if(hours.equals("Midnight")) {
			cronHour="0 ";
		}else {
			cronHour=hours+" ";
		}
		return cronHour;
	}
	
	public String convertDays(String days) {
		String cronDay;
		if(days.equals("")) {
			cronDay=" 0 ";
		}else if(days.equals("every")) {
			cronDay="* ";
		}else if(days.equals("even")) {
			cronDay="*/2 ";
		}else if(days.equals("odd")) {
			cronDay="1/2 ";
		}else if(days.equals("every 5")) {
			cronDay="*/5 ";
		}else if(days.equals("every 10")) {
			cronDay="*/10 ";
		}else if(days.equals("every 15")) {
			cronDay="*/15 ";
		}else {
			cronDay=days+" ";
		}
		return cronDay;
	}
	
	public String convertMonths(String months) {
		String cronMonth;
		if(months.equals("")) {
			cronMonth=" 0 ";
		}else if(months.equals("every")) {
			cronMonth="* ";
		}else if(months.equals("even")) {
			cronMonth="*/2 ";
		}else if(months.equals("odd")) {
			cronMonth="1/2 ";
		}else if(months.equals("every 4")) {
			cronMonth="*/4 ";
		}else if(months.equals("every 6")) {
			cronMonth="*/6 ";
		}else {
			cronMonth=months+" ";
		}
		return cronMonth;
	}
	
	public String convertWeeks(String weeks) {
		String cronWeek;
		if(weeks.equals("")) {
			cronWeek=" ? ";
		}else if(weeks.equals("every")) {
			cronWeek="? ";
		}else if(weeks.equals("weekday")) {
			cronWeek="MON-FRI ";
		}else if(weeks.equals("weekend")) {
			cronWeek="SUN,SAT ";
		}else {
			cronWeek=weeks+" ";
		}
		return cronWeek;
	}
	
	public String generateCronExpression(String min,String hour,String day,String month,String week) {
		String finalString = convertMinutes(min);
		finalString+=convertHours(hour);
		finalString+=convertDays(day);
		finalString+=convertMonths(month);
		finalString+=convertWeeks(week);
		return finalString;
	}
}
