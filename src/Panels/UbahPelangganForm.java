package Panels;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import main.Koneksi;

class UbahPelangganForm extends JFrame {
    private JComboBox<String> comboPelanggan;

    public UbahPelangganForm() {
        setTitle("Form Ubah Pelanggan");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboPelanggan = new JComboBox<>();
        loadPelanggan();

        JTextField txtNama = new JTextField();
        JTextField txtTelepon = new JTextField();
        JTextField txtAlamat = new JTextField();

        comboPelanggan.addActionListener(e -> {
            // Dummy fill on selection change
            String selected = (String) comboPelanggan.getSelectedItem();
            txtNama.setText(selected);
            txtTelepon.setText("08xxxxxx");
            txtAlamat.setText("Alamat " + selected);
        });

        formPanel.add(new JLabel("Pilih Pelanggan:"));
        formPanel.add(comboPelanggan);

        formPanel.add(new JLabel("Nama:"));
        formPanel.add(txtNama);

        formPanel.add(new JLabel("No. Telepon:"));
        formPanel.add(txtTelepon);

        formPanel.add(new JLabel("Alamat:"));
        formPanel.add(txtAlamat);

        JButton btnUpdate = new JButton("Update");
        formPanel.add(new JLabel());
        formPanel.add(btnUpdate);

        add(formPanel);
    }
    
private void loadPelanggan() {
    try {
        Connection conn = Koneksi.getKoneksi();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nama FROM Pelanggan");
        while (rs.next()) {
            comboPelanggan.addItem(rs.getString("nama"));
        }
        rs.close(); stmt.close(); Koneksi.tutupKoneksi(conn);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data pelanggan", "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
}