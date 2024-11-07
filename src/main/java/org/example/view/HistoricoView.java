package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.example.controller.HistoricoViewController;
import org.example.model.Paciente;
import org.example.view.tablemodels.HistoricoTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoView {
    private final HistoricoViewController viewController = new HistoricoViewController();

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
    private JTextField filtroPacienteNomeTextField;
    private HistoricoTableModel tableModel;

    private List<Paciente> pacientes = new ArrayList<>();

    public HistoricoView() {
        initializeComponents();
        configureListeners();
        loadHistoricos(filtroPacienteNomeTextField.getText().trim());
    }

    private void initializeComponents() {
        dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        try {
            pacientes = viewController.listarPacientes();
            pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));
        } catch (Exception e) {
            handleException("Erro ao listar pacientes", e);
        }
    }

    private void configureListeners() {
        adicionarAoHistoricoButton.addActionListener(this::adicionarHistorico);
        removerDoHistoricoButton.addActionListener(this::removerHistorico);

        filtroPacienteNomeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterHistoricos();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterHistoricos();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterHistoricos();
            }

            private void filterHistoricos() {
                String text = filtroPacienteNomeTextField.getText().trim();
                loadHistoricos(text);
            }
        });
    }

    private void adicionarHistorico(ActionEvent e) {
        try {
            Paciente paciente = pacientes.get(pacienteComboBox.getSelectedIndex());
            List<String> vacinas = List.of(vacinasTextArea.getText().split("\n"));
            List<String> doencas = List.of(doencasTextArea.getText().split("\n"));
            String peso = pesoTextField.getText().trim();
            List<String> observacoes = List.of(observacoesTextArea.getText().split("\n"));
            LocalDateTime dataHora = dataHoraDateTimePicker.getDateTimePermissive();

            validateInputs(paciente, vacinas, doencas, peso, observacoes, dataHora);

            viewController.adicionarHistorico(paciente, vacinas, doencas, peso, observacoes, dataHora);
            clearInputs();
            loadHistoricos(filtroPacienteNomeTextField.getText().trim());
        } catch (Exception ex) {
            handleException("Erro ao adicionar histórico", ex);
        }
    }

    private void removerHistorico(ActionEvent e) {
        int[] selectedRows = historicoTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um histórico!");
            return;
        }
        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) histórico(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int historicoId = tableModel.getHistorico(i).getId();
                    viewController.removerHistorico(historicoId);
                } catch (Exception ex) {
                    handleException("Erro ao remover histórico", ex);
                }
            }
            loadHistoricos(filtroPacienteNomeTextField.getText().trim());
        }
    }

    private void loadHistoricos(String nomePaciente) {
        try {
            tableModel = viewController.criarHistoricoTableModel(nomePaciente);
            historicoTable.setModel(tableModel);
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
        historicoTable = new JTable();
        scrollPane1.setViewportView(historicoTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Paciente:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pacienteComboBox = new JComboBox();
        panel2.add(pacienteComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Vacinas:");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vacinasTextArea = new JTextArea();
        panel2.add(vacinasTextArea, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Doenças:");
        panel2.add(label3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doencasTextArea = new JTextArea();
        panel2.add(doencasTextArea, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Peso:");
        panel2.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pesoTextField = new JTextField();
        panel2.add(pesoTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Data e hora:");
        panel2.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataHoraDateTimePicker = new DateTimePicker();
        panel2.add(dataHoraDateTimePicker, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        removerDoHistoricoButton = new JButton();
        removerDoHistoricoButton.setText("Remover do Histórico");
        panel2.add(removerDoHistoricoButton, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarAoHistoricoButton = new JButton();
        adicionarAoHistoricoButton.setText("Adicionar ao Histórico");
        panel2.add(adicionarAoHistoricoButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Observações:");
        panel2.add(label6, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        observacoesTextArea = new JTextArea();
        panel2.add(observacoesTextArea, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Buscar por paciente:");
        panel3.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filtroPacienteNomeTextField = new JTextField();
        panel3.add(filtroPacienteNomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}