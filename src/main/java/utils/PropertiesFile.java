package utils;

import org.apache.commons.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;

import static constants.Constants.PROJECT_PATH;
import static reports.TestReporter.log4j;

public class PropertiesFile {
    private static Properties properties;
    private static FileInputStream fileIn;
    private static FileOutputStream fileOut;
    //Lấy đường dẫn đến project hiện tại
    public static String projectPath = PROJECT_PATH + "/";
    //Tạo đường dẫn đến file configs.properties mặc định
    private static String propertiesFilePathRoot = "src/test/resources/config/config.properties";

    static {
        properties = new Properties();
        try {
            //Khởi tạo giá trị cho đối tượng của class FileInputStream
            fileIn = new FileInputStream(projectPath + propertiesFilePathRoot);
            //Load properties file
            properties.load(fileIn);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
            System.out.println(exp.getCause());
            exp.printStackTrace();
        }
    }

    public static String getPropValue(String KeyProp) {
        String value = null;
        try {
            //get values from properties file
            value = properties.getProperty(KeyProp);
            System.out.println(value);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
            System.out.println(exp.getCause());
            exp.printStackTrace();
        }
        return value;
    }

    //Xây dựng hàm Set Value với Key tương ứng vào trong file properties đã setup bên trên
    public static void setPropValue(String KeyProp, String Value) {
        try {
            //Khởi tạo giá trị cho đối tượng của class FileOutputStream
            fileOut = new FileOutputStream(projectPath + propertiesFilePathRoot);
            //Load properties file hiện tại và thực hiện mapping value với key tương ứng
            properties.setProperty(KeyProp, Value);
            //Lưu key và value vào properties file
            properties.store(fileOut, "Set new value in properties file");
            System.out.println("Set new value in file properties success.");
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
            System.out.println(exp.getCause());
            exp.printStackTrace();
        }
    }

}
