import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import main.Koneksi;

public class DataNilaiViewer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Data Nilai Mahasiswa");
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JComboBox<String> cbMataKuliah = new JComboBox<>();

        model.addColumn("NIM");
        model.addColumn("Nama");
        model.addColumn("Mata Kuliah");
        model.addColumn("Nilai");

        try {
            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            String sql = "SELECT m.NIM, m.Nama, mk.Nama_Mata_Kuliah, n.Nilai " +
                         "FROM Nilai n " +
                         "JOIN Mahasiswa m ON n.NIM = m.NIM " +
                         "JOIN Mata_Kuliah mk ON n.Kode_Mata_Kuliah = mk.Kode_Mata_Kuliah";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("NIM"),
                    rs.getString("Nama"),
                    rs.getString("Nama_Mata_Kuliah"),
                    rs.getFloat("Nilai")
                });
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frame.add(new JScrollPane(table));
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
