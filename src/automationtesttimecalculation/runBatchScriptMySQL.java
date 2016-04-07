/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automationtesttimecalculation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author WNamw7165990
 */
public class runBatchScriptMySQL {

  public String clickBatchFile(ConfigurationFile config, TesttimeDataManagementGUI gui) throws IOException, InterruptedException {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    File file = new File(config.getBatchExecuteSPSS());
    String sCmd = "cmd.exe /c Start /wait " + file.getPath();
    System.out.println(sCmd);
    Runtime rt = Runtime.getRuntime();
    Process pc = rt.exec(sCmd);
    final long startTime = System.currentTimeMillis();
    boolean timeout = false;
    long duration = System.currentTimeMillis() - startTime;
    while (true) {
      if (this.checkCSVExist(config.getResultAll()).equals("Result exit")) {
        this.runThreadWaitCSV(2000);
        break;
      } else if (duration > 15 * 1000 * 60) { // timout 10 minutes
        timeout = true;
        break;
      }
      duration = (System.currentTimeMillis() - startTime);
      gui.getLabel_excTime().setText(duration / 1000 / 60 + " minute." + (duration/1000)%60 + " sec.");
    }
    // pc.waitFor();
    pc.destroy();
    // kill background process before next step.'
    // kill cmd.exe and process tree for makesure don't have any thred run in backfround process.
    // command : TASKKILL /F /IM cmd.exe /T
    sCmd = "TASKKILL /F /IM cmd.exe /T";
    Runtime.getRuntime().exec(sCmd);
    WriteLog.writeLogFile(methodName +"()," + "Clean cmd.exe");
    // kill clementine process
    sCmd = "TASKKILL /F /IM clemb.exe /T";
    Runtime.getRuntime().exec(sCmd);
    WriteLog.writeLogFile(methodName +"()," + "Clean clemb.exe");
    WriteLog.writeLogFile(methodName +"()," + "Exc:" +duration / 1000 / 60 + " minute." + (duration/1000)%60 + " sec.");
    if (timeout) {
      return "Run Batch File timeout";
    } else {
      return "Run Batch File Complete";
    }

  }

  private static ResultSet resultSet = null;
  private static Connection conn = null;
  private static Statement statement = null;

  /**
   *
   * @param config
   */
  public void addToDatabase(ConfigurationFile config) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    try {
      runThreadWaitCSV(1000);
//      conn =DriverManager.getConnection("jdbc:mysql://"+config.getDBHostName()+ ":3306//" +config.getDBName()+ "?user=" +config.getDBUser()+ "&password=" +config.getDBPass());
      conn = DriverManager.getConnection("jdbc:mysql://" + config.getDBHostName() + ":" + config.getDBPort() + "/" + config.getDBName() + "?user=" + config.getDBUser() + "&password=" + config.getDBPass());
      statement = conn.createStatement();
      resultSet = statement.executeQuery("select * from " + config.getDBTableName());
      writeSQLCommand(resultSet, config);
      System.out.println("connect data base");
      // your prepstatements goes here...
      conn.close();
    } catch (Exception e) {
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
      System.err.println("Got an exception! ");
      System.err.println(e.getCause());
      System.err.println(e.getMessage());
    }

  }

  private void writeSQLCommand(ResultSet resultSet, ConfigurationFile config) throws SQLException {

    //import CSV FILE into database
    String query = " LOAD DATA LOCAL INFILE '" + config.getResultAll().replace("\\", "\\\\") + "'"
            + " IGNORE"
            + " INTO TABLE " + config.getDBTableName()
            + " FIELDS TERMINATED BY \',\' ENCLOSED BY \'\"'"
            + " LINES TERMINATED BY \'\\n\' IGNORE 1 LINES"
            + " ;";
    System.out.println(query);
    resultSet = statement.executeQuery(query);

    // remove duplicate key
//    String sqlDropTestLoad = "DROP TABLE IF EXISTS `TESTLOAD`;";
//    resultSet = statement.executeQuery(sqlDropTestLoad);
//    System.out.println("drop Testload table");
//    
//    String sqlCreateTempTable ="CREATE TABLE `TESTLOAD` (SELECT* FROM "+config.getDBTableName()+" WHERE 1=2)";
//    resultSet = statement.executeQuery(sqlCreateTempTable);
//    System.out.println("create Testload table");
//    
//    String sqlImportCSV2TempDB = "LOAD DATA LOCAL INFILE '" + config.getResultAll().replace("\\", "\\\\") + "'"
//            + " IGNORE"
//            + " INTO TABLE " + config.getDBTableName()
//            + " FIELDS TERMINATED BY \',\' ENCLOSED BY \'\"'"
//            + " LINES TERMINATED BY \'\\n\' IGNORE 1 LINES" 
//            + " ;";
//    resultSet = statement.executeQuery(sqlImportCSV2TempDB);
//    System.out.println("Import csv data to temperary Table");
//    
//    String sqlInsert = "INSERT IGNORE INTO `"+config.getDBTableName()+"`\n SELECT * FROM `TESTLOAD`;";
//    resultSet = statement.executeQuery(sqlInsert);
//    System.out.println("Insert data from temperary table with Ignore dup. to target table");
    System.out.println("Add CSV FILE INTO DATABASE Succeeed");
  }

  private void printDataShow() throws SQLException {

    String Mtype = resultSet.getString("MTYPE");
    String PROC = resultSet.getString("PROC");
    String TT_BD_95 = resultSet.getString("TT_BD_95");
    String TT_BD_99 = resultSet.getString("TT_BD_99");
    String TT_BD_MEAN = resultSet.getString("TT_BD_MEAN");
    String TT_BD_MIN = resultSet.getString("TT_BD_MIN");
    String TT_BD_MAX = resultSet.getString("TT_BD_MAX");
    String TT_BD_SDEV = resultSet.getString("TT_BD_SDEV");
    String TT_BD_COUNT = resultSet.getString("TT_BD_COUNT");

    String TT_BD_95_PASS = resultSet.getString("TT_BD_95_PASS");
    String TT_BD_99_PASS = resultSet.getString("TT_BD_99_PASS");
    String TT_BD_MEAN_PASS = resultSet.getString("TT_BD_MEAN_PASS");
    String TT_BD_MIN_PASS = resultSet.getString("TT_BD_MIN_PASS");
    String TT_BD_MAX_PASS = resultSet.getString("TT_BD_MAX_PASS");
    String TT_BD_SDEV_PASS = resultSet.getString("TT_BD_SDEV_PASS");
    String TT_BD_COUNT_PASS = resultSet.getString("TT_BD_COUNT_PASS");

    String TT_BD_TC_95 = resultSet.getString("TT_BD_TC_95");
    String TT_BD_TC_99 = resultSet.getString("TT_BD_TC_99");
    String TT_BD_TC_MEAN = resultSet.getString("TT_BD_TC_MEAN");
    String TT_BD_TC_MIN = resultSet.getString("TT_BD_TC_MIN");
    String TT_BD_TC_MAX = resultSet.getString("TT_BD_TC_MAX");
    String TT_BD_TC_STDEV = resultSet.getString("TT_BD_TC_STDEV");
    String TT_BD_TC_COUNT = resultSet.getString("TT_BD_TC_COUNT");

    String TESTCODE = resultSet.getString("TESTCODE");
    String TESTTIMEGROUP = resultSet.getString("TESTTIMEGROUP");
    String plan = resultSet.getString("plan");
    String YIELD_BD = resultSet.getString("YIELD_BD");
    String From_End_Date = resultSet.getString("From_End_Date");

    String To_End_Date = resultSet.getString("To_End_Date");
    String Year_Of_Week = resultSet.getString("Year_Of_Week");
    String WW = resultSet.getString("WW");
    String UPDATE_DATE = resultSet.getString("UPDATE_DATE");
    String UNITQTY = resultSet.getString("UNITQTY");
    String YIELDFIRST_PASS = resultSet.getString("YIELDFIRST_PASS");
    String LATEST_YIELD = resultSet.getString("LATEST_YIELD");

    System.out.println(
            "Mtype" + " | " + "PROC" + " | " + "TT_BD_95" + " | " + "TT_BD_99" + " | " + "TT_BD_MEAN"
            + " | " + "TT_BD_MIN" + " | " + "TT_BD_MAX" + " | " + "TT_BD_SDEV" + " | " + "TT_BD_COUNT"
            + " | " + "TT_BD_95_PASS" + " | " + "TT_BD_99_PASS" + " | " + "TT_BD_MEAN_PASS" + " | " + "TT_BD_MIN_PASS"
            + " | " + "TT_BD_MAX_PASS" + " | " + "TT_BD_SDEV_PASS" + " | " + "TT_BD_COUNT_PASS" + " | " + "TT_BD_TC_95"
            + " | " + "TT_BD_TC_99" + " | " + "TT_BD_TC_MEAN" + " | " + "TT_BD_TC_MIN" + " | " + "TT_BD_TC_MAX"
            + " | " + "TT_BD_TC_STDEV" + " | " + "TT_BD_TC_COUNT" + " | " + "TT_BT_95" + " | " + "TT_BT_99"
            + " | " + "TT_BT_MEAN" + " | " + "TT_BT_MIN" + " | " + "TT_BT_MAX" + " | " + "TT_BT_SDEV"
            + " | " + "TT_BT_COUNT" + " | " + "TT_BT_TC_95" + " | " + "TT_BT_TC_99" + " | " + "TT_BT_TC_MEAN"
            + " | " + "TT_BT_TC_MIN" + " | " + "TT_BT_TC_MAX" + " | " + "TT_BT_TC_STDEV" + " | " + "TT_BT_TC_COUNT"
            + " | " + "TESTCODE" + " | " + "TESTTIMEGROUP" + " | " + "plan" + " | " + "YIELD_BD"
            + " | " + "From_End_Date" + " | " + "To_End_Date" + " | " + "Year_Of_Week" + " | " + "WW"
            + " | " + "UPDATE_DATE" + " | " + "UNITQTY" + " | " + "YIELDFIRST_PASS" + " | " + "LATEST_YIELD");

    System.out.println(Mtype + " | " + PROC + " | " + TT_BD_95
            + " | " + TT_BD_99 + " | " + TT_BD_MEAN
            + " | " + TT_BD_MIN + " | " + TT_BD_MAX
            + " | " + TT_BD_SDEV + " | " + TT_BD_COUNT
            + " | " + TT_BD_95_PASS + " | " + TT_BD_99_PASS
            + " | " + TT_BD_MEAN_PASS + " | " + TT_BD_MIN_PASS
            + " | " + TT_BD_MAX_PASS + " | " + TT_BD_SDEV_PASS
            + " | " + TT_BD_COUNT_PASS + " | " + TT_BD_TC_95
            + " | " + TT_BD_TC_99 + " | " + TT_BD_TC_MEAN
            + " | " + TT_BD_TC_MIN + " | " + TT_BD_TC_MAX
            + " | " + TT_BD_TC_STDEV + " | " + TT_BD_TC_COUNT
            + " | " + TESTCODE + " | " + TESTTIMEGROUP
            + " | " + plan + " | " + YIELD_BD
            + " | " + From_End_Date + " | " + To_End_Date
            + " | " + Year_Of_Week + " | " + WW
            + " | " + UPDATE_DATE + " | " + UNITQTY
            + " | " + YIELDFIRST_PASS + " | " + LATEST_YIELD
    );
  }

  public void runThreadWaitCSV(int timeThread) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    try {
      System.out.print("Thread.Sleep : " + timeThread);
      Thread.sleep(timeThread);
      System.out.println("  :  Thread.Sleep Success");
    } catch (InterruptedException e) {
      e.printStackTrace();
      WriteLog.writeLogFile(methodName +"()," + e.getMessage()+"\n"+e.getCause());
    }
  }

  public String checkCSVExist(String csvPath) {
    File file = new File(csvPath);
    while (!file.exists()) {
      runThreadWaitCSV(1000);
      return "Result doesn't exit";
    }
    runThreadWaitCSV(1000);
    return "Result exit";
  }
}
