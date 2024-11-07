package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.example.controller.FaturaViewController;
import org.example.model.Proprietario;
import org.example.model.StatusPagamento;
import org.example.view.tablemodels.FaturaTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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
    private JTextField filtroProprietarioNomeTextField;
    private FaturaTableModel tableModel;

    private List<Proprietario> proprietarios = new ArrayList<>();

    public FaturaView() {
        initializeComponents();
        configureListeners();
        loadFaturamentos(filtroProprietarioNomeTextField.getText().trim());
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

        filtroProprietarioNomeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterPacientes();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterPacientes();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterPacientes();
            }

            private void filterPacientes() {
                String text = filtroProprietarioNomeTextField.getText().trim();
                loadFaturamentos(text);
            }
        });
    }

    private void adicionarFatura(ActionEvent e) {
        try {
            Proprietario proprietario = proprietarios.get(proprietarioComboBox.getSelectedIndex());
            StatusPagamento status = StatusPagamento.fromDescricao((String) statusComboBox.getSelectedItem());
            LocalDateTime dataVencimento = dataVencimentoDateTimePicker.getDateTimePermissive();
            double valorTotal = Double.parseDouble(valorTotalTextField.getText().trim());

            validateInputs(proprietario, valorTotal, status, dataVencimento);

            viewController.adicionarFatura(proprietario, valorTotal, status, dataVencimento);
            clearInputs();
            loadFaturamentos(filtroProprietarioNomeTextField.getText().trim());
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
                    int faturamentoId = tableModel.getFaturamento(i).getId();
                    viewController.removerFatura(faturamentoId);
                } catch (Exception ex) {
                    handleException("Erro ao remover fatura", ex);
                }
            }
            loadFaturamentos(filtroProprietarioNomeTextField.getText().trim());
        }
    }

    private void clearInputs() {
        valorTotalTextField.setText("");
        proprietarioComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        dataVencimentoDateTimePicker.setDateTimePermissive(LocalDateTime.now());
    }

    private void loadFaturamentos(String nomeProprietario) {
        try {
            tableModel = viewController.criarFaturaTableModel(nomeProprietario);
            faturaTable.setModel(tableModel);
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
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        faturaTable = new JTable();
        scrollPane1.setViewportView(faturaTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Proprietário:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        proprietarioComboBox = new JComboBox();
        panel2.add(proprietarioComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarFaturaButton = new JButton();
        adicionarFaturaButton.setText("Adicionar Fatura");
        panel2.add(adicionarFaturaButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removerFaturaButton = new JButton();
        removerFaturaButton.setText("Remover Fatura");
        panel2.add(removerFaturaButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Status:");
        panel2.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Data de Vencimento:");
        panel2.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataVencimentoDateTimePicker = new DateTimePicker();
        panel2.add(dataVencimentoDateTimePicker, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Valor Total:");
        panel2.add(label4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorTotalTextField = new JTextField();
        panel2.add(valorTotalTextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        statusComboBox = new JComboBox();
        panel2.add(statusComboBox, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Buscar por proprietário:");
        panel3.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filtroProprietarioNomeTextField = new JTextField();
        panel3.add(filtroProprietarioNomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}