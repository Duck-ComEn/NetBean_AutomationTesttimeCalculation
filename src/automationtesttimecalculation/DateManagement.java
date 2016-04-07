/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automationtesttimecalculation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author WNamw7165990
 */
public class DateManagement {

  /**
   * **************************************************
   ******************** Common Method ****************** ***************************************************
   */
  public int getDayOfWeekBy_StringDate(String date) {
    Calendar calendar = Calendar.getInstance();
    int year = Integer.parseInt(String.valueOf(date).substring(0, 4));
    int month = Integer.parseInt(String.valueOf(date).substring(4, 6));
    int day = Integer.parseInt(String.valueOf(date).substring(6, 8));
    calendar.set(year, month - 1, day);
    int x = calendar.get(Calendar.DAY_OF_WEEK);
    return x;
  }

  public int calculateDateWith_OffSet(int date, int offset) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    Date dt = new Date();
    try {
      dt = df.parse(String.valueOf(date));
      Calendar c = Calendar.getInstance();
      c.setTime(dt);
      c.add(Calendar.DATE, offset);
      dt = c.getTime();
      String newDateString = df.format(dt);
      return Integer.valueOf(newDateString);
    } catch (ParseException e) {
      e.printStackTrace();
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    return 0;
  }

  public int dayDif(String dat1, String dat2) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    String dateStart = dat1 + " 00:00:00";
    String dateStop = dat2 + " 00:00:00";

    //HH converts hour in 24 hours format (0-23), day calculation
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.getDefault());

    Date d1 = null;
    Date d2 = null;

    try {
      d1 = format.parse(dateStart);
      d2 = format.parse(dateStop);
      //in milliseconds
      long diff = d2.getTime() - d1.getTime();
      long diffDays = diff / (24 * 60 * 60 * 1000);
      int day = (int) diffDays;
      return day;

    } catch (Exception e) {
      e.printStackTrace();
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    return 0;

  }

  public int getWeek(String calendarPath, String date) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    
    String line = null;
    String curWeektmp = null;
    String tmp = null;
    try {
      File file = new File(calendarPath);
      if (file.exists()) {
        FileReader fr = new FileReader(file);
        BufferedReader bfReader = new BufferedReader(fr);
        while ((line = bfReader.readLine()) != null) {
          int tab = 0;
          int countWeekVal = 0;
          /*
           ********************************************** 
           * Check week from Date
           ********************************************** 
           */
          if (line.substring(0, 8).equals(date)) {
            for (int i = 0; i < line.length(); i++) {
              // Check Tab
              if (line.substring(i, i + 1).equals("\t")) {
                tab++;

                if (tab == 8) {
                  while (tab != 9) {
                    i++;
                    if (line.substring(i, i + 1).equals("\t")) {

                      //Change countInLine is original Value;
                      i = i - countWeekVal;
                      tmp = (line.substring(i, i + countWeekVal));
                      tab++;

                    } else {
                      countWeekVal++;

                    }

                  }
                }

              }
            }
          }
        }
      } else {
        System.out.println(file.getName() + ": " + "not exists!");
      }

    } catch (IOException e) {
      e.printStackTrace();
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    return Integer.parseInt(tmp);
  }

  ///////////////////////////////////////////////////////
  /**
   * **************************************************
   **************** Current Option ******************** ***************************************************
   */
  public String curDateAndTime() {
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    DateFormat dateFormatHour = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    //       return  "20131110 01:40:15";
    //       globalCurrentDate = dateFormat.format(date);
    return dateFormat.format(date) + " " + dateFormatHour.format(date);
  }

  public String curDate() {
    String tmp = curDateAndTime().substring(0, 8);
    return tmp;
  }

  public int curWeek(String calendarPath, String curDate) {
    int tmp = getWeek(calendarPath, curDate);
    return tmp;
  }

  public int curYear() {
    return Integer.parseInt(curDateAndTime().substring(0, 4));
  }

  /**
   * **************************************************
   ******************** Latest Option******************* ***************************************************
   */
  public String latDateAndTime(String dateTxt) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    String line = null;
    String tmp = null;
    try {
      File fileDateTxt = new File(dateTxt);
      if (fileDateTxt.exists()) {
        FileReader readDateTxt = new FileReader(dateTxt);
        BufferedReader bfReadDateTxt = new BufferedReader(readDateTxt);
        while ((line = bfReadDateTxt.readLine()) != null) {
          int singleQuote = 0;
          int countChar = 0;
          if (line.length() >= 6) {
            if (line.substring(0, 11).equals("Latest Date")) {
              for (int countInLine = 0; countInLine < line.length(); countInLine++) {
                // Check " ' "
                if (line.substring(countInLine, countInLine + 1).equals("'")) {
                  singleQuote++;
                  if (singleQuote == 1) {
                    while (singleQuote != 2) {
                      countInLine++;
                      if (line.substring(countInLine, countInLine + 1).equals("'")) {
                        //Change countInLine is original Value;
                        countInLine = countInLine - countChar;
                        singleQuote++;
                        tmp = (line.substring(countInLine, countInLine + countChar));
                      } else {
                        countChar++;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      } else {
        System.out.println(fileDateTxt.getName() + ": " + "not exists!");
      }
    } catch (IOException e) {
      e.printStackTrace();
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    return tmp.substring(0, 4) + tmp.substring(5, 7) + tmp.substring(8, 10) + " " + tmp.substring(11, 19);
  }

  public String latDate(String dateTxt) {
    String tmp = latDateAndTime(dateTxt).substring(0, 8);
    return tmp;
  }

  public int latWeek(String calendarPath, String latDate) {
    int tmp = getWeek(calendarPath, latDate);
    return tmp;
  }

  public int latYear(String latDate) {
    int tmp = Integer.parseInt(latDate.substring(0, 4));
    return tmp;
  }

  public String calculatePreviousStartDate2Week(String endWeekDate) {
    // input need to calculateEndWeekDate before call this function.  
    String fromEnd2Wk = String.valueOf(calculateDateWith_OffSet(Integer.valueOf(endWeekDate), -14));
    fromEnd2Wk = fromEnd2Wk.substring(0, 4) + "-" + fromEnd2Wk.substring(4, 6) + "-" + fromEnd2Wk.substring(6, 8) + " 00:00:00";
    return fromEnd2Wk;
  }

  public String calculatePreviousStartDate1Week(String endWeekDate) {
    // input need to calculateEndWeekDate before call this function.  
    String fromEnd1Wk = String.valueOf(calculateDateWith_OffSet(Integer.valueOf(endWeekDate), -7));
    fromEnd1Wk = fromEnd1Wk.substring(0, 4) + "-" + fromEnd1Wk.substring(4, 6) + "-" + fromEnd1Wk.substring(6, 8) + " 00:00:00";
    return fromEnd1Wk;
  }

  public String calculateCurrentEndDate(String endWeekDate) {
    // input need to calculateEndWeekDate before call this function.  
    String enddate = String.valueOf(calculateDateWith_OffSet(Integer.valueOf(endWeekDate), -1));
    enddate = enddate.substring(0, 4) + "-" + enddate.substring(4, 6) + "-" + enddate.substring(6, 8) + " 23:59:59";
    return enddate;
  }

  public String findEndWeekDateBy_IntDate(String latDate) {

    int dayOfWeek = getDayOfWeekBy_StringDate(latDate);

    String endDateOFWeek = String.valueOf(calculateDateWith_OffSet(Integer.valueOf(latDate), 8 - dayOfWeek));
    return endDateOFWeek;
  }
  public String getCalendar_WDWorkWeek(ReadWriteFileManagement rwFileMng,String calendarPath, String datePrimaryKey){
    return rwFileMng.findWrokWeek(calendarPath,datePrimaryKey);
  }
//  public static void main(String[] args) {
//    DateManagement dm = new DateManagement();
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160101"));
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160102"));
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160103"));
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160104"));
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160105"));
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160106"));
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160107"));
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160108"));
//    System.out.println(dm.findEndWeekDateBy_IntDate("20160109"));
//    System.out.println(dm.calculateDateWith_OffSet(20160110, -15 + 1));
//
//  }
}
