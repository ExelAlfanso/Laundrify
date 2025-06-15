package main;

import Panels.Dashboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Laundrify {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }
}
