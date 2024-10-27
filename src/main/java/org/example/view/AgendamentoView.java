package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.example.controller.AgendamentoViewController;
import org.example.model.Agendamento;
import org.example.model.Paciente;
import org.example.model.StatusAgendamento;
import org.example.model.Veterinario;
import org.example.view.tablemodels.AgendamentoTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoView {
    private final AgendamentoViewController viewController = new AgendamentoViewController();
    private JPanel mainPanel;
    private JTable agendamentoTable;
    private JComboBox<String> pacienteComboBox;
    private JComboBox<String> veterinarioComboBox;
    private JTextField servicoTextField;
    private JComboBox<String> statusComboBox;
    private JButton adicionarAgendamentoButton;
    private JButton removerAgendamentoButton;
    private DateTimePicker dataHoraDateTimePicker;

    private List<Paciente> pacientes = new ArrayList<>();
    private List<Veterinario> veterinarios = new ArrayList<>();

    public AgendamentoView() {
        initializeComponents();
        configureListeners();
        buscarAgendamentos();
    }

    private void initializeComponents() {
        dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        for (StatusAgendamento status : StatusAgendamento.values()) {
            statusComboBox.addItem(status.getDescricao());
        }

        try {
            pacientes = viewController.listarPacientes();
            pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));
        } catch (Exception e) {
            handleException("Erro ao listar pacientes", e);
        }

        try {
            veterinarios = viewController.listarVeterinarios();
            veterinarios.forEach(veterinario -> veterinarioComboBox.addItem(veterinario.getNome()));
        } catch (Exception e) {
            handleException("Erro ao listar veterinários", e);
        }
    }

    private void configureListeners() {
        adicionarAgendamentoButton.addActionListener(this::adicionarAgendamento);
        removerAgendamentoButton.addActionListener(this::removerAgendamento);
    }

    private void adicionarAgendamento(ActionEvent e) {
        try {
            Paciente paciente = pacientes.get(pacienteComboBox.getSelectedIndex());
            Veterinario veterinario = veterinarios.get(veterinarioComboBox.getSelectedIndex());
            String servico = servicoTextField.getText().trim();
            StatusAgendamento status = StatusAgendamento.fromDescricao((String) statusComboBox.getSelectedItem());
            LocalDateTime dataHora = dataHoraDateTimePicker.getDateTimePermissive();

            validateInputs(paciente, veterinario, servico, status, dataHora);

            viewController.adicionarAgendamento(paciente, veterinario, servico, status, dataHora);
            servicoTextField.setText("");
            dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());
            buscarAgendamentos();
        } catch (Exception ex) {
            handleException("Erro ao adicionar agendamento", ex);
        }
    }

    private void removerAgendamento(ActionEvent e) {
        int[] selectedRows = agendamentoTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um agendamento!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) agendamento(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int agendamentoId = (Integer) agendamentoTable.getValueAt(i, 0);
                    viewController.removerAgendamento(agendamentoId);
                } catch (Exception ex) {
                    handleException("Erro ao remover agendamento", ex);
                }
            }
            buscarAgendamentos();
        }
    }

    private void buscarAgendamentos() {
        try {
            List<Agendamento> agendamentos = viewController.listarAgendamentos();
            agendamentoTable.setModel(new AgendamentoTableModel(agendamentos));
        } catch (Exception e) {
            handleException("Erro ao listar agendamentos", e);
        }
    }

    private void validateInputs(Paciente paciente, Veterinario veterinario, String servico, StatusAgendamento status, LocalDateTime dataHora) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente inválido!");
        }
        if (veterinario == null) {
            throw new IllegalArgumentException("Veterinário inválido!");
        }
        if (servico.isBlank()) {
            throw new IllegalArgumentException("Serviço inválido!");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status inválido!");
        }
        if (dataHora == null) {
            throw new IllegalArgumentException("Data e hora inválidas!");
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
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        agendamentoTable = new JTable();
        scrollPane1.setViewportView(agendamentoTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Paciente:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pacienteComboBox = new JComboBox();
        panel2.add(pacienteComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Veterinário:");
        panel2.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        veterinarioComboBox = new JComboBox();
        panel2.add(veterinarioComboBox, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Data e Hora:");
        panel2.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataHoraDateTimePicker = new DateTimePicker();
        panel2.add(dataHoraDateTimePicker, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Serviço:");
        panel2.add(label4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        servicoTextField = new JTextField();
        panel2.add(servicoTextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Status:");
        panel2.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statusComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        statusComboBox.setModel(defaultComboBoxModel1);
        panel2.add(statusComboBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarAgendamentoButton = new JButton();
        adicionarAgendamentoButton.setText("Adicionar Agendamento");
        panel2.add(adicionarAgendamentoButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removerAgendamentoButton = new JButton();
        removerAgendamentoButton.setText("Remover Agendamento");
        panel2.add(removerAgendamentoButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}