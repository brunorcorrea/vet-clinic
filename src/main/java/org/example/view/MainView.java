package org.example.view;

import javax.swing.*;

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
    private JButton historicoButton;

    public MainView() {
        veterinariosButton.addActionListener(e -> openFrame("Vet Clinic - Veterinários", new VeterinarioView().getMainPanel()));

        sairButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        proprietariosButton.addActionListener(e -> openFrame("Vet Clinic - Proprietários", new ProprietarioView().getMainPanel()));

        faturasButton.addActionListener(e -> openFrame("Vet Clinic - Faturas", new FaturaView().getMainPanel()));

        pacientesButton.addActionListener(e -> openFrame("Vet Clinic - Pacientes", new PacienteView().getMainPanel()));

        agendamentosButton.addActionListener(e -> openFrame("Vet Clinic - Agendamentos", new AgendamentoView().getMainPanel()));

        receitasMedicasButton.addActionListener(e -> openFrame("Vet Clinic - Receitas Médicas", new ReceitaMedicaView().getMainPanel()));

        estoqueButton.addActionListener(e -> openFrame("Vet Clinic - Estoque", new EstoqueView().getMainPanel()));

        historicoButton.addActionListener(e -> openFrame("Vet Clinic - Histórico", new HistoricoView().getMainPanel()));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void openFrame(String title, JPanel panel) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setSize(800, 600);
    }
}
