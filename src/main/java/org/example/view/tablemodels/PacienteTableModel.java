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
            case 0 -> {
                String nome = ((String) aValue).trim();

                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nome não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                paciente.setNome(nome);
            }
            case 1 -> {
                String status = ((String) aValue).trim();
                try {
                    paciente.setEstadoCastracao(EstadoCastracao.fromDescricao(status));
                } catch (IllegalArgumentException e) {
                    String message = "Status inválido: " + status + ". Os status válidos são: " + EstadoCastracao.CASTRADO.getDescricao() + " ou " + EstadoCastracao.FERTIL.getDescricao() + ".";
                    JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            case 2 -> {
                int idade = (int) aValue;

                if (idade < 0) {
                    JOptionPane.showMessageDialog(null, "Idade não pode ser negativa.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                paciente.setIdade(idade);
            }
            case 3 -> {
                String raca = ((String) aValue).trim();

                if (raca.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Raça não pode ser vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                paciente.setRaca(raca);
            }
            case 4 -> {
                String coloracao = ((String) aValue).trim();

                if (coloracao.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Coloração não pode ser vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                paciente.setColoracao(coloracao);
            }
            case 5 -> {
                String especie = ((String) aValue).trim();

                if (especie.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Espécie não pode ser vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                paciente.setEspecie(especie);
            }
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