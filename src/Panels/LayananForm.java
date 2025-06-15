package Panels;

import javax.swing.*;
import java.awt.*;

class LayananForm extends JFrame {
    public LayananForm() {
        setTitle("Form Tambah Layanan");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Nama Layanan:"));
        add(new JTextField());

        add(new JLabel("Harga/kg:"));
        add(new JTextField());

        add(new JButton("Tambah"));
    }
}