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
    private JButton faturasButton;
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
        proprietariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame proprietarioFrame = new JFrame("Vet Clinic - Proprietários");
                proprietarioFrame.setContentPane(new ProprietarioView().getMainPanel());
                proprietarioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                proprietarioFrame.pack();
                proprietarioFrame.setLocationRelativeTo(null);
                proprietarioFrame.setVisible(true);
            }
        });
        faturasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame faturaFrame = new JFrame("Vet Clinic - Faturas");
                faturaFrame.setContentPane(new FaturaView().getMainPanel());
                faturaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                faturaFrame.pack();
                faturaFrame.setLocationRelativeTo(null);
                faturaFrame.setVisible(true);
            }
        });
        pacientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame pacienteFrame = new JFrame("Vet Clinic - Pacientes");
                pacienteFrame.setContentPane(new PacienteView().getMainPanel());
                pacienteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pacienteFrame.pack();
                pacienteFrame.setLocationRelativeTo(null);
                pacienteFrame.setVisible(true);
            }
        });
        agendamentosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame agendamentoFrame = new JFrame("Vet Clinic - Agendamentos");
                agendamentoFrame.setContentPane(new AgendamentoView().getMainPanel());
                agendamentoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                agendamentoFrame.pack();
                agendamentoFrame.setLocationRelativeTo(null);
                agendamentoFrame.setVisible(true);
            }
        });
        receitasMedicasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame receitaMedicaFrame = new JFrame("Vet Clinic - Receitas Médicas");
                receitaMedicaFrame.setContentPane(new ReceitaMedicaView().getMainPanel());
                receitaMedicaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                receitaMedicaFrame.pack();
                receitaMedicaFrame.setLocationRelativeTo(null);
                receitaMedicaFrame.setVisible(true);
            }
        });
        estoqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame estoqueFrame = new JFrame("Vet Clinic - Estoque");
                estoqueFrame.setContentPane(new EstoqueView().getMainPanel());
                estoqueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                estoqueFrame.pack();
                estoqueFrame.setLocationRelativeTo(null);
                estoqueFrame.setVisible(true);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
