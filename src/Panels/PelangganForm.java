package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import main.Koneksi;

public class PelangganForm extends JFrame {

    private JTextField txtNama, txtTelepon, txtJalan, txtNomor;
    private JButton btnSimpan, btnClear, btnHapus;

    private JTable table;
    private DefaultTableModel tableModel;
    private int editingRow = -1;

    public PelangganForm() {
        setTitle("Manajemen Data Pelanggan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);
        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;

        txtNama    = new JTextField(15);
        txtTelepon = new JTextField(15);
        txtJalan   = new JTextField(15);
        txtNomor   = new JTextField(5);

        String[] labels = { "Nama", "No. Telepon", "Jalan", "No Rumah" };
        JTextField[] fields = { txtNama, txtTelepon, txtJalan, txtNomor };

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i;
            formPanel.add(new JLabel(labels[i] + ":"), gc);
            gc.gridx = 1;
            formPanel.add(fields[i], gc);
        }

        btnSimpan = new JButton("Simpan / Perbarui");
        btnClear  = new JButton("Bersihkan");
        btnHapus  = new JButton("Hapus");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSimpan);
        btnPanel.add(btnClear);
        btnPanel.add(btnHapus);

        gc.gridx = 0; gc.gridy = labels.length; gc.gridwidth = 2;
        formPanel.add(btnPanel, gc);

        String[] columnNames = { "Nama", "Telepon", "Jalan", "No Rumah", "ID" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        table = new JTable(tableModel);
        table.removeColumn(table.getColumnModel().getColumn(4));
        JScrollPane scrollPane = new JScrollPane(table);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, scrollPane);
        splitPane.setResizeWeight(0.35);
        add(splitPane);

        btnSimpan.addActionListener(e -> saveOrUpdate());
        btnClear.addActionListener(e -> clearForm());
        btnHapus.addActionListener(e -> deleteSelected());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection con = Koneksi.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_pelanggan,nama,no_telp,jalan,no_rumah FROM Pelanggan")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("nama"),
                        rs.getString("no_telp"),
                        rs.getString("jalan"),
                        rs.getString("no_rumah"),
                        rs.getInt("id_pelanggan")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"Gagal load data:\n"+ex.getMessage());
        }
    }

    private void saveOrUpdate() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) { JOptionPane.showMessageDialog(this,"Nama wajib diisi"); return; }
        String telp = txtTelepon.getText().trim();
        String jalan= txtJalan.getText().trim();
        String nomor= txtNomor.getText().trim();

        try (Connection con = Koneksi.getConnection()) {
            if (editingRow == -1) {
                String sql = "INSERT INTO Pelanggan(nama,no_telp,jalan,no_rumah) OUTPUT INSERTED.id_pelanggan VALUES (?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1,nama); ps.setString(2,telp); ps.setString(3,jalan); ps.setString(4,nomor);
                    try(ResultSet rs = ps.executeQuery()){
                        rs.next(); int id = rs.getInt(1);
                        tableModel.addRow(new Object[]{nama,telp,jalan,nomor,id});
                    }
                }
            } else {
                int id = (int) tableModel.getValueAt(editingRow,4);
                String sql = "UPDATE Pelanggan SET nama=?,no_telp=?,jalan=?,no_rumah=? WHERE id_pelanggan=?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1,nama); ps.setString(2,telp); ps.setString(3,jalan); ps.setString(4,nomor); ps.setInt(5,id);
                    ps.executeUpdate();
                    for(int c=0;c<4;c++) tableModel.setValueAt(new Object[]{nama,telp,jalan,nomor}[c], editingRow, c);
                }
            }
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"Gagal simpan:\n"+ex.getMessage());
        }
    }

    private void deleteSelected(){
        int row = table.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"Pilih baris dahulu"); return; }
        int confirm = JOptionPane.showConfirmDialog(this,"Yakin hapus?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if(confirm!=JOptionPane.YES_OPTION) return;
        int id = (int) tableModel.getValueAt(row,4);
        try (Connection con = Koneksi.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Pelanggan WHERE id_pelanggan=?")) {
            ps.setInt(1,id); ps.executeUpdate();
            tableModel.removeRow(row); clearForm();
        } catch (Exception ex){ JOptionPane.showMessageDialog(this,"Gagal hapus:\n"+ex.getMessage()); }
    }

    private void fillFormFromTable(){
        int row = table.getSelectedRow();
        if(row>-1){ editingRow=row;
            txtNama.setText(tableModel.getValueAt(row,0).toString());
            txtTelepon.setText(tableModel.getValueAt(row,1).toString());
            txtJalan.setText(tableModel.getValueAt(row,2).toString());
            txtNomor.setText(tableModel.getValueAt(row,3).toString());
            btnSimpan.setText("Perbarui");
        }
    }

    private void clearForm() {
        txtNama.setText(""); txtTelepon.setText(""); txtJalan.setText(""); txtNomor.setText("");
        table.clearSelection(); editingRow=-1; btnSimpan.setText("Simpan");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PelangganForm().setVisible(true));
    }
}