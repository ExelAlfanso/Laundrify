package Panels;
import javax.swing.*;
import java.awt.*;
class PelangganForm extends JFrame {
    public PelangganForm() {
        setTitle("Form Pelanggan");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNama = new JTextField();
        JTextField txtTelepon = new JTextField();
        JTextField txtAlamat = new JTextField();

        formPanel.add(new JLabel("Nama:"));
        formPanel.add(txtNama);

        formPanel.add(new JLabel("No. Telepon:"));
        formPanel.add(txtTelepon);

        formPanel.add(new JLabel("Alamat:"));
        formPanel.add(txtAlamat);

        JButton btnSimpan = new JButton("Simpan");
        formPanel.add(new JLabel()); // empty cell
        formPanel.add(btnSimpan);

        add(formPanel);
    }
}
