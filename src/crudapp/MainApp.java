package crudapp;

import Forms.MahasiswaForm;
import Forms.MataKuliahForm;
import Forms.NilaiForm;
import java.sql.*;
import javax.swing.*;
import crudapp.Koneksi;
import Forms.*;

public class MainApp {

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Menu CRUD Data");
        JButton btnMahasiswa = new JButton("Form Mahasiswa");
        JButton btnMatkul = new JButton("Form Mata Kuliah");
        JButton btnNilai = new JButton("Form Nilai");

        btnMahasiswa.addActionListener(e -> new MahasiswaForm());
        btnMatkul.addActionListener(e -> new MataKuliahForm());
        btnNilai.addActionListener(e -> new NilaiForm());

        JPanel panel = new JPanel();
        panel.add(btnMahasiswa);
        panel.add(btnMatkul);
        panel.add(btnNilai);

        frame.add(panel);
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}