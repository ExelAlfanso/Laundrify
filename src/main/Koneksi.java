package main;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    private static String server = "LAPTOP-KAVAKS1D";
    private static String database = "sistemLaundry";
    public static String user = "root";
    public static String password = "dina001";
    public static String url = "jdbc:sqlserver://" + server + ":1004;databaseName=" + database + ";encrypt=false";


    public static Connection getConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, user, password);
    }
}