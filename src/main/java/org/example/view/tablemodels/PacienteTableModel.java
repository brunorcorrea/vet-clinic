package org.example.view.tablemodels;

import org.example.controller.PacienteController;
import org.example.model.EstadoCastracao;
import org.example.model.Paciente;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class PacienteTableModel extends GenericTableModel {

    PacienteController pacienteController = PacienteController.getInstance();

    public PacienteTableModel(List vDados) {
        super(vDados, new String[]{"Nome", "Estado Castração", "Idade", "Raça", "Coloração", "Espécie", "Proprietário", "Foto"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1, 3, 4, 5, 6 -> String.class;
            case 2 -> Integer.class;
            case 7 -> ImageIcon.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    public Paciente getPaciente(int rowIndex) {
        return (Paciente) vDados.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Paciente paciente = (Paciente) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> paciente.getNome();
            case 1 -> paciente.getEstadoCastracao().getDescricao();
            case 2 -> paciente.getIdade();
            case 3 -> paciente.getRaca();
            case 4 -> paciente.getColoracao();
            case 5 -> paciente.getEspecie();
            case 6 -> paciente.getProprietario().getNomeCompleto();
            case 7 -> paciente.getFoto() != null ? new ImageIcon(paciente.getFoto()) : null;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Paciente paciente = (Paciente) vDados.get(rowIndex);

        switch (columnIndex) {
            case 0 -> paciente.setNome((String) aValue);
            case 1 -> paciente.setEstadoCastracao((EstadoCastracao) aValue);
            case 2 -> paciente.setIdade((int) aValue);
            case 3 -> paciente.setRaca((String) aValue);
            case 4 -> paciente.setColoracao((String) aValue);
            case 5 -> paciente.setEspecie((String) aValue);
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            pacienteController.editarPaciente(paciente);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar paciente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(6, 7);
        return !columnsNotEditable.contains(columnIndex);
    }
}