package Panels;

import javax.swing.*;
import java.awt.*;

public class DetailTransaksiForm extends JFrame {
    public DetailTransaksiForm() {
        setTitle("Input Detail Transaksi");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> comboLayanan = new JComboBox<>(new String[]{"Cuci Kering", "Setrika", "Cuci Basah"}); // contoh
        JTextField txtQty = new JTextField();
        JTextField txtSubtotal = new JTextField();

        panel.add(new JLabel("Layanan:"));
        panel.add(comboLayanan);

        panel.add(new JLabel("Qty:"));
        panel.add(txtQty);

        panel.add(new JLabel("Subtotal:"));
        panel.add(txtSubtotal);

        panel.add(new JLabel());
        JButton btnSimpan = new JButton("Simpan");
        panel.add(btnSimpan);

        add(panel);
    }
}
