package org.example.view;

import org.example.controller.HistoricoController;
import org.example.model.Historico;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class HistoricoTableModel extends GenericTableModel {

    HistoricoController historicoController = HistoricoController.getInstance();

    public HistoricoTableModel(List vDados) {
        super(vDados, new String[]{"Id", "Paciente", "Peso", "Vacinas", "Doenças", "Observações", "Data e Hora"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Integer.class;
            case 1, 3, 4, 5 -> String.class;
            case 2 -> double.class;
            case 6 -> LocalDateTime.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Historico historico = (Historico) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> historico.getId();
            case 1 -> historico.getPaciente().getNome();
            case 2 -> historico.getPeso();
            case 3 -> String.join(", ", historico.getVacinas());
            case 4 -> String.join(", ", historico.getDoencas());
            case 5 -> String.join(", ", historico.getObservacoes());
            case 6 -> historico.getDataHora();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Historico historico = (Historico) vDados.get(rowIndex);

        switch (columnIndex) {
            case 2 -> historico.setPeso((String) aValue);
            case 3 -> historico.setVacinas(List.of(((String) aValue).split(", ")));
            case 4 -> historico.setDoencas(List.of(((String) aValue).split(", ")));
            case 5 -> historico.setObservacoes(List.of(((String) aValue).split(", ")));
            case 6 -> historico.setDataHora((LocalDateTime) aValue);
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            historicoController.editarHistorico(historico);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar histórico: " + e.getMessage());
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 7, 8);
        return !columnsNotEditable.contains(columnIndex);
    }
}
