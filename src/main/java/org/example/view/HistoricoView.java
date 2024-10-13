package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.HistoricoController;
import org.example.controller.PacienteController;
import org.example.model.Historico;
import org.example.model.Paciente;
import org.example.view.tablemodels.HistoricoTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoView {
    private final HistoricoController historicoController = HistoricoController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();

    private JPanel mainPanel;
    private JTable historicoTable;
    private JComboBox pacienteComboBox;
    private JTextArea vacinasTextArea;
    private JTextArea doencasTextArea;
    private JTextField pesoTextField;
    private JButton adicionarAoHistoricoButton;
    private JButton removerDoHistoricoButton;
    private DateTimePicker dataHoraDateTimePicker;

    private List<Paciente> pacientes = new ArrayList<>();

    public HistoricoView() {
        dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        try {
            pacientes = pacienteController.listarPacientes();
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar pacientes: " + e.getMessage());
        }

        pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));

        adicionarAoHistoricoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        removerDoHistoricoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        List<Historico> historicos;
        try {
            historicos = historicoController.listarHistoricos();
        } catch (Exception e) {
            historicos = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar históricos: " + e.getMessage());
        }

        historicoTable.setModel(new HistoricoTableModel(historicos));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
