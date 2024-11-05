package org.example.view.tablemodels;

import org.example.controller.ReceitaMedicaController;
import org.example.model.ReceitaMedica;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ReceitaMedicaTableModel extends GenericTableModel {

    ReceitaMedicaController receitaMedicaController = ReceitaMedicaController.getInstance();

    public ReceitaMedicaTableModel(List vDados) {
        super(vDados, new String[]{"Paciente", "Medicamentos", "Observações", "Data de Emissão"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1, 2 -> String.class;
            case 3 -> LocalDateTime.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReceitaMedica receitaMedica = (ReceitaMedica) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> receitaMedica.getPaciente().getNome();
            case 1 -> String.join(", ", receitaMedica.getMedicamentos());
            case 2 -> String.join(", ", receitaMedica.getObservacoes());
            case 3 -> receitaMedica.getDataEmissao();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ReceitaMedica receitaMedica = (ReceitaMedica) vDados.get(rowIndex);

        switch (columnIndex) {
            case 1 -> receitaMedica.setMedicamentos(List.of(((String) aValue).split(", ")));
            case 2 -> receitaMedica.setObservacoes(List.of(((String) aValue).split(", ")));
            case 3 -> receitaMedica.setDataEmissao((LocalDateTime) aValue);
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            receitaMedicaController.editarReceitaMedica(receitaMedica);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar Receita Médica: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }
}