package Panels;

import javax.swing.*;
import java.awt.*;

public class ItemTambahanForm extends JFrame {
    public ItemTambahanForm() {
        setTitle("Input Item Tambahan");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNamaItem = new JTextField();
        JTextField txtJumlah = new JTextField();
        JTextField txtHargaSatuan = new JTextField();
        JTextField txtSubtotal = new JTextField();

        panel.add(new JLabel("Nama Item:"));
        panel.add(txtNamaItem);

        panel.add(new JLabel("Jumlah:"));
        panel.add(txtJumlah);

        panel.add(new JLabel("Harga Satuan:"));
        panel.add(txtHargaSatuan);

        panel.add(new JLabel("Subtotal:"));
        panel.add(txtSubtotal);

        panel.add(new JLabel());
        JButton btnSimpan = new JButton("Simpan");
        panel.add(btnSimpan);

        add(panel);
    }
}