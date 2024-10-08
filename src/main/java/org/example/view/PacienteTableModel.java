package org.example.view;

import org.example.controller.PacienteController;
import org.example.model.EstadoCastracao;
import org.example.model.Paciente;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class PacienteTableModel extends GenericTableModel {

    PacienteController pacienteController = PacienteController.getInstance();

    public PacienteTableModel(List vDados) {
        super(vDados, new String[]{"Id", "Nome", "Estado Castração", "Idade", "Raça", "Coloração", "Espécie", "Proprietário", "Foto"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 3 -> Integer.class;
            case 1, 2, 4, 5, 6, 7 -> String.class;
            case 8 -> ImageIcon.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Paciente paciente = (Paciente) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> paciente.getId();
            case 1 -> paciente.getNome();
            case 2 -> paciente.getEstadoCastracao().getDescricao();
            case 3 -> paciente.getIdade();
            case 4 -> paciente.getRaca();
            case 5 -> paciente.getColoracao();
            case 6 -> paciente.getEspecie();
            case 7 -> paciente.getProprietario().getNomeCompleto();
            case 8 -> paciente.getFoto() != null ? new ImageIcon(paciente.getFoto()) : null;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Paciente paciente = (Paciente) vDados.get(rowIndex);

        switch (columnIndex) {
            case 1 -> paciente.setNome((String) aValue);
            case 2 -> paciente.setEstadoCastracao((EstadoCastracao) aValue);
            case 3 -> paciente.setIdade((int) aValue);
            case 4 -> paciente.setRaca((String) aValue);
            case 5 -> paciente.setColoracao((String) aValue);
            case 6 -> paciente.setEspecie((String) aValue);
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            pacienteController.editarPaciente(paciente);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar paciente: " + e.getMessage());
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 7, 8);
        return !columnsNotEditable.contains(columnIndex);
    }
}
