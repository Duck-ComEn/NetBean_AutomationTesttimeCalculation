package automationtesttimecalculation;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 * @author Witsawa Namwongs ID7165990
 */
public class AutomationTesttimeCalculation {

  public static void main(String[] args) throws IOException, InterruptedException, ParseException {

// run TesttimeDataManagementGUI
    TesttimeDataManagementGUI gui = new TesttimeDataManagementGUI();
//Show GUI
    gui.setVisible(true);
    gui.setTitle("Test time data Management program");
    gui.setDefaultCloseOperation(gui.EXIT_ON_CLOSE);

//Read Configuration file
    gui.getLabel_Status().setText("Loading Configuration...");
    ConfigurationFile config = new ConfigurationFile();
    config.LoadConfiguration(gui);

// set Main Parameter.
    String AllProduct = config.getProducts();
    String[] Products = AllProduct.split(",");
    DateManagement dateManagement = new DateManagement();
    ReadWriteFileManagement readFileManagement = new ReadWriteFileManagement();
    runBatchScriptMySQL runBatchScript = new runBatchScriptMySQL();
    ScriptParam scriptParam = new ScriptParam();
    String endDateOfWeek_Of_lastExecuteDate = "";
    String upDateLatestDate = "";
    String StreamPath = "";
    int endWeekDate_Now, endWeekDate_lastExecute = 0;
    int round = 1;
    String workWeek = "";

    gui.getLabel_Status().setText("calculate EndWeek");

    endWeekDate_Now = Integer.parseInt(dateManagement.findEndWeekDateBy_IntDate(dateManagement.curDate()));
    endWeekDate_lastExecute = Integer.parseInt(dateManagement.findEndWeekDateBy_IntDate(dateManagement.latDate(config.getDatePath())));
    round = 1;
    while (endWeekDate_Now > endWeekDate_lastExecute) {
      String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
      WriteLog.writeLogFile(methodName +"()," + "Perform execute with endWeekDate_lastExecute:" + endWeekDate_lastExecute);
      for (String product : Products) {
        gui.getLabel_Status().setText(product + ":: Finding Stream");
        WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
        // finding Stream and calculate date.
        StreamPath = readFileManagement.checkAndGetProductStream(config.getMainDIR(), product);
        scriptParam.setStreamPath(StreamPath.replace("\\", "\\\\"));
        if (scriptParam.getStreamPath().equals("File Not Found")) {
          WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText() + ": Stream not found");
          //JOptionPane jopFileNotFound = new JOptionPane("Not found Stream for product " +product,JOptionPane.ERROR_MESSAGE);
          continue; // next product.
        }
        gui.getLabel_Status().setText(product + "::Found steam: "+scriptParam.getStreamPath());
        WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
        
        gui.getLabel_Status().setText(product + "::R" + round + ":: Step 1 Calculate Parameter and write Script");
        WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());

        scriptParam.setProduct(product);
        scriptParam.setResultAllPath(config.getResultAll().replace("\\", "\\\\"));
        scriptParam.setResultByTestCodePath(config.getResultByTestCode().replace("\\", "\\\\"));
        endDateOfWeek_Of_lastExecuteDate = dateManagement.findEndWeekDateBy_IntDate(dateManagement.latDate(config.getDatePath()));
        scriptParam.setFromEndDate2Week(dateManagement.calculatePreviousStartDate2Week(endDateOfWeek_Of_lastExecuteDate));
        scriptParam.setFromEndDate(dateManagement.calculatePreviousStartDate1Week(endDateOfWeek_Of_lastExecuteDate));
        scriptParam.setEndDate(dateManagement.calculateCurrentEndDate(endDateOfWeek_Of_lastExecuteDate));
        workWeek = dateManagement.getCalendar_WDWorkWeek(readFileManagement, config.getCalendarPath(), scriptParam.getFromEndDate().substring(0, 10).replaceAll("-", ""));
        scriptParam.setWorkWeek(workWeek);

//        gui.getLabel_Status().setText(scriptParam.getFromEndDate2Week());
//        gui.getLabel_Status().setText(scriptParam.getFromEndDate());
//        gui.getLabel_Status().setText(scriptParam.getEndDate());
//        gui.getLabel_Status().setText(scriptParam.getWorkWeek());
        readFileManagement.writeScript(scriptParam, config);
        WriteLog.dumpScript2Log(config.getExecuteSPSS_Script());
        readFileManagement.writeBatch(config);
        WriteLog.dumpBatch2Log(config.getBatchExecuteSPSS());
        
        gui.getLabel_Status().setText(product + "::R" + round + ":: Pre_Step 2 Delete result ");
        WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
        readFileManagement.deleteFile(config.getResultAll());
        runBatchScript.runThreadWaitCSV(1000);

        gui.getLabel_Status().setText(product + "::R" + round + ":: Step 2 Execute Batch script");
        WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
        System.out.println(runBatchScript.clickBatchFile(config, gui));

        gui.getLabel_Status().setText(product + "::R" + round + ":: Step 3 Check Result");
        WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
        String result = runBatchScript.checkCSVExist(config.getResultAll());
        if (result.equals("Result doesn't exit")) {
          //Write log.
          WriteLog.writeLogFile(methodName +"()," + "Batch file execute timeout or Result not exit.");
          continue; // nexe product.
        }

        gui.getLabel_Status().setText(product + "::R" + round + ":: Step 4 Import result to Database");
        WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
        runBatchScript.addToDatabase(config);
        runBatchScript.runThreadWaitCSV(1000);

        gui.getLabel_Status().setText(product + "::R" + round + ":: Step 5 Delete result ");
        WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
//        readFileManagement.deleteFile(config.getExecuteSPSS_Script());
//        runBatchScript.runThreadWaitCSV(1000);
        readFileManagement.deleteFile(config.getResultAll());
        runBatchScript.runThreadWaitCSV(1000);
//        readFileManagement.deleteFile(config.getResultByTestCode());
//        runBatchScript.runThreadWaitCSV(1000);
      }

      gui.getLabel_Status().setText("__AllProduct__::R" + round + ":: Finish week :" + workWeek + " then update execute date");
      WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
      endWeekDate_Now = Integer.parseInt(dateManagement.findEndWeekDateBy_IntDate(dateManagement.curDate()));
       WriteLog.writeLogFile(methodName +"()," + "Exc_EndWeekDate:" + endDateOfWeek_Of_lastExecuteDate +  " Now_EndWeekDate:" + endWeekDate_Now );
      if (Integer.parseInt(endDateOfWeek_Of_lastExecuteDate) < endWeekDate_Now) {
        WriteLog.writeLogFile(methodName +"()," + "last execute is previous week then save endLastWeekDate");
        upDateLatestDate = endDateOfWeek_Of_lastExecuteDate;
      } else {
        upDateLatestDate = dateManagement.curDate();
        WriteLog.writeLogFile(methodName +"()," + "last execute is same with this week then save currentDate");
      }
      WriteLog.writeLogFile(methodName +"()," + "::R "+round+ " PrepareNext round :Save last executeDate:"+upDateLatestDate );
      readFileManagement.writeDateFile(config.getDatePath(), upDateLatestDate);

//        
      gui.getLabel_Status().setText("__AllProduct__::R" + round + ":: Week " + workWeek + " execute complete");
      WriteLog.writeLogFile(methodName +"()," + gui.getLabel_Status().getText());
      runBatchScript.runThreadWaitCSV(1000);

      // update lastExecuteDate data
      endWeekDate_lastExecute = Integer.parseInt(dateManagement.findEndWeekDateBy_IntDate(dateManagement.latDate(config.getDatePath())));
      round++;
      runBatchScript.runThreadWaitCSV(2000);
    }

//End by close GUI
    System.out.println("********Process Complete!!!********");
    gui.getLabel_Status().setText("********Process Complete!!!********");
    System.out.println("Application will close in 5 second");
    gui.getLabel_Status().setText("Application will close in 5 second");
    runBatchScript.runThreadWaitCSV(5000);
    gui.dispose();
    System.exit(0);

  }
}
