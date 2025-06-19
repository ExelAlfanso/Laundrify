package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import main.Koneksi;

public class LayananForm extends JFrame {

    private JTextField txtNama, txtSatuan, txtHarga, txtDurasi;
    private JButton btnSimpan, btnClear, btnHapus;

    private JTable table;
    private DefaultTableModel tableModel;
    private int editingRow = -1;

    public LayananForm() {
        setTitle("Manajemen Data Layanan");
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

        txtNama   = new JTextField(15);
        txtSatuan = new JTextField(10);
        txtHarga  = new JTextField(10);
        txtDurasi = new JTextField(5);

        String[] labels = {"Nama Layanan", "Satuan", "Harga (Rp)", "Durasi (jam)"};
        JTextField[] fields = {txtNama, txtSatuan, txtHarga, txtDurasi};

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; 
            gc.gridy = i; 
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
        gc.gridx = 0; 
        gc.gridy = labels.length; 
        gc.gridwidth = 2; 
        formPanel.add(btnPanel, gc);

        String[] cols = {"Nama", "Satuan", "Harga", "Durasi", "ID"};
        tableModel = new DefaultTableModel(cols, 0){ 
            @Override public boolean isCellEditable(int r,int c){ 
                return false;
            } 
        };
        table = new JTable(tableModel);
        table.removeColumn(table.getColumnModel().getColumn(4));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        split.setResizeWeight(0.35);
        add(split);

        btnSimpan.addActionListener(e->saveOrUpdate());
        btnClear.addActionListener(e->clearForm());
        btnHapus.addActionListener(e->deleteSelected());
        table.getSelectionModel().addListSelectionListener(e->{ if(!e.getValueIsAdjusting()) fillForm(); });
    }

    private void loadData(){
        tableModel.setRowCount(0);
        try (Connection con = Koneksi.getConnection(); 
            Statement st = con.createStatement(); 
            ResultSet rs = st.executeQuery("SELECT id_layanan,nama_layanan,satuan,harga,durasi_layanan FROM Layanan")) {
            while(rs.next()){
                tableModel.addRow(new Object[]{rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getInt(5),rs.getInt(1)});
            }
        } catch(Exception ex){ 
            JOptionPane.showMessageDialog(this,"Gagal load:\n"+ex.getMessage()); 
        }
    }

    private void saveOrUpdate(){
        String nama = txtNama.getText().trim();
        if(nama.isEmpty()){ 
            JOptionPane.showMessageDialog(this,"Nama wajib diisi"); 
            return; 
        }
        String satuan=txtSatuan.getText().trim();
        double harga; 
        int durasi;
        try { 
            harga=Double.parseDouble(txtHarga.getText()); 
        } catch(Exception ex) { 
            JOptionPane.showMessageDialog(this,"Harga tidak valid"); 
            return; 
        }
        try { durasi=txtDurasi.getText().isBlank()?0:Integer.parseInt(txtDurasi.getText()); 
        } catch(Exception ex) { 
            JOptionPane.showMessageDialog(this,"Durasi tidak valid"); 
            return; 
        }

        try(Connection con = Koneksi.getConnection()){
            if(editingRow==-1){
                String sql="INSERT INTO Layanan(nama_layanan,satuan,harga,durasi_layanan) OUTPUT INSERTED.id_layanan VALUES(?,?,?,?)";
                try(PreparedStatement ps=con.prepareStatement(sql)){
                    ps.setString(1,nama); 
                    ps.setString(2,satuan); 
                    ps.setDouble(3,harga); 
                    ps.setInt(4,durasi);
                    try(ResultSet rs=ps.executeQuery()) { 
                        rs.next(); int id=rs.getInt(1);
                        tableModel.addRow(new Object[]{nama,satuan,harga,durasi,id});
                    }
                }
            }else{
                int id=(int)tableModel.getValueAt(editingRow,4);
                String sql="UPDATE Layanan SET nama_layanan=?,satuan=?,harga=?,durasi_layanan=? WHERE id_layanan=?";
                try(PreparedStatement ps=con.prepareStatement(sql)){
                    ps.setString(1,nama); 
                    ps.setString(2,satuan); 
                    ps.setDouble(3,harga); 
                    ps.setInt(4,durasi); 
                    ps.setInt(5,id);
                    ps.executeUpdate();
                    tableModel.setValueAt(nama, editingRow,0); 
                    tableModel.setValueAt(satuan,editingRow,1);
                    tableModel.setValueAt(harga,editingRow,2); 
                    tableModel.setValueAt(durasi,editingRow,3);
                }
            }
            clearForm();
        }catch(Exception ex){ JOptionPane.showMessageDialog(this,"Gagal simpan:\n"+ex.getMessage()); }
    }

    private void deleteSelected(){
        int row=table.getSelectedRow(); 
        if(row==-1) { 
            JOptionPane.showMessageDialog(this,"Pilih data!"); 
            return; 
        }
        if(JOptionPane.showConfirmDialog(this,"Hapus layanan?","Konfirmasi",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        int id=(int)tableModel.getValueAt(row,4);
        try(Connection con=Koneksi.getConnection(); PreparedStatement ps=con.prepareStatement("DELETE FROM Layanan WHERE id_layanan=?")){
            ps.setInt(1,id); 
            ps.executeUpdate();
            tableModel.removeRow(row); 
            clearForm();
        }catch(Exception ex){ 
            JOptionPane.showMessageDialog(this,"Gagal hapus:\n"+ex.getMessage()); 
        }
    }

    private void fillForm(){
        int row=table.getSelectedRow(); 
        if(row==-1) return; editingRow=row;
        txtNama.setText(tableModel.getValueAt(row,0).toString());
        txtSatuan.setText(tableModel.getValueAt(row,1).toString());
        txtHarga.setText(tableModel.getValueAt(row,2).toString());
        txtDurasi.setText(tableModel.getValueAt(row,3).toString());
        btnSimpan.setText("Perbarui");
    }

    private void clearForm(){ 
        txtNama.setText(""); 
        txtSatuan.setText(""); 
        txtHarga.setText(""); 
        txtDurasi.setText(""); 
        table.clearSelection(); 
        editingRow=-1; 
        btnSimpan.setText("Simpan"); 
    }

    public static void main(String[] args){ 
        SwingUtilities.invokeLater(() -> new LayananForm().setVisible(true)); 
    }
}