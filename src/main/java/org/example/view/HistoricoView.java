package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.HistoricoController;
import org.example.controller.PacienteController;
import org.example.model.Historico;
import org.example.model.Paciente;
import org.example.view.tablemodels.HistoricoTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoView {
    private final HistoricoController historicoController = HistoricoController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();

    private JPanel mainPanel;
    private JTable historicoTable;
    private JComboBox<String> pacienteComboBox;
    private JTextArea vacinasTextArea;
    private JTextArea doencasTextArea;
    private JTextField pesoTextField;
    private JButton adicionarAoHistoricoButton;
    private JButton removerDoHistoricoButton;
    private DateTimePicker dataHoraDateTimePicker;
    private JTextArea observacoesTextArea;

    private List<Paciente> pacientes = new ArrayList<>();

    public HistoricoView() {
        initializeComponents();
        configureListeners();
        buscarHistoricos();
    }

    private void initializeComponents() {
        dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        try {
            pacientes = pacienteController.listarPacientes();
            pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));
        } catch (Exception e) {
            handleException("Erro ao listar pacientes", e);
        }
    }

    private void configureListeners() {
        adicionarAoHistoricoButton.addActionListener(this::adicionarAoHistorico);
        removerDoHistoricoButton.addActionListener(this::removerDoHistorico);
    }

    private void adicionarAoHistorico(ActionEvent e) {
        try {
            Paciente paciente = pacientes.get(pacienteComboBox.getSelectedIndex());
            List<String> vacinas = List.of(vacinasTextArea.getText().split("\n"));
            List<String> doencas = List.of(doencasTextArea.getText().split("\n"));
            String peso = pesoTextField.getText().trim();
            List<String> observacoes = List.of(observacoesTextArea.getText().split("\n"));
            LocalDateTime dataHora = dataHoraDateTimePicker.getDateTimePermissive();

            validateInputs(paciente, vacinas, doencas, peso, observacoes, dataHora);

            Historico historico = new Historico();
            historico.setPaciente(paciente);
            historico.setVacinas(vacinas);
            historico.setDoencas(doencas);
            historico.setPeso(peso);
            historico.setDataHora(dataHora);
            historico.setObservacoes(observacoes);

            historicoController.adicionarHistorico(historico);
            clearInputs();
            JOptionPane.showMessageDialog(null, "Histórico adicionado com sucesso!");
            buscarHistoricos();
        } catch (Exception ex) {
            handleException("Erro ao adicionar histórico", ex);
        }
    }

    private void removerDoHistorico(ActionEvent e) {
        int[] selectedRows = historicoTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um histórico!");
            return;
        }
        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) histórico(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    Historico historico = new Historico();
                    historico.setId((Integer) historicoTable.getValueAt(i, 0));
                    historicoController.removerHistorico(historico);
                } catch (Exception ex) {
                    handleException("Erro ao remover histórico", ex);
                }
            }
            JOptionPane.showMessageDialog(null, "Histórico(s) removido(s) com sucesso!");
            buscarHistoricos();
        }
    }

    private void buscarHistoricos() {
        try {
            List<Historico> historicos = historicoController.listarHistoricos();
            historicoTable.setModel(new HistoricoTableModel(historicos));
        } catch (Exception e) {
            handleException("Erro ao listar históricos", e);
        }
    }

    private void validateInputs(Paciente paciente, List<String> vacinas, List<String> doencas, String peso, List<String> observacoes, LocalDateTime dataHora) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente inválido!");
        }
        if (vacinas.isEmpty()) {
            throw new IllegalArgumentException("Vacinas inválidas!");
        }
        if (doencas.isEmpty()) {
            throw new IllegalArgumentException("Doenças inválidas!");
        }
        if (peso.isEmpty()) {
            throw new IllegalArgumentException("Peso inválido!");
        }
        if (observacoes.isEmpty()) {
            throw new IllegalArgumentException("Observações inválidas!");
        }
        if (dataHora == null) {
            throw new IllegalArgumentException("Data e hora inválidas!");
        }
    }

    private void clearInputs() {
        vacinasTextArea.setText("");
        doencasTextArea.setText("");
        pesoTextField.setText("");
        observacoesTextArea.setText("");
        dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}