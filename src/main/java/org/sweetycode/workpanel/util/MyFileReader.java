package org.sweetycode.workpanel.util;

import java.io.*;
import java.util.Properties;

public class MyFileReader {
    //private static final Logger logger = Logger.getLogger(MyFileReader.class);
    public static String readResources (String propertyFile, String propertyName) {
        Properties properties = new Properties();
        try {
            properties.load(MyFileReader.class.getClassLoader().getResource(propertyFile).openStream());
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            //logger.error("can't read properties: " + propertyFile + ":" + propertyName);
        }
        return null;
    }

    public static String readConf(String propertyFile, String propertyName) {
        String propertyVal = "";
        Properties dbProperty = new Properties();
        try {
            InputStream inputStream = new FileInputStream(propertyFile);
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            dbProperty.load(reader);
            propertyVal = dbProperty.getProperty(propertyName);
            inputStream.close();
        } catch (Exception e) {
            //logger.error("can't read properties: " + propertyFile + ":" + propertyName);
        }
        return propertyVal;
    }

}