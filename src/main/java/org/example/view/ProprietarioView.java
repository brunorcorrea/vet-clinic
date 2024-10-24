package org.example.view;

import org.example.controller.ProprietarioViewController;
import org.example.view.tablemodels.ProprietarioTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProprietarioView {
    private final ProprietarioViewController viewController = new ProprietarioViewController();
    private JPanel mainPanel;
    private JTable proprietarioTable;
    private JButton adicionarProprietarioButton;
    private JButton removerProprietarioButton;
    private JTextField cpfTextField;
    private JTextField nomeCompletoTextField;
    private JTextField telefoneTextField;
    private JTextField enderecoTextField;

    public ProprietarioView() {
        configureListeners();
        loadProprietarios();
    }

    private void configureListeners() {
        adicionarProprietarioButton.addActionListener(this::adicionarProprietario);
        removerProprietarioButton.addActionListener(this::removerProprietario);
    }

    private void adicionarProprietario(ActionEvent e) {
        String cpf = cpfTextField.getText().trim();
        String nomeCompleto = nomeCompletoTextField.getText().trim();
        String telefone = telefoneTextField.getText().trim();
        String endereco = enderecoTextField.getText().trim();

        if (cpf.isEmpty() || nomeCompleto.isEmpty() || telefone.isEmpty() || endereco.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            return;
        }

        try {
            viewController.adicionarProprietario(cpf, nomeCompleto, telefone, endereco);
            clearInputs();
            loadProprietarios();
        } catch (Exception ex) {
            handleException("Erro ao cadastrar proprietário", ex);
        }
    }

    private void removerProprietario(ActionEvent e) {
        int[] selectedRows = proprietarioTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um proprietário!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) proprietário(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int proprietarioId = (Integer) proprietarioTable.getValueAt(i, 0);
                    viewController.removerProprietario(proprietarioId);
                } catch (Exception ex) {
                    handleException("Erro ao remover proprietário", ex);
                }
            }
            JOptionPane.showMessageDialog(null, "Proprietário(s) removidos(s) com sucesso!");
            loadProprietarios();
        }
    }

    private void loadProprietarios() {
        try {
            ProprietarioTableModel model = viewController.criarProprietarioTableModel();
            proprietarioTable.setModel(model);
        } catch (Exception e) {
            handleException("Erro ao listar proprietários", e);
        }
    }

    private void clearInputs() {
        cpfTextField.setText("");
        nomeCompletoTextField.setText("");
        telefoneTextField.setText("");
        enderecoTextField.setText("");
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}