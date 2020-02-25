package com.iossocket;

import java.io.*;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        try {
            InputStream inputStream = App.class.getResourceAsStream("/users.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            String pwd = properties.getProperty("user1");
            System.out.println(pwd);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
