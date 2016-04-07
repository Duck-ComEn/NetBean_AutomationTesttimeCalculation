/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automationtesttimecalculation;

/**
 *
 * @author PKaml7162859
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import javax.swing.JOptionPane;

public class ConfigurationFile {

  private String mainDIR;
  private String datePath;
  private String calendarPath;
  private String MassterSPSS_Script;
  private String batchExecuteSPSS;
  private String executeSPSS_Script;
  private String ResultAll;
  private String ResultByTestCode;
  private String products;

  private String batchMaster;
  private String ClementineHostName;
  private String ClementinePortName;
  private String ClementineUsername;
  private String ClementinePassWord;
  private String ClementineLogFile;
  private String ClementineScript;
  private String DBHostName;
  private String DBPort;
  private String DBName;
  private String DBUser;
  private String DBPass;
  private String DBTableName;

  /**
   * @return the datePath
   */
  public String getDatePath() {
    return datePath;
  }

  /**
   * @param datePath the datePath to set
   */
  public void setDatePath(String datePath) {
    this.datePath = datePath;
  }

  /**
   * @return the calendarPath
   */
  public String getCalendarPath() {
    return calendarPath;
  }

  /**
   * @param calendarPath the calendarPath to set
   */
  public void setCalendarPath(String calendarPath) {
    this.calendarPath = calendarPath;
  }

  /**
   * @return the MassterSPSS_Script
   */
  public String getMassterSPSS_Script() {
    return MassterSPSS_Script;
  }

  /**
   * @param MassterSPSS_Script the MassterSPSS_Script to set
   */
  public void setMassterSPSS_Script(String MassterSPSS_Script) {
    this.MassterSPSS_Script = MassterSPSS_Script;
  }

  /**
   * @return the batchExecuteSPSS
   */
  public String getBatchExecuteSPSS() {
    return batchExecuteSPSS;
  }

  /**
   * @param batchExecuteSPSS the batchExecuteSPSS to set
   */
  public void setBatchExecuteSPSS(String batchExecuteSPSS) {
    this.batchExecuteSPSS = batchExecuteSPSS;
  }

  /**
   * @return the executeSPSS_Script
   */
  public String getExecuteSPSS_Script() {
    return executeSPSS_Script;
  }

  /**
   * @param executeSPSS_Script the executeSPSS_Script to set
   */
  public void setExecuteSPSS_Script(String executeSPSS_Script) {
    this.executeSPSS_Script = executeSPSS_Script;
  }

  /**
   * @return the ResultAll
   */
  public String getResultAll() {
    return ResultAll;
  }

  /**
   * @param ResultAll the ResultAll to set
   */
  public void setResultAll(String ResultAll) {
    this.ResultAll = ResultAll;
  }

  /**
   * @return the ResultByTestCode
   */
  public String getResultByTestCode() {
    return ResultByTestCode;
  }

  /**
   * @param ResultByTestCode the ResultByTestCode to set
   */
  public void setResultByTestCode(String ResultByTestCode) {
    this.ResultByTestCode = ResultByTestCode;
  }

  void LoadConfiguration(TesttimeDataManagementGUI gui) throws FileNotFoundException {
    URL location = ConfigurationFile.class.getProtectionDomain().getCodeSource().getLocation();
    String MainDIR = location.getPath().substring(1, location.getPath().length());
    //System.out.println("System path:" + MainDIR);
    //String settingPath = MainDIR + "setting/setting.conf"; // fix file name (use this line for debug.)
    System.out.println(MainDIR);
    String settingPath = "";

    String[] ArrayPath = MainDIR.split("/");
    for (int i = 0; i < ArrayPath.length - 1; i++) {
      if (i == 0) {
        settingPath = settingPath + ArrayPath[i];
      } else {
        settingPath = settingPath + "/" + ArrayPath[i];
      }
    }
    settingPath = settingPath + "/setting/setting.conf";

    double count = 0, countBuffer = 0, countLine = 0;
    String lineNumber = "";
    String filePath = settingPath;
    BufferedReader br;
    String inputSearch = "are";
    String line = "";
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    try {
      br = new BufferedReader(new FileReader(filePath));
      try {
        while ((line = br.readLine()) != null) {
          countLine++;
          //System.out.println(line);
          String[] words = line.split("\"");

          for (String word : words) {
            if (word.equals("MainDIR = ")) {
              gui.getTextField_MainDIR().setText(words[1]);
              this.setMainDIR(gui.getTextField_MainDIR().getText());
            } else if (word.equals("LastExecuteDate = ")) {
              gui.getTextField_LastExecuteDate().setText(gui.getTextField_MainDIR().getText() + words[1]);
              this.setDatePath(gui.getTextField_LastExecuteDate().getText());
            } else if (word.equals("Calendar = ")) {
              gui.getTextField_Calendar().setText(gui.getTextField_MainDIR().getText() + words[1]);
              this.setCalendarPath(gui.getTextField_Calendar().getText());
            } else if (word.equals("MasterScript = ")) {
              gui.getTextField_MasterScript().setText(gui.getTextField_MainDIR().getText() + words[1]);
              this.setMassterSPSS_Script(gui.getTextField_MasterScript().getText());
            } else if (word.equals("ExecuteScript = ")) {
              gui.getTextField_ExecuteScript().setText(gui.getTextField_MainDIR().getText() + words[1]);
              this.setExecuteSPSS_Script(gui.getTextField_ExecuteScript().getText());
            } else if (word.equals("BatchExecuteSPSS = ")) {
              gui.getTextField_BatchExecuteSPSS().setText(gui.getTextField_MainDIR().getText() + words[1]);
              this.setBatchExecuteSPSS(gui.getTextField_BatchExecuteSPSS().getText());
            } else if (word.equals("ResultAll = ")) {
              gui.getTextField_ResultAll().setText(gui.getTextField_MainDIR().getText() + words[1]);
              this.setResultAll(gui.getTextField_ResultAll().getText());
            } else if (word.equals("ResultByTestCode = ")) {
              gui.getTextField_ResultByTestCode().setText(gui.getTextField_MainDIR().getText() + words[1]);
              this.setResultByTestCode(gui.getTextField_ResultByTestCode().getText());
            } else if (word.equals("Product = ")) {
              gui.getTextField_Product().setText(words[1]);
              this.setProducts(gui.getTextField_Product().getText());
            } else if (word.equals("BatchMasterSPSS = ")) {
              gui.getTextField_BatchMasterSPSS().setText(this.getMainDIR() + words[1]);
              this.setBatchMaster(gui.getTextField_BatchMasterSPSS().getText());
            } else if (word.equals("ClementineHostName = ")) {
              gui.getTextField_SPSSHostName().setText(words[1]);
              this.setClementineHostName(gui.getTextField_SPSSHostName().getText());
            } else if (word.equals("ClementinePortName = ")) {
              gui.getTextField_SPSSPortName().setText(words[1]);
              this.setClementinePortName(gui.getTextField_SPSSPortName().getText());
            } else if (word.equals("ClementineUsername = ")) {
              gui.getTextField_SPSSUserName().setText(words[1]);
              this.setClementineUsername(gui.getTextField_SPSSUserName().getText());
            } else if (word.equals("ClementinePassWord = ")) {
              gui.getTextField_SPSSPassword().setText(words[1]);
              this.setClementinePassWord(gui.getTextField_SPSSPassword().getText());
            } else if (word.equals("ClementineLogFile = ")) {
              gui.getTextField_SPSSLogFile().setText(this.getMainDIR() + words[1]);
              this.setClementineLogFile(gui.getTextField_SPSSLogFile().getText());
            } else if (word.equals("ClementineScript = ")) {
              gui.getTextField_SPSSScriptFile().setText(this.getExecuteSPSS_Script());
              this.setClementineScript(gui.getTextField_SPSSScriptFile().getText());
            } else if (word.equals("DBHostName = ")) {
              gui.getTextField_DBHostName().setText(words[1]);
              this.setDBHostName(gui.getTextField_DBHostName().getText());
            } else if (word.equals("DBPort = ")) {
              gui.getTextField_DBPortName().setText(words[1]);
              this.setDBPort(gui.getTextField_DBPortName().getText());
            } else if (word.equals("DBName = ")) {
              gui.getTextField_DBName().setText(words[1]);
              this.setDBName(gui.getTextField_DBName().getText());
            } else if (word.equals("DBUser = ")) {
              gui.getTextField_DBUsername().setText(words[1]);
              this.setDBUser(gui.getTextField_DBUsername().getText());
            } else if (word.equals("DBPass = ")) {
              gui.getTextField_DBPassword().setText(words[1]);
              this.setDBPass(gui.getTextField_DBPassword().getText());
            } else if (word.equals("DBTableName = ")) {
              gui.getTextField_DBTableName().setText(words[1]);
              this.setDBTableName(gui.getTextField_DBTableName().getText());
            }

          }
        }
        br.close();
      } catch (IOException e) {
//            // TODO Auto-generated catch block
        JOptionPane joption = new JOptionPane();
        joption.showMessageDialog(joption, "Read/Write scrip.conf Error", "Config Error", JOptionPane.ERROR_MESSAGE);
        WriteLog.writeLogFile(methodName + "()," + e.getMessage() + "\n" + e.getCause());
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      JOptionPane joption = new JOptionPane();
      joption.showMessageDialog(joption, e.getMessage() + "\n" + e.getCause(), "Config Error", JOptionPane.ERROR_MESSAGE);
      WriteLog.writeLogFile(methodName + "()," + e.getMessage() + "\n" + e.getCause());
      e.printStackTrace();
    }
  }

  /**
   * @return the mainDIR
   */
  public String getMainDIR() {
    return mainDIR;
  }

  /**
   * @param mainDIR the mainDIR to set
   */
  public void setMainDIR(String mainDIR) {
    this.mainDIR = mainDIR;
  }

  /**
   * @return the products
   */
  public String getProducts() {
    return products;
  }

  /**
   * @param products the products to set
   */
  public void setProducts(String products) {
    this.products = products;
  }

  /**
   * @return the batchMaster
   */
  public String getBatchMaster() {
    return batchMaster;
  }

  /**
   * @param batchMaster the batchMaster to set
   */
  public void setBatchMaster(String batchMaster) {
    this.batchMaster = batchMaster;
  }

  /**
   * @return the ClementineHostName
   */
  public String getClementineHostName() {
    return ClementineHostName;
  }

  /**
   * @param ClementineHostName the ClementineHostName to set
   */
  public void setClementineHostName(String ClementineHostName) {
    this.ClementineHostName = ClementineHostName;
  }

  /**
   * @return the ClementinePortName
   */
  public String getClementinePortName() {
    return ClementinePortName;
  }

  /**
   * @param ClementinePortName the ClementinePortName to set
   */
  public void setClementinePortName(String ClementinePortName) {
    this.ClementinePortName = ClementinePortName;
  }

  /**
   * @return the ClementineUsername
   */
  public String getClementineUsername() {
    return ClementineUsername;
  }

  /**
   * @param ClementineUsername the ClementineUsername to set
   */
  public void setClementineUsername(String ClementineUsername) {
    this.ClementineUsername = ClementineUsername;
  }

  /**
   * @return the ClementinePassWord
   */
  public String getClementinePassWord() {
    return ClementinePassWord;
  }

  /**
   * @param ClementinePassWord the ClementinePassWord to set
   */
  public void setClementinePassWord(String ClementinePassWord) {
    this.ClementinePassWord = ClementinePassWord;
  }

  /**
   * @return the ClementineLogFile
   */
  public String getClementineLogFile() {
    return ClementineLogFile;
  }

  /**
   * @param ClementineLogFile the ClementineLogFile to set
   */
  public void setClementineLogFile(String ClementineLogFile) {
    this.ClementineLogFile = ClementineLogFile;
  }

  /**
   * @return the ClementineScript
   */
  public String getClementineScript() {
    return ClementineScript;
  }

  /**
   * @param ClementineScript the ClementineScript to set
   */
  public void setClementineScript(String ClementineScript) {
    this.ClementineScript = ClementineScript;
  }

  /**
   * @return the DBHostName
   */
  public String getDBHostName() {
    return DBHostName;
  }

  /**
   * @param DBHostName the DBHostName to set
   */
  public void setDBHostName(String DBHostName) {
    this.DBHostName = DBHostName;
  }

  /**
   * @return the DBPort
   */
  public String getDBPort() {
    return DBPort;
  }

  /**
   * @param DBPort the DBPort to set
   */
  public void setDBPort(String DBPort) {
    this.DBPort = DBPort;
  }

  /**
   * @return the DBName
   */
  public String getDBName() {
    return DBName;
  }

  /**
   * @param DBName the DBName to set
   */
  public void setDBName(String DBName) {
    this.DBName = DBName;
  }

  /**
   * @return the DBUser
   */
  public String getDBUser() {
    return DBUser;
  }

  /**
   * @param DBUser the DBUser to set
   */
  public void setDBUser(String DBUser) {
    this.DBUser = DBUser;
  }

  /**
   * @return the DBPass
   */
  public String getDBPass() {
    return DBPass;
  }

  /**
   * @param DBPass the DBPass to set
   */
  public void setDBPass(String DBPass) {
    this.DBPass = DBPass;
  }

  /**
   * @return the DBTableName
   */
  public String getDBTableName() {
    return DBTableName;
  }

  /**
   * @param DBTableName the DBTableName to set
   */
  public void setDBTableName(String DBTableName) {
    this.DBTableName = DBTableName;
  }

}
