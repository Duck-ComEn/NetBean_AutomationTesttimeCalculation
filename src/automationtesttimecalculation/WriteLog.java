/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automationtesttimecalculation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PKaml7162859
 */
public class WriteLog {

  static void writeLogFile(String logMsg) {
    try {
      DateFormat DateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
      Date dateobj = new Date();
      String date_time = DateFormat.format(dateobj);
      String filename = "AutomationTT.log";
      FileWriter fw = new FileWriter(filename, true); //the true will append the new data
      fw.write(date_time + "::" + logMsg + "\n");//appends the string to the file
      fw.close();
    } catch (IOException ioe) {
      System.err.println("IOException: " + ioe.getMessage());
    }
  }

  static void dumpScript2Log(String executeSPSS_Script) throws FileNotFoundException, IOException {
    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
    FileReader inputFile = new FileReader(executeSPSS_Script);
    BufferedReader bufRead = new BufferedReader(inputFile);
    String lineString = "";
    writeLogFile(methodName + "()," + "---------------------------SPSS-Script-DUMP-------------------------");
    try {
      while ((lineString = bufRead.readLine()) != null) {
        writeLogFile(lineString);
      }
    } catch (FileNotFoundException e) {
      Logger.getLogger(ReadWriteFileManagement.class.getName()).log(Level.SEVERE, null, e);
      WriteLog.writeLogFile(methodName + "()," + e.getMessage() + "\n" + e.getCause());
    }
    writeLogFile(methodName + "()," + "---------------------------SPSS-Script-End-------------------------");
    bufRead.close();
    inputFile.close();
  }

  static void dumpBatch2Log(String batchExecuteSPSS) throws FileNotFoundException, IOException {
    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
    FileReader inputFile = new FileReader(batchExecuteSPSS);
    BufferedReader bufRead = new BufferedReader(inputFile);
    String lineString = "";
    writeLogFile(methodName + "()," + "---------------------------Batch-Script-DUMP-------------------------");
    try {
      while ((lineString = bufRead.readLine()) != null) {
        writeLogFile(lineString);
      }
    } catch (FileNotFoundException e) {
      Logger.getLogger(ReadWriteFileManagement.class.getName()).log(Level.SEVERE, null, e);
      WriteLog.writeLogFile(methodName + "()," + e.getMessage() + "\n" + e.getCause());
    }
    writeLogFile(methodName + "()," + "---------------------------Batch-Script-End-------------------------");
    bufRead.close();
    inputFile.close();
  }
}
