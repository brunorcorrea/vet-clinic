package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.AgendamentoController;
import org.example.controller.PacienteController;
import org.example.controller.VeterinarioController;
import org.example.model.*;
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
                Paciente paciente = pacientes.get(pacienteComboBox.getSelectedIndex());
                Veterinario veterinario = veterinarios.get(veterinarioComboBox.getSelectedIndex());
                String servico = servicoTextField.getText() != null ? servicoTextField.getText().trim() : "";
                StatusAgendamento status = StatusAgendamento.fromDescricao((String) statusComboBox.getSelectedItem());
                LocalDateTime dataHora = dataHoraDateTimePicker.getDateTimePermissive();

                if (paciente == null) {
                    JOptionPane.showMessageDialog(null, "Paciente inválido!");
                    return;
                }

                if (veterinario == null) {
                    JOptionPane.showMessageDialog(null, "Veterinário inválido!");
                    return;
                }

                if (servico.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Serviço inválido!");
                    return;
                }

                if (status == null) {
                    JOptionPane.showMessageDialog(null, "Status inválido!");
                    return;
                }

                if (dataHora == null) {
                    JOptionPane.showMessageDialog(null, "Data e hora inválidas!");
                    return;
                }

                if (dataHora.isBefore(LocalDateTime.now())) {
                    int response = JOptionPane.showConfirmDialog(null, "Data e hora estão no passado. Deseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.NO_OPTION) {
                        return;
                    }
                }

                Agendamento agendamento = new Agendamento();
                agendamento.setPaciente(paciente);
                agendamento.setVeterinario(veterinario);
                agendamento.setServico(servico);
                agendamento.setStatus(status);
                agendamento.setDataHora(dataHora);

                try {
                    agendamentoController.adicionarAgendamento(agendamento);
                    JOptionPane.showMessageDialog(null, "Agendamento adicionado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar agendamento: " + ex.getMessage());
                }

                buscarAgendamentos();
            }
        });
        removerAgendamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = agendamentoTable.getSelectedRows();

                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione ao menos um agendamento!");
                    return;
                }

                int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) agendamento(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    for (int i : selectedRows) {
                        Agendamento agendamento = new Agendamento();
                        agendamento.setId((Integer) agendamentoTable.getValueAt(i, 0));

                        try {
                            agendamentoController.removerAgendamento(agendamento);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao remover agendamento: " + ex.getMessage());
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Agendamento(s) removidos(s) com sucesso!");
                }

                buscarAgendamentos();
            }
        });

        buscarAgendamentos();
    }

    private void buscarAgendamentos() {
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
