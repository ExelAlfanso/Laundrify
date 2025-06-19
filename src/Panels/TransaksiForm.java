// File: Panels/TransaksiForm.java
package Panels;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import main.Koneksi;
import java.util.ArrayList;
import java.util.List;

public class TransaksiForm extends JFrame {

    private final JTextField txtNoNota = new JTextField(8);
    private final JFormattedTextField txtTanggal =
            new JFormattedTextField(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private final JFormattedTextField txtJam =
            new JFormattedTextField(DateTimeFormatter.ofPattern("HH:mm"));
    private final JComboBox<ComboboxItem> cbPelanggan = new JComboBox<>();

    private final DefaultTableModel modelDetail = new DefaultTableModel(
            new String[]{"Layanan", "Qty", "Harga", "Subtotal", "ID"}, 0);
    private final DefaultTableModel modelItem = new DefaultTableModel(
            new String[]{"Nama Item", "Jumlah", "Harga Satuan", "Subtotal"}, 0);
    private final DefaultTableModel modelBiaya = new DefaultTableModel(
            new String[]{"Nama Biaya", "Jumlah"}, 0);

    private final JLabel lblTotal = new JLabel("0.00");
    private final JButton btnSimpanCetak = new JButton("Simpan & Cetak");

    private final List<ComboboxItem> layananList = new ArrayList<>();

    public TransaksiForm() {
        setTitle("Input Transaksi");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initMasterData();
        setContentPane(buildUI());
        initEvents();
        setNowDateTime();
    }

    private void initMasterData() {
        try (Connection con = Koneksi.getConnection(); Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(no_nota),600)+1 FROM Transaksi");
            if (rs.next()) txtNoNota.setText(rs.getString(1));

            rs = st.executeQuery("SELECT id_pelanggan, nama FROM Pelanggan");
            while (rs.next()) cbPelanggan.addItem(new ComboboxItem(rs.getInt(1), rs.getString(2)));

            rs = st.executeQuery("SELECT id_layanan, nama_layanan, harga FROM Layanan");
            while (rs.next()) layananList.add(new ComboboxItem(rs.getInt(1), rs.getString(2), rs.getDouble(3)));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Koneksi DB gagal: " + e.getMessage());
        }
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        root.add(headerPanel(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Detail Layanan", detailTab());
        tabs.addTab("Item Tambahan", itemTab());
        tabs.addTab("Biaya Tambahan", biayaTab());
        root.add(tabs, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 16f));
        south.add(new JLabel("TOTAL:"));
        south.add(lblTotal);
        south.add(btnSimpanCetak);
        root.add(south, BorderLayout.SOUTH);
        return root;
    }

    private JPanel detailTab() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout());
        JComboBox<ComboboxItem> cb = new JComboBox<>(layananList.toArray(new ComboboxItem[0]));
        JTextField qty = new JTextField(3);
        JButton add = new JButton("Tambah");
        form.add(new JLabel("Layanan:")); form.add(cb);
        form.add(new JLabel("Qty:")); form.add(qty);
        form.add(add);

        JTable tbl = new JTable(modelDetail);
        tbl.removeColumn(tbl.getColumnModel().getColumn(4));

        add.addActionListener(e -> {
            ComboboxItem item = (ComboboxItem) cb.getSelectedItem();
            int q = (int) parse(qty.getText());
            if (item != null && q > 0) {
                double sub = item.harga * q;
                modelDetail.addRow(new Object[]{item.text, q, item.harga, sub, item.id});
                qty.setText("");
                hitungTotal();
            }
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(tbl), BorderLayout.CENTER);
        return p;
    }

    private JPanel itemTab() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout());
        JTextField nama = new JTextField(10);
        JTextField jumlah = new JTextField(3);
        JTextField harga = new JTextField(6);
        JButton add = new JButton("Tambah");
        form.add(new JLabel("Item:")); form.add(nama);
        form.add(new JLabel("Jumlah:")); form.add(jumlah);
        form.add(new JLabel("Harga:")); form.add(harga);
        form.add(add);

        JTable tbl = new JTable(modelItem);

        add.addActionListener(e -> {
            String n = nama.getText();
            int j = (int) parse(jumlah.getText());
            double h = parse(harga.getText());
            if (!n.isBlank() && j > 0) {
                double sub = j * h;
                modelItem.addRow(new Object[]{n, j, h, sub});
                nama.setText(""); jumlah.setText(""); harga.setText("");
                hitungTotal();
            }
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(tbl), BorderLayout.CENTER);
        return p;
    }

    private JPanel biayaTab() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout());
        JTextField nama = new JTextField(10);
        JTextField jumlah = new JTextField(6);
        JButton add = new JButton("Tambah");
        form.add(new JLabel("Biaya:")); form.add(nama);
        form.add(new JLabel("Jumlah:")); form.add(jumlah);
        form.add(add);

        JTable tbl = new JTable(modelBiaya);

        add.addActionListener(e -> {
            String n = nama.getText();
            double j = parse(jumlah.getText());
            if (!n.isBlank() && j > 0) {
                modelBiaya.addRow(new Object[]{n, j});
                nama.setText(""); jumlah.setText("");
                hitungTotal();
            }
        });

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(tbl), BorderLayout.CENTER);
        return p;
    }

    private JPanel headerPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; p.add(new JLabel("No Nota:"), g);
        g.gridx = 1; p.add(txtNoNota, g); txtNoNota.setEditable(false);

        g.gridy = 1; g.gridx = 0; p.add(new JLabel("Tanggal:"), g);
        g.gridx = 1; p.add(txtTanggal, g);
        g.gridx = 2; p.add(new JLabel("Jam:"), g);
        g.gridx = 3; p.add(txtJam, g);

        g.gridy = 2; g.gridx = 0; p.add(new JLabel("Pelanggan:"), g);
        g.gridx = 1; g.gridwidth = 3; g.fill = GridBagConstraints.HORIZONTAL;
        p.add(cbPelanggan, g);
        return p;
    }

    private void initEvents() {
        TableModelListener l = e -> hitungTotal();
        modelDetail.addTableModelListener(l);
        modelItem.addTableModelListener(l);
        modelBiaya.addTableModelListener(l);
        btnSimpanCetak.addActionListener(e -> { if (save()) printNota(); });
    }

    private void hitungTotal() {
        double t = 0;
        for (int r = 0; r < modelDetail.getRowCount(); r++) t += parse(modelDetail.getValueAt(r, 3));
        for (int r = 0; r < modelItem.getRowCount(); r++) t += parse(modelItem.getValueAt(r, 3));
        for (int r = 0; r < modelBiaya.getRowCount(); r++) t += parse(modelBiaya.getValueAt(r, 1));
        lblTotal.setText(String.format("%.2f", t));
    }

    private boolean save() {
        try (Connection con = Koneksi.getConnection()) {
            con.setAutoCommit(false);
            String sqlTrx = "INSERT INTO Transaksi (tgl_masuk, jam_masuk, id_pelanggan, total_transaksi) OUTPUT INSERTED.no_nota VALUES (?,?,?,?)";
            LocalDateTime dt = LocalDateTime.parse(txtTanggal.getText() + "T" + txtJam.getText() + ":00");
            int pelangganId = ((ComboboxItem) cbPelanggan.getSelectedItem()).id;
            double total = Double.parseDouble(lblTotal.getText());
            int noNota;

            try (PreparedStatement ps = con.prepareStatement(sqlTrx)) {
                ps.setDate(1, java.sql.Date.valueOf(dt.toLocalDate()));
                ps.setTime(2, java.sql.Time.valueOf(dt.toLocalTime()));
                ps.setInt(3, pelangganId);
                ps.setDouble(4, total);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    noNota = rs.getInt(1);
                }
            }

            String sqlDet = "INSERT INTO DetailTransaksi(no_nota, id_layanan, qty, subtotal) VALUES (?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sqlDet)) {
                for (int r = 0; r < modelDetail.getRowCount(); r++) {
                    ps.setInt(1, noNota);
                    ps.setInt(2, (int) modelDetail.getValueAt(r, 4));
                    ps.setInt(3, (int) modelDetail.getValueAt(r, 1));
                    ps.setDouble(4, (double) modelDetail.getValueAt(r, 3));
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String sqlItem = "INSERT INTO ItemTambahan(no_nota, nama_item, jumlah, harga_satuan, subtotal) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                for (int r = 0; r < modelItem.getRowCount(); r++) {
                    ps.setInt(1, noNota);
                    ps.setString(2, (String) modelItem.getValueAt(r, 0));
                    ps.setInt(3, (int) modelItem.getValueAt(r, 1));
                    ps.setDouble(4, (double) modelItem.getValueAt(r, 2));
                    ps.setDouble(5, (double) modelItem.getValueAt(r, 3));
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String sqlBiaya = "INSERT INTO BiayaTambahan(no_nota, nama_biaya, jumlah) VALUES (?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sqlBiaya)) {
                for (int r = 0; r < modelBiaya.getRowCount(); r++) {
                    ps.setInt(1, noNota);
                    ps.setString(2, (String) modelBiaya.getValueAt(r, 0));
                    ps.setDouble(3, (double) modelBiaya.getValueAt(r, 1));
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            con.commit();
            txtNoNota.setText(String.valueOf(noNota));
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void printNota() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== NOTA =====\nNo Nota : ").append(txtNoNota.getText()).append('\n')
          .append("Tanggal  : ").append(txtTanggal.getText()).append(' ').append(txtJam.getText()).append('\n')
          .append("Pelanggan: ").append(cbPelanggan.getSelectedItem()).append("\n\n");

        sb.append("--- Detail Layanan ---\n");
        for (int r = 0; r < modelDetail.getRowCount(); r++)
            sb.append(modelDetail.getValueAt(r, 0)).append(" x")
              .append(modelDetail.getValueAt(r, 1)).append(" = ")
              .append(modelDetail.getValueAt(r, 3)).append('\n');

        sb.append("\n--- Item Tambahan ---\n");
        for (int r = 0; r < modelItem.getRowCount(); r++)
            sb.append(modelItem.getValueAt(r, 0)).append(" x")
              .append(modelItem.getValueAt(r, 1)).append(" = ")
              .append(modelItem.getValueAt(r, 3)).append('\n');

        sb.append("\n--- Biaya Tambahan ---\n");
        for (int r = 0; r < modelBiaya.getRowCount(); r++)
            sb.append(modelBiaya.getValueAt(r, 0)).append(" : ")
              .append(modelBiaya.getValueAt(r, 1)).append('\n');

        sb.append("\nTOTAL : ").append(lblTotal.getText());

        JOptionPane.showMessageDialog(this, new JTextArea(sb.toString()),
                "Nota " + txtNoNota.getText(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void setNowDateTime() {
        LocalDateTime now = LocalDateTime.now();
        txtTanggal.setText(now.toLocalDate().toString());
        txtJam.setText(now.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    private double parse(Object o) {
        if (o == null || o.toString().isBlank()) return 0;
        try { return Double.parseDouble(o.toString()); } catch (Exception e) { return 0; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransaksiForm().setVisible(true));
    }

    private static class ComboboxItem {
        int id; String text; double harga;
        ComboboxItem(int id, String text) { this(id, text, 0); }
        ComboboxItem(int id, String text, double harga) { this.id = id; this.text = text; this.harga = harga; }
        public String toString() { return text; }
    }
}