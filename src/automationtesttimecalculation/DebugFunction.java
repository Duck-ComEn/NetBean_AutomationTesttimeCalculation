/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automationtesttimecalculation;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author PKaml7162859
 */
public class DebugFunction {
//  public static void main (String[] args){

  private static ResultSet resultSet = null;
  private static Connection conn = null;
  private static Statement statement = null;

  public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException, Exception {
    
  }

  public String acheckAndGetProductStream(String location, String product) {
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
//          System.out.println("File " + fileName);
          StreamFileName = fileName;
          found = true;
        }
      }
    }
    if (found) {
      return StreamFileName;
    }
    return "File Not Found";
  }
}
