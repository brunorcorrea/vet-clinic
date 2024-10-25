package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.AgendamentoViewController;
import org.example.model.Agendamento;
import org.example.model.Paciente;
import org.example.model.StatusAgendamento;
import org.example.model.Veterinario;
import org.example.view.tablemodels.AgendamentoTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoView {
    private final AgendamentoViewController viewController = new AgendamentoViewController();
    private JPanel mainPanel;
    private JTable agendamentoTable;
    private JComboBox<String> pacienteComboBox;
    private JComboBox<String> veterinarioComboBox;
    private JTextField servicoTextField;
    private JComboBox<String> statusComboBox;
    private JButton adicionarAgendamentoButton;
    private JButton removerAgendamentoButton;
    private DateTimePicker dataHoraDateTimePicker;

    private List<Paciente> pacientes = new ArrayList<>();
    private List<Veterinario> veterinarios = new ArrayList<>();

    public AgendamentoView() {
        initializeComponents();
        configureListeners();
        buscarAgendamentos();
    }

    private void initializeComponents() {
        dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        for (StatusAgendamento status : StatusAgendamento.values()) {
            statusComboBox.addItem(status.getDescricao());
        }

        try {
            pacientes = viewController.listarPacientes();
            pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));
        } catch (Exception e) {
            handleException("Erro ao listar pacientes", e);
        }

        try {
            veterinarios = viewController.listarVeterinarios();
            veterinarios.forEach(veterinario -> veterinarioComboBox.addItem(veterinario.getNome()));
        } catch (Exception e) {
            handleException("Erro ao listar veterinários", e);
        }
    }

    private void configureListeners() {
        adicionarAgendamentoButton.addActionListener(this::adicionarAgendamento);
        removerAgendamentoButton.addActionListener(this::removerAgendamento);
    }

    private void adicionarAgendamento(ActionEvent e) {
        try {
            Paciente paciente = pacientes.get(pacienteComboBox.getSelectedIndex());
            Veterinario veterinario = veterinarios.get(veterinarioComboBox.getSelectedIndex());
            String servico = servicoTextField.getText().trim();
            StatusAgendamento status = StatusAgendamento.fromDescricao((String) statusComboBox.getSelectedItem());
            LocalDateTime dataHora = dataHoraDateTimePicker.getDateTimePermissive();

            validateInputs(paciente, veterinario, servico, status, dataHora);

            viewController.adicionarAgendamento(paciente, veterinario, servico, status, dataHora);
            servicoTextField.setText("");
            dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());
            JOptionPane.showMessageDialog(null, "Agendamento adicionado com sucesso!");
            buscarAgendamentos();
        } catch (Exception ex) {
            handleException("Erro ao adicionar agendamento", ex);
        }
    }

    private void removerAgendamento(ActionEvent e) {
        int[] selectedRows = agendamentoTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um agendamento!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) agendamento(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int agendamentoId = (Integer) agendamentoTable.getValueAt(i, 0);
                    viewController.removerAgendamento(agendamentoId);
                } catch (Exception ex) {
                    handleException("Erro ao remover agendamento", ex);
                }
            }
            JOptionPane.showMessageDialog(null, "Agendamento(s) removidos(s) com sucesso!");
            buscarAgendamentos();
        }
    }

    private void buscarAgendamentos() {
        try {
            List<Agendamento> agendamentos = viewController.listarAgendamentos();
            agendamentoTable.setModel(new AgendamentoTableModel(agendamentos));
        } catch (Exception e) {
            handleException("Erro ao listar agendamentos", e);
        }
    }

    private void validateInputs(Paciente paciente, Veterinario veterinario, String servico, StatusAgendamento status, LocalDateTime dataHora) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente inválido!");
        }
        if (veterinario == null) {
            throw new IllegalArgumentException("Veterinário inválido!");
        }
        if (servico.isBlank()) {
            throw new IllegalArgumentException("Serviço inválido!");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status inválido!");
        }
        if (dataHora == null) {
            throw new IllegalArgumentException("Data e hora inválidas!");
        }
        if (dataHora.isBefore(LocalDateTime.now())) {
            int response = JOptionPane.showConfirmDialog(null, "Data e hora estão no passado. Deseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.NO_OPTION) {
                throw new IllegalArgumentException("Data e hora no passado não permitida!");
            }
        }
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}