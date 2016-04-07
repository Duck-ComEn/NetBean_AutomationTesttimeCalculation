/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automationtesttimecalculation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
//import jdk.internal.jfr.events.FileWriteEvent;

/**
 *
 * @author WNamw7165990
 */
public class ReadWriteFileManagement {

  /**
   * ******************************************
   * readFile ******************************************
   */
  private ArrayList<String> readFile(String path) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    String line = null;
    ArrayList<String> bufRead = new ArrayList<String>();

    try {
      FileReader file = new FileReader(path);
      BufferedReader buf = new BufferedReader(file);
      while ((line = buf.readLine()) != null) {
        bufRead.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    return bufRead;
  }

  public String writeDateFile(String dateTxt, String toEndDate) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    ArrayList<String> val = readFile(dateTxt);

    try {
      FileWriter file = new FileWriter(dateTxt);
      BufferedWriter buf = new BufferedWriter(file);

      for (int i = 0; i < val.size(); i++) {
        if (val.get(i).substring(0, 11).equals("Latest Date")) {

          buf.write(val.get(i).substring(0, 12)
                  + "= "
                  + "'" + toEndDate.substring(0, 4)
                  + "-" + toEndDate.substring(4, 6)
                  + "-" + toEndDate.substring(6, 8)
                  + " " + "00:00:00"
                  + "'");
        } else {
          buf.write(val.get(i));
        }
        buf.newLine();
      }
      buf.close();
      return "update--> " + "lated date.txt" + " Completed";
    } catch (IOException e) {
      e.printStackTrace();
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    return null;
  }

  public String writeScript(String product, String masterScript, String executeScript, String latDate, String calendarPath) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    ArrayList<String> val = readFile(masterScript);

    DateManagement dateMng = new DateManagement();

    try {
      FileWriter file = new FileWriter(executeScript);
      BufferedWriter bufWrite = new BufferedWriter(file);

      for (int i = 0; i < val.size(); i++) {

        if (val.get(i).length() > 22) {
          if ((val.get(i).substring(0, 11).equals("set sql_cbd"))
                  || (val.get(i).substring(0, 11).equals("set sql_cbe"))
                  || (val.get(i).substring(0, 11).equals("set sql_cep"))
                  || (val.get(i).substring(0, 11).equals("set sql_mks"))
                  || (val.get(i).substring(0, 11).equals("set sql_mps"))) {
            String transformToString = String.valueOf(dateMng.calculatePreviousStartDate2Week(latDate));
            String transformToStringToEndDate = String.valueOf(dateMng.calculateCurrentEndDate(latDate));
            bufWrite.write(val.get(i).substring(0, 65)
                    + transformToString.substring(0, 4)
                    + "-" + transformToString.substring(4, 6)
                    + "-" + transformToString.substring(6, 8)
                    + " " + "00:00:00'"
                    + " " + val.get(i).substring(86, 100)
                    + "'" + transformToStringToEndDate.substring(0, 4)
                    + "-" + transformToStringToEndDate.substring(4, 6)
                    + "-" + transformToStringToEndDate.substring(6, 8)
                    + " " + "23:59:59'"
                    + " \"");
          } else if (val.get(i).substring(0, 22).equals("set '$P-From_End_Date_")) {
            String transformToString = String.valueOf(dateMng.calculatePreviousStartDate2Week(latDate));

            bufWrite.write(val.get(i).substring(0, 28)
                    + "   = "
                    + "'" + transformToString.substring(0, 4)
                    + "-" + transformToString.substring(4, 6)
                    + "-" + transformToString.substring(6, 8)
                    + " " + "00:00:00'");
          } else if (val.get(i).substring(0, 22).equals("set '$P-From_End_Date'")) {
            String transformToString = String.valueOf(dateMng.calculateCurrentEndDate(latDate));

            bufWrite.write(val.get(i).substring(0, 22)
                    + "         = "
                    + "'" + transformToString.substring(0, 4)
                    + "-" + transformToString.substring(4, 6)
                    + "-" + transformToString.substring(6, 8)
                    + " " + "00:00:00'");
          } else if (val.get(i).substring(0, 20).equals("set '$P-To_End_Date'")) {
            String transformToString = String.valueOf(dateMng.calculateCurrentEndDate(latDate));

            bufWrite.write(val.get(i).substring(0, 20)
                    + "           = "
                    + "'" + transformToString.substring(0, 4)
                    + "-" + transformToString.substring(4, 6)
                    + "-" + transformToString.substring(6, 8)
                    + " " + "00:00:00'");
          } else if (val.get(i).substring(0, 11).equals("set '$P-WW'")) {
            String transformToString = String.valueOf(dateMng.latWeek(calendarPath, latDate));

            bufWrite.write(val.get(i).substring(0, 20)
                    + "           = "
                    + "'" + transformToString
                    + "'");
          } else if (val.get(i).substring(0, 20).equals("set '$P-Update_Date'")) {
            String transformToString = String.valueOf(dateMng.curDate());

            bufWrite.write(val.get(i).substring(0, 20)
                    + "           = "
                    + "'" + transformToString.substring(0, 4)
                    + "-" + transformToString.substring(4, 6)
                    + "-" + transformToString.substring(6, 8)
                    + " " + dateMng.curDateAndTime().substring(9, 17)
                    + "'");
          } else if (val.get(i).substring(0, 21).equals("set '$P-Year_Of_Week'")) {
            String transformToString = String.valueOf(dateMng.latYear(latDate));

            bufWrite.write(val.get(i).substring(0, 21)
                    + "          = "
                    + "'" + transformToString
                    + "'");
          } else {
            bufWrite.write(val.get(i));
          }
        } else {
          bufWrite.write(val.get(i));
        }
        bufWrite.newLine();
      }
      bufWrite.close();
      File file2 = new File(executeScript);
      return "update--> " + file2.getName() + " Completed";
    } catch (IOException e) {
      e.printStackTrace();
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    return null;
  }

  public void deleteFile(String filePath) {
    File file = new File(filePath);
    file.delete();
  }

  public String checkAndGetProductStream(String location, String product) {
    File folder = new File(location);
    File[] listOfFiles = folder.listFiles();
    boolean found = false;
    String fileName, extFile = "";
    extFile = ".str";
    String StreamFileName = "";
    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        fileName = listOfFiles[i].getName();
        extFile = fileName.substring(fileName.length() - 4, fileName.length());
//          System.out.println("File " + fileName +", "+extFile);
        if (fileName.substring(0, 3).equals(product) && extFile.equals(".str")) {
          System.out.println("File " + fileName);
          StreamFileName = fileName;
          found = true;
        }
      }
    }
    if (found) {
      return (location + StreamFileName);
    }
    return "File Not Found";
  }

  public String findWrokWeek(String calendarPath, String datePrimaryKey) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

    try {

      BufferedReader br = new BufferedReader(new FileReader(calendarPath));
      String line = "";
      String[] lineValue = {};
      String calendarDate, calendarWeek ,calendarYear= "";

      while ((line = br.readLine()) != null) {
        lineValue = line.split("\t");
        calendarDate = lineValue[0];
        calendarWeek = lineValue[7];
        calendarYear = lineValue[1];
        if (calendarDate.equals(datePrimaryKey)) {
          return calendarYear + calendarWeek;
        }
      }
      br.close();
    } catch (IOException e) {
      Logger.getLogger(ReadWriteFileManagement.class.getName()).log(Level.SEVERE, null, e);
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    return "Not Found PrimaryKey in Calendar";
  }

  void writeScript(ScriptParam scriptParam, ConfigurationFile config) throws IOException {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

    FileWriter outputFile = new FileWriter(config.getExecuteSPSS_Script());
    outputFile.flush();
    BufferedWriter bufWrite = new BufferedWriter(outputFile);
    FileReader inputFile = new FileReader(config.getMassterSPSS_Script());
    BufferedReader bufRead = new BufferedReader(inputFile);
    String lineString = "";

    try {
      while ((lineString = bufRead.readLine()) != null) {
        //fill value in to Argument
        lineString = lineString.replaceAll("Param_StreamPath", scriptParam.getStreamPath());
        lineString = lineString.replaceAll("Param_Product", scriptParam.getProduct());
        lineString = lineString.replaceAll("Param_FromEndDate2Week", scriptParam.getFromEndDate2Week());
        lineString = lineString.replaceAll("Param_FromEndDate", scriptParam.getFromEndDate());
        lineString = lineString.replaceAll("Param_EndDate", scriptParam.getEndDate());
        lineString = lineString.replaceAll("Param_WorkWeek", scriptParam.getWorkWeek());
        lineString = lineString.replaceAll("Param_ResultAllDataPath", scriptParam.getResultAllPath());
        lineString = lineString.replaceAll("Param_ResultByTestCodeDataPath", scriptParam.getResultByTestCodePath());
        bufWrite.write(lineString + "\n");
      }
    } catch (FileNotFoundException e) {
      Logger.getLogger(ReadWriteFileManagement.class.getName()).log(Level.SEVERE, null, e);
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    bufRead.close();
    inputFile.close();
    bufWrite.close();
    outputFile.close();

  }
    void writeBatch(ConfigurationFile config) throws IOException {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    FileWriter outputFile = new FileWriter(config.getBatchExecuteSPSS());
    outputFile.flush();
    BufferedWriter bufWrite = new BufferedWriter(outputFile);
    FileReader inputFile = new FileReader(config.getBatchMaster());
    BufferedReader bufRead = new BufferedReader(inputFile);
    String lineString = "";

    try {
      while ((lineString = bufRead.readLine()) != null) {
        //fill value in to Argument
        lineString = lineString.replace("[ClementineHostName]", config.getClementineHostName());
        lineString = lineString.replace("[ClementinePortName]", config.getClementinePortName());
        lineString = lineString.replace("[ClementineUsername]", config.getClementineUsername());
        lineString = lineString.replace("[ClementinePassWord]", config.getClementinePassWord());
        lineString = lineString.replace("[ClementineLogFile]", config.getClementineLogFile());
        lineString = lineString.replace("[ClementineScript]", config.getClementineScript());

        bufWrite.write(lineString + "\n");
      }
    } catch (FileNotFoundException e) {
      Logger.getLogger(ReadWriteFileManagement.class.getName()).log(Level.SEVERE, null, e);
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
    bufRead.close();
    inputFile.close();
    bufWrite.close();
    outputFile.close();

  }
  
}
