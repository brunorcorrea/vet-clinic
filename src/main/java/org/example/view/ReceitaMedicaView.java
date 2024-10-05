package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.PacienteController;
import org.example.controller.ReceitaMedicaController;
import org.example.model.Paciente;
import org.example.model.ReceitaMedica;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceitaMedicaView {
    private final ReceitaMedicaController receitaMedicaController = ReceitaMedicaController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();

    private JPanel mainPanel;
    private JTable receitaMedicaTable;
    private JComboBox pacienteComboBox;
    private JButton adicionarReceitaMedicaButton;
    private JButton removerReceitaMedicaButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private DateTimePicker dataEmissaoDateTimePicker;

    private List<Paciente> pacientes = new ArrayList<>();

    public ReceitaMedicaView() {
        dataEmissaoDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        try {
            pacientes = pacienteController.listarPacientes();
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar pacientes: " + e.getMessage());
        }

        pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));

        adicionarReceitaMedicaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        removerReceitaMedicaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        List<ReceitaMedica> receitaMedicas;
        try {
            receitaMedicas = receitaMedicaController.listarReceitasMedica();
        } catch (Exception e) {
            receitaMedicas = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar receitas médicas: " + e.getMessage());
        }

        receitaMedicaTable.setModel(new AgendamentoTableModel(receitaMedicas));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
