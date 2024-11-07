package org.example;

import org.example.dao.DAO;
import org.example.view.MainView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Vet Clinic - O melhor software de gestão de clínicas veterinárias!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new MainView().getMainPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    DAO.closeConnection();
                }
            });
        });
    }
}