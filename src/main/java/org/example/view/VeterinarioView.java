package org.example.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.example.controller.VeterinarioViewController;
import org.example.view.tablemodels.VeterinarioTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VeterinarioView {
    private final VeterinarioViewController viewController = new VeterinarioViewController();
    private JPanel mainPanel;
    private JTable veterinarioTable;
    private JButton adicionarVeterinarioButton;
    private JTextField nomeVeterinarioTextField;
    private JButton removerVeterinarioButton;
    private JTextField filtroNomeTextField;
    private VeterinarioTableModel tableModel;

    public VeterinarioView() {
        configureListeners();
        loadVeterinarios(filtroNomeTextField.getText().trim());
    }

    private void configureListeners() {
        adicionarVeterinarioButton.addActionListener(this::adicionarVeterinario);
        removerVeterinarioButton.addActionListener(this::removerVeterinario);

        filtroNomeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterVeterinarios();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterVeterinarios();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterVeterinarios();
            }

            private void filterVeterinarios() {
                String text = filtroNomeTextField.getText().trim();
                loadVeterinarios(text);
            }
        });
    }

    private void adicionarVeterinario(ActionEvent e) {
        String nomeVeterinario = nomeVeterinarioTextField.getText().trim();
        if (nomeVeterinario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome do veterinário não pode ser vazio");
            return;
        }

        try {
            viewController.adicionarVeterinario(nomeVeterinario);
            nomeVeterinarioTextField.setText("");
        } catch (Exception ex) {
            handleException("Erro ao adicionar veterinário", ex);
        }

        loadVeterinarios(filtroNomeTextField.getText().trim());
    }

    private void removerVeterinario(ActionEvent e) {
        int[] selectedRows = veterinarioTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um veterinário!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) veterinário(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int veterinarioId = tableModel.getVeterinario(i).getId();
                    viewController.removerVeterinario(veterinarioId);
                } catch (Exception ex) {
                    handleException("Erro ao remover veterinário", ex);
                }
            }
            loadVeterinarios(filtroNomeTextField.getText().trim());
        }
    }

    private void loadVeterinarios(String nome) {
        try {
            tableModel = viewController.criarVeterinarioTableModel(nome);
            veterinarioTable.setModel(tableModel);
        } catch (Exception e) {
            handleException("Erro ao listar veterinários", e);
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
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeVeterinarioTextField = new JTextField();
        nomeVeterinarioTextField.setText("");
        nomeVeterinarioTextField.setToolTipText("Nome do Veterinário");
        panel2.add(nomeVeterinarioTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        adicionarVeterinarioButton = new JButton();
        adicionarVeterinarioButton.setText("Adicionar Veterinário");
        panel2.add(adicionarVeterinarioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        removerVeterinarioButton = new JButton();
        removerVeterinarioButton.setText("Remover Veterinário");
        panel2.add(removerVeterinarioButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Nome:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Buscar por nome:");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filtroNomeTextField = new JTextField();
        panel3.add(filtroNomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        veterinarioTable = new JTable();
        veterinarioTable.setAutoResizeMode(2);
        scrollPane1.setViewportView(veterinarioTable);
        label1.setLabelFor(scrollPane1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}