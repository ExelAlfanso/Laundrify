package Panels;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    private JPanel panelNavigasi;
    private JPanel panelKonten;

    public Dashboard() {
        setTitle("Laundry Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Judul di atas
        JLabel judul = new JLabel("Laundrify Dashboard", SwingConstants.CENTER);
        judul.setFont(new Font("Arial", Font.BOLD, 24));
        add(judul, BorderLayout.NORTH);

        // Panel Navigasi di kiri
        panelNavigasi = new JPanel();
        panelNavigasi.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblPelanggan = new JLabel("Data Pelanggan:");
        JButton btnDaftarPelanggan = new JButton("Daftar Pelanggan");
        JLabel lblUbahPelanggan = new JLabel("Ubah Pelanggan:");
        JButton btnUbahPelanggan = new JButton("Ubah Pelanggan");
        JLabel lblTransaksi = new JLabel("Manajemen Transaksi:");
        JButton btnTransaksi = new JButton("Transaksi");
        JLabel lblLayanan = new JLabel("Manajemen Layanan:");
        JButton btnLayanan = new JButton("Tambah Layanan");

        panelNavigasi.add(lblPelanggan);
        panelNavigasi.add(btnDaftarPelanggan);
        panelNavigasi.add(lblUbahPelanggan);
        panelNavigasi.add(btnUbahPelanggan);
        panelNavigasi.add(lblTransaksi);
        panelNavigasi.add(btnTransaksi);
        panelNavigasi.add(lblLayanan);
        panelNavigasi.add(btnLayanan);

        // Panel Konten utama di tengah
        panelKonten = new JPanel();
        panelKonten.setLayout(new BorderLayout());

        add(panelNavigasi, BorderLayout.WEST);
        add(panelKonten, BorderLayout.CENTER);

        // Aksi tombol
        btnDaftarPelanggan.addActionListener(e -> new PelangganForm().setVisible(true));
        btnUbahPelanggan.addActionListener(e -> new UbahPelangganForm().setVisible(true));
        btnTransaksi.addActionListener(e -> new TransaksiForm().setVisible(true));
        btnLayanan.addActionListener(e -> new LayananForm().setVisible(true));

        setVisible(true);
    }

    private void setPanel(JPanel panel) {
        panelKonten.removeAll();
        panelKonten.add(panel, BorderLayout.CENTER);
        panelKonten.revalidate();
        panelKonten.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
