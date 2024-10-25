package org.example.view;

import org.example.controller.ProprietarioViewController;
import org.example.view.tablemodels.ProprietarioTableModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.text.ParseException;

import static org.example.utils.Validator.isCPFValid;
import static org.example.utils.Validator.isTelefoneValid;

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


        try {
            validateInputs(cpf, nomeCompleto, telefone, endereco);

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

    private void validateInputs(String cpf, String nomeCompleto, String telefone, String endereco) {
        if (cpf.isEmpty() || nomeCompleto.isEmpty() || telefone.isEmpty() || endereco.isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos!");
        }

        if (!isCPFValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }

        if (!isTelefoneValid(telefone)) {
            throw new IllegalArgumentException("Telefone inválido!");
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

    private void createUIComponents() {
        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            cpfTextField = new JFormattedTextField(cpfMask);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            MaskFormatter telefoneMask = new MaskFormatter("(##) #####-####");
            telefoneMask.setPlaceholderCharacter('_');
            telefoneTextField = new JFormattedTextField(telefoneMask);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}