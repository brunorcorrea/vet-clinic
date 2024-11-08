package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.example.controller.ReceitaMedicaViewController;
import org.example.model.Paciente;
import org.example.view.tablemodels.ReceitaMedicaTableModel;

import javax.swing.*;
import java.awt.*;
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
    private ReceitaMedicaTableModel tableModel;

    private List<Paciente> pacientes = new ArrayList<>();

    public ReceitaMedicaView() {
        initializeComponents();
        configureListeners();
        loadPacientes();
        loadReceitasMedicas();
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
        String medicamentosText = medicamentosTextArea.getText();
        LocalDateTime dataEmissao = dataEmissaoDateTimePicker.getDateTimePermissive();
        int pacienteIndex = pacienteComboBox.getSelectedIndex();

        if (!validateInputs(pacienteIndex, medicamentosText, dataEmissao)) return;

        List<String> medicamentos = List.of(medicamentosTextArea.getText().split("\n"));
        List<String> observacoes = List.of(observacoesTextArea.getText().split("\n"));
        Paciente paciente = pacientes.get(pacienteIndex);
        try {
            viewController.adicionarReceitaMedica(paciente, medicamentos, observacoes, dataEmissao);
            clearInputs();
        } catch (Exception ex) {
            handleException("Erro ao adicionar receita médica", ex);
        }

        loadReceitasMedicas();
    }

    private void removerReceitaMedica(ActionEvent e) {
        int[] selectedRows = receitaMedicaTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos uma receita médica", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover a(s) receita(s) médica(s) selecionada(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int receitaMedicaId = tableModel.getReceitaMedica(i).getId();
                    viewController.removerReceitaMedica(receitaMedicaId);
                } catch (Exception ex) {
                    handleException("Erro ao remover receita médica", ex);
                }
            }
            loadReceitasMedicas();
        }
    }

    private void loadReceitasMedicas() {
        try {
            tableModel = viewController.criarReceitaMedicaTableModel();
            receitaMedicaTable.setModel(tableModel);
        } catch (Exception e) {
            handleException("Erro ao listar receitas médicas", e);
        }
    }

    private boolean validateInputs(int pacienteIndex, String medicamentos, LocalDateTime dataEmissao) {
        if (pacienteIndex < 0 || pacienteIndex >= pacientes.size()) {
            JOptionPane.showMessageDialog(null, "Paciente inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (medicamentos == null || medicamentos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Medicamentos não podem ser vazios", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (dataEmissao == null) {
            JOptionPane.showMessageDialog(null, "Data e hora inválidas", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearInputs() {
        medicamentosTextArea.setText("");
        observacoesTextArea.setText("");
        dataEmissaoDateTimePicker.setDateTimePermissive(LocalDateTime.now());
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
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        receitaMedicaTable = new JTable();
        scrollPane1.setViewportView(receitaMedicaTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Paciente:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pacienteComboBox = new JComboBox();
        panel2.add(pacienteComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarReceitaMedicaButton = new JButton();
        adicionarReceitaMedicaButton.setText("Adicionar Receita Médica");
        panel2.add(adicionarReceitaMedicaButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removerReceitaMedicaButton = new JButton();
        removerReceitaMedicaButton.setText("Remover Receita Médica");
        panel2.add(removerReceitaMedicaButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Data de emissão:");
        panel2.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataEmissaoDateTimePicker = new DateTimePicker();
        panel2.add(dataEmissaoDateTimePicker, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Observações:");
        panel2.add(label3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        medicamentosTextArea = new JTextArea();
        panel2.add(medicamentosTextArea, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Medicamentos:");
        panel2.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        observacoesTextArea = new JTextArea();
        observacoesTextArea.setText("");
        panel2.add(observacoesTextArea, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}