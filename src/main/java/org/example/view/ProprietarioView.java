package org.example.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.example.controller.ProprietarioViewController;
import org.example.view.tablemodels.ProprietarioTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
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
    private JTextField filtroNomeTextField;
    private ProprietarioTableModel tableModel;

    public ProprietarioView() {
        $$$setupUI$$$();
        configureListeners();
        loadProprietarios(filtroNomeTextField.getText().trim());
    }

    private void configureListeners() {
        adicionarProprietarioButton.addActionListener(this::adicionarProprietario);
        removerProprietarioButton.addActionListener(this::removerProprietario);

        filtroNomeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterProprietarios();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterProprietarios();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterProprietarios();
            }

            private void filterProprietarios() {
                String text = filtroNomeTextField.getText().trim();
                loadProprietarios(text);
            }
        });
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
            loadProprietarios(filtroNomeTextField.getText().trim());
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

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) proprietário(s) selecionado(s) e todos os seus dados relacionados?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int proprietarioId = tableModel.getProprietario(i).getId();
                    viewController.removerProprietario(proprietarioId);
                    //TODO remover agendamentos
                    //TODO remover faturamento
                    //TODO remover histórico
                    //TODO remover receita médica
                    //TODO remover pacientes
                } catch (Exception ex) {
                    handleException("Erro ao remover proprietário", ex);
                }
            }
            loadProprietarios(filtroNomeTextField.getText().trim());
        }
    }

    private void loadProprietarios(String nome) {
        try {
            tableModel = viewController.criarProprietarioTableModel(nome);
            proprietarioTable.setModel(tableModel);
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
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("CPF:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Telefone:");
        panel2.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Nome Completo:");
        panel2.add(label3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeCompletoTextField = new JTextField();
        panel2.add(nomeCompletoTextField, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Endereço:");
        panel2.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enderecoTextField = new JTextField();
        panel2.add(enderecoTextField, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        removerProprietarioButton = new JButton();
        removerProprietarioButton.setText("Remover Proprietário");
        panel2.add(removerProprietarioButton, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarProprietarioButton = new JButton();
        adicionarProprietarioButton.setText("Adicionar Proprietário");
        panel2.add(adicionarProprietarioButton, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(cpfTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        panel2.add(telefoneTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        proprietarioTable = new JTable();
        scrollPane1.setViewportView(proprietarioTable);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Buscar por nome:");
        panel3.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filtroNomeTextField = new JTextField();
        panel3.add(filtroNomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}