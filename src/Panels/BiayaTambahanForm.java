package Panels;

import javax.swing.*;
import java.awt.*;

public class BiayaTambahanForm extends JFrame {
    public BiayaTambahanForm() {
        setTitle("Input Biaya Tambahan");
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNamaBiaya = new JTextField();
        JTextField txtJumlah = new JTextField();

        panel.add(new JLabel("Nama Biaya:"));
        panel.add(txtNamaBiaya);

        panel.add(new JLabel("Jumlah:"));
        panel.add(txtJumlah);

        panel.add(new JLabel());
        JButton btnSimpan = new JButton("Simpan");
        panel.add(btnSimpan);

        add(panel);
    }
}