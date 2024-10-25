package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.FaturaViewController;
import org.example.model.Proprietario;
import org.example.model.StatusPagamento;
import org.example.view.tablemodels.FaturaTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FaturaView {
    private final FaturaViewController viewController = new FaturaViewController();

    private JPanel mainPanel;
    private JTable faturaTable;
    private JComboBox<String> proprietarioComboBox;
    private JTextField valorTotalTextField;
    private JComboBox<String> statusComboBox;
    private JButton adicionarFaturaButton;
    private JButton removerFaturaButton;
    private DateTimePicker dataVencimentoDateTimePicker;

    private List<Proprietario> proprietarios = new ArrayList<>();

    public FaturaView() {
        initializeComponents();
        configureListeners();
        buscarFaturamentos();
    }

    private void initializeComponents() {
        dataVencimentoDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        for (StatusPagamento status : StatusPagamento.values()) {
            statusComboBox.addItem(status.getDescricao());
        }

        try {
            proprietarios = viewController.listarProprietarios();
            proprietarios.forEach(proprietario -> proprietarioComboBox.addItem(proprietario.getNomeCompleto()));
        } catch (Exception e) {
            handleException("Erro ao listar proprietários", e);
        }
    }

    private void configureListeners() {
        adicionarFaturaButton.addActionListener(this::adicionarFatura);
        removerFaturaButton.addActionListener(this::removerFatura);
    }

    private void adicionarFatura(ActionEvent e) {
        try {
            Proprietario proprietario = proprietarios.get(proprietarioComboBox.getSelectedIndex());
            StatusPagamento status = StatusPagamento.fromDescricao((String) statusComboBox.getSelectedItem());
            LocalDateTime dataVencimento = dataVencimentoDateTimePicker.getDateTimePermissive();
            double valorTotal = Double.parseDouble(valorTotalTextField.getText().trim());

            validateInputs(proprietario, valorTotal, status, dataVencimento);

            viewController.adicionarFatura(proprietario, valorTotal, status, dataVencimento);
            JOptionPane.showMessageDialog(null, "Fatura adicionada com sucesso!");
            buscarFaturamentos();
        } catch (Exception ex) {
            handleException("Erro ao adicionar fatura", ex);
        }
    }

    private void removerFatura(ActionEvent e) {
        int[] selectedRows = faturaTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos uma fatura!");
            return;
        }
        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover a(s) fatura(s) selecionada(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int faturamentoId = (Integer) faturaTable.getValueAt(i, 0);
                    viewController.removerFatura(faturamentoId);
                } catch (Exception ex) {
                    handleException("Erro ao remover fatura", ex);
                }
            }
            JOptionPane.showMessageDialog(null, "Fatura(s) removida(s) com sucesso!");
            buscarFaturamentos();
        }
    }

    private void buscarFaturamentos() {
        try {
            FaturaTableModel model = viewController.criarFaturaTableModel();
            faturaTable.setModel(model);
        } catch (Exception e) {
            handleException("Erro ao listar faturamentos", e);
        }
    }

    private void validateInputs(Proprietario proprietario, double valorTotal, StatusPagamento status, LocalDateTime dataVencimento) {
        if (proprietario == null) {
            throw new IllegalArgumentException("Proprietário inválido!");
        }
        if (valorTotal <= 0) {
            throw new IllegalArgumentException("Valor total inválido!");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status inválido!");
        }
        if (dataVencimento == null) {
            throw new IllegalArgumentException("Data de vencimento inválida!");
        }
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}