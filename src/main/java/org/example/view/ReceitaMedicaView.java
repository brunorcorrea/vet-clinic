package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.ReceitaMedicaViewController;
import org.example.model.Paciente;
import org.example.view.tablemodels.ReceitaMedicaTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceitaMedicaView {
    private final ReceitaMedicaViewController viewController = new ReceitaMedicaViewController();

    private JPanel mainPanel;
    private JTable receitaMedicaTable;
    private JComboBox<String> pacienteComboBox;
    private JButton adicionarReceitaMedicaButton;
    private JButton removerReceitaMedicaButton;
    private JTextArea medicamentosTextArea;
    private JTextArea observacoesTextArea;
    private DateTimePicker dataEmissaoDateTimePicker;

    private List<Paciente> pacientes = new ArrayList<>();

    public ReceitaMedicaView() {
        initializeComponents();
        configureListeners();
        loadPacientes();
        buscarReceitasMedicas();
    }

    private void initializeComponents() {
        dataEmissaoDateTimePicker.setDateTimePermissive(LocalDateTime.now());
    }

    private void configureListeners() {
        adicionarReceitaMedicaButton.addActionListener(this::adicionarReceitaMedica);
        removerReceitaMedicaButton.addActionListener(this::removerReceitaMedica);
    }

    private void loadPacientes() {
        try {
            pacientes = viewController.listarPacientes();
            pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            handleException("Erro ao listar pacientes", e);
        }
    }

    private void adicionarReceitaMedica(ActionEvent e) {
        Paciente paciente = pacientes.get(pacienteComboBox.getSelectedIndex());
        List<String> medicamentos = List.of(medicamentosTextArea.getText().split("\n"));
        List<String> observacoes = List.of(observacoesTextArea.getText().split("\n"));
        LocalDateTime dataEmissao = dataEmissaoDateTimePicker.getDateTimePermissive();

        if (!validateInputs(paciente, medicamentos, observacoes, dataEmissao)) return;

        try {
            viewController.adicionarReceitaMedica(paciente, medicamentos, observacoes, dataEmissao);
            clearInputs();
            JOptionPane.showMessageDialog(null, "Receita médica adicionada com sucesso!");
        } catch (Exception ex) {
            handleException("Erro ao adicionar receita médica", ex);
        }

        buscarReceitasMedicas();
    }

    private void removerReceitaMedica(ActionEvent e) {
        int[] selectedRows = receitaMedicaTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos uma receita médica!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover a(s) receita(s) médica(s) selecionada(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int receitaMedicaId = (Integer) receitaMedicaTable.getValueAt(i, 0);
                    viewController.removerReceitaMedica(receitaMedicaId);
                } catch (Exception ex) {
                    handleException("Erro ao remover receita médica", ex);
                }
            }
            JOptionPane.showMessageDialog(null, "Receita(s) médica(s) removida(s) com sucesso!");
            buscarReceitasMedicas();
        }
    }

    private void buscarReceitasMedicas() {
        try {
            ReceitaMedicaTableModel model = viewController.criarReceitaMedicaTableModel();
            receitaMedicaTable.setModel(model);
        } catch (Exception e) {
            handleException("Erro ao listar receitas médicas", e);
        }
    }

    private boolean validateInputs(Paciente paciente, List<String> medicamentos, List<String> observacoes, LocalDateTime dataEmissao) {
        if (paciente == null) {
            JOptionPane.showMessageDialog(null, "Paciente inválido!");
            return false;
        }

        if (medicamentos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Medicamentos inválidos!");
            return false;
        }

        if (observacoes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Observações inválidas!");
            return false;
        }

        if (dataEmissao == null) {
            JOptionPane.showMessageDialog(null, "Data e hora inválidas!");
            return false;
        }

        if (dataEmissao.isBefore(LocalDateTime.now())) {
            int response = JOptionPane.showConfirmDialog(null, "Data e hora estão no passado. Deseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);
            return response != JOptionPane.NO_OPTION;
        }

        return true;
    }

    private void clearInputs() {
        medicamentosTextArea.setText("");
        observacoesTextArea.setText("");
        dataEmissaoDateTimePicker.setDateTimePermissive(LocalDateTime.now());
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}