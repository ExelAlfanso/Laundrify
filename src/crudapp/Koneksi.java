package crudapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    private static final String NAMA_SERVER = "LAPTOP-O3O7I0JS\\SQLEXPRESS02";
    private static final String NAMA_DATABASE = "modul12";
    private static final String USERNAME = "sa"; // Ganti dengan username Anda
    private static final String PASSWORD = "password123"; // Ganti dengan password Anda

    private static final String CONNECTION_URL_SQL_AUTH =
            String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=true;",
                          NAMA_SERVER, NAMA_DATABASE, USERNAME, PASSWORD);

    public static Connection getKoneksi() {
        Connection koneksiBaru = null;
        try {
            System.out.println("Mencoba membuat koneksi baru ke database...");

            koneksiBaru = DriverManager.getConnection(CONNECTION_URL_SQL_AUTH);

            System.out.println("Koneksi baru ke database '" + NAMA_DATABASE + "' berhasil dibuat!");

        } catch (SQLException e) {
            System.err.println("Pembuatan koneksi baru ke database gagal!");
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());
            e.printStackTrace();
            return null;
        }
        return koneksiBaru;
    }

    public static void tutupKoneksi(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Koneksi spesifik telah ditutup.");
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi spesifik: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Connection conn = Koneksi.getKoneksi();
        if (conn != null) {
            System.out.println("Pengujian koneksi dari Koneksi.main() berhasil.");
            Koneksi.tutupKoneksi(conn);
        } else {
            System.out.println("Pengujian koneksi dari Koneksi.main() gagal.");
        }
    }
}
