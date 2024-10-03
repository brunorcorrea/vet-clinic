package org.example.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    private JPanel mainPanel;
    private JButton veterinariosButton;
    private JButton agendamentosButton;
    private JButton pacientesButton;
    private JButton estoqueButton;
    private JButton receitasMedicasButton;
    private JButton proprietariosButton;
    private JButton faturamentosButton;
    private JButton sairButton;

    public MainView() {
        veterinariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame veterinarioFrame = new JFrame("Vet Clinic - Veterinários");
                veterinarioFrame.setContentPane(new VeterinarioView().getMainPanel());
                veterinarioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                veterinarioFrame.pack();
                veterinarioFrame.setLocationRelativeTo(null);
                veterinarioFrame.setVisible(true);
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
