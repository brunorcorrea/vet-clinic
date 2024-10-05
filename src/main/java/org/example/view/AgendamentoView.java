package org.example.view;

import org.example.controller.AgendamentoController;
import org.example.controller.PacienteController;
import org.example.controller.VeterinarioController;
import org.example.model.Paciente;
import org.example.model.StatusAgendamento;
import org.example.model.Veterinario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoView {
    private final AgendamentoController agendamentoController = AgendamentoController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();
    private final VeterinarioController veterinarioController = VeterinarioController.getInstance();
    private JPanel mainPanel;
    private JTable agendamentoTable;
    private JComboBox pacienteComboBox;
    private JComboBox veterinarioComboBox;
    private JTextField servicoTextField;
    private JComboBox statusComboBox;
    private JButton adicionarAgendamentoButton;
    private JButton removerAgendamentoButton;

    private List<Paciente> pacientes = new ArrayList<>();
    private List<Veterinario> veterinarios = new ArrayList<>();

    public AgendamentoView() {
        statusComboBox.addItem(StatusAgendamento.AGENDADO.getDescricao());
        statusComboBox.addItem(StatusAgendamento.CONCLUIDO.getDescricao());
        statusComboBox.addItem(StatusAgendamento.CANCELADO.getDescricao());

        try {
            pacientes = pacienteController.listarPacientes();
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar pacientes: " + e.getMessage());
        }

        pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));

        try {
            veterinarios = veterinarioController.listarVeterinarios();
        } catch (Exception e) {
            veterinarios = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar veterinários: " + e.getMessage());
        }

        veterinarios.forEach(veterinario -> veterinarioComboBox.addItem(veterinario.getNome()));

        adicionarAgendamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        removerAgendamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
