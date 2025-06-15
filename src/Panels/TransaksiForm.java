package Panels;

import javax.swing.*;
import java.awt.*;

class TransaksiForm extends JFrame {
    public TransaksiForm() {
        setTitle("Form Transaksi");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("ID Pelanggan:"));
        add(new JTextField());

        add(new JLabel("Jenis Layanan:"));
        add(new JTextField());

        add(new JLabel("Berat (kg):"));
        add(new JTextField());

        add(new JButton("Proses"));
    }
}
