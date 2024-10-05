package org.example.view;

import org.example.controller.AgendamentoController;
import org.example.model.Agendamento;
import org.example.model.StatusAgendamento;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoTableModel extends GenericTableModel {

    AgendamentoController agendamentoController = AgendamentoController.getInstance();

    public AgendamentoTableModel(List vDados) {
        super(vDados, new String[]{"Id", "Paciente", "Veterinário", "Data e Hora", "Serviço", "Status"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Integer.class;
            case 1, 2, 4, 5 -> String.class;
            case 3 -> LocalDateTime.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agendamento agendamento = (Agendamento) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> agendamento.getId();
            case 1 -> agendamento.getPaciente().getNome();
            case 2 -> agendamento.getVeterinario().getNome();
            case 3 -> agendamento.getDataHora();
            case 4 -> agendamento.getServico();
            case 5 -> agendamento.getStatus().getDescricao();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Agendamento agendamento = (Agendamento) vDados.get(rowIndex);

        switch (columnIndex) {
            case 3 -> agendamento.setDataHora((LocalDateTime) aValue);
            case 4 -> agendamento.setServico((String) aValue);
            case 5 -> agendamento.setStatus(StatusAgendamento.fromDescricao((String) aValue));
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            agendamentoController.editarAgendamento(agendamento);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar agendamento: " + e.getMessage());
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 7, 8);
        return !columnsNotEditable.contains(columnIndex);
    }
}
