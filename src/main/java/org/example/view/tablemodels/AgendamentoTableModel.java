package org.example.view.tablemodels;

import org.example.controller.AgendamentoController;
import org.example.model.Agendamento;
import org.example.model.StatusAgendamento;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendamentoTableModel extends GenericTableModel {

    AgendamentoController agendamentoController = AgendamentoController.getInstance();

    public AgendamentoTableModel(List vDados) {
        super(vDados, new String[]{"Paciente", "Veterinário", "Data e Hora", "Serviço", "Status"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1, 2, 3, 4 -> String.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    public Agendamento getAgendamento(int rowIndex) {
        return (Agendamento) vDados.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agendamento agendamento = (Agendamento) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> agendamento.getPaciente().getNome();
            case 1 -> agendamento.getVeterinario().getNome();
            case 2 -> agendamento.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            case 3 -> agendamento.getServico();
            case 4 -> agendamento.getStatus().getDescricao();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Agendamento agendamento = (Agendamento) vDados.get(rowIndex);

        switch (columnIndex) {
            case 2 -> {
                String dataHoraStr = ((String) aValue).trim();

                try {
                    LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    agendamento.setDataHora(dataHora);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Data e Hora inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            case 3 -> {
                String servico = ((String) aValue).trim();

                if (servico.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Serviço não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                agendamento.setServico(servico);
            }
            case 4 -> {
                String status = ((String) aValue).trim();
                try {
                    agendamento.setStatus(StatusAgendamento.fromDescricao(status));
                } catch (IllegalArgumentException e) {
                    String message = "Status inválido: " + status + ". Os status válidos são: " + StatusAgendamento.AGENDADO.getDescricao() + ", " + StatusAgendamento.CONCLUIDO.getDescricao() + " ou " + StatusAgendamento.CANCELADO.getDescricao() + ".";
                    JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            agendamentoController.editarAgendamento(agendamento);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar agendamento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 1);
        return !columnsNotEditable.contains(columnIndex);
    }
}
