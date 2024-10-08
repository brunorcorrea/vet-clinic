package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.FaturamentoController;
import org.example.controller.ProprietarioController;
import org.example.model.Faturamento;
import org.example.model.Proprietario;
import org.example.model.StatusPagamento;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FaturaView {
    private final FaturamentoController faturamentoController = FaturamentoController.getInstance();
    private final ProprietarioController proprietarioController = ProprietarioController.getInstance();

    private JPanel mainPanel;
    private JTable faturaTable;
    private JComboBox proprietarioComboBox;
    private JTextField valorTotalTextField;
    private JComboBox statusComboBox;
    private JButton adicionarFaturaButton;
    private JButton removerFaturaButton;
    private DateTimePicker dataVencimentoDateTimePicker;

    private List<Proprietario> proprietarios = new ArrayList<>();

    public FaturaView() {
        dataVencimentoDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        statusComboBox.addItem(StatusPagamento.EM_ATRASO.getDescricao());
        statusComboBox.addItem(StatusPagamento.PENDENTE.getDescricao());
        statusComboBox.addItem(StatusPagamento.PAGO.getDescricao());

        try {
            proprietarios = proprietarioController.listarProprietarios();
        } catch (Exception e) {
            proprietarios = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar proprietários: " + e.getMessage());
        }

        proprietarios.forEach(proprietario -> proprietarioComboBox.addItem(proprietario.getNomeCompleto()));

        adicionarFaturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        removerFaturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        List<Faturamento> faturamentos;
        try {
            faturamentos = faturamentoController.listarFaturamentos();
        } catch (Exception e) {
            faturamentos = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar faturamentos: " + e.getMessage());
        }

        faturaTable.setModel(new FaturaTableModel(faturamentos));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
