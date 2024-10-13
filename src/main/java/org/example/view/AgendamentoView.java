package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.AgendamentoController;
import org.example.controller.PacienteController;
import org.example.controller.VeterinarioController;
import org.example.model.Agendamento;
import org.example.model.Paciente;
import org.example.model.StatusAgendamento;
import org.example.model.Veterinario;
import org.example.view.tablemodels.AgendamentoTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoView {
    private final AgendamentoController agendamentoController = AgendamentoController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();
    private final VeterinarioController veterinarioController = VeterinarioController.getInstance();
    private JPanel mainPanel;
    private JTable agendamentoTable;
    private JComboBox pacienteComboBox;
    private JComboBox veterinarioComboBox;
    private JTextField servicoTextField;
    private JComboBox statusComboBox;
    private JButton adicionarAgendamentoButton;
    private JButton removerAgendamentoButton;
    private DateTimePicker dataHoraDateTimePicker;

    private List<Paciente> pacientes = new ArrayList<>();
    private List<Veterinario> veterinarios = new ArrayList<>();

    public AgendamentoView() {
        dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        statusComboBox.addItem(StatusAgendamento.AGENDADO.getDescricao());
        statusComboBox.addItem(StatusAgendamento.CONCLUIDO.getDescricao());
        statusComboBox.addItem(StatusAgendamento.CANCELADO.getDescricao());

        try {
            pacientes = pacienteController.listarPacientes();
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar pacientes: " + e.getMessage());
        }

        pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));

        try {
            veterinarios = veterinarioController.listarVeterinarios();
        } catch (Exception e) {
            veterinarios = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar veterinários: " + e.getMessage());
        }

        veterinarios.forEach(veterinario -> veterinarioComboBox.addItem(veterinario.getNome()));

        adicionarAgendamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        removerAgendamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        List<Agendamento> agendamentos;
        try {
            agendamentos = agendamentoController.listarAgendamentos();
        } catch (Exception e) {
            agendamentos = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar agendamentos: " + e.getMessage());
        }

        agendamentoTable.setModel(new AgendamentoTableModel(agendamentos));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
