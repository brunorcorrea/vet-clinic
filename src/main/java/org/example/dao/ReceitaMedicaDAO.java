package org.example.dao;

import org.example.model.Paciente;
import org.example.model.ReceitaMedica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceitaMedicaDAO extends DAO {

    private static ReceitaMedicaDAO instance;

    public ReceitaMedicaDAO() {
        getConnection();
        createTable();
    }

    public static ReceitaMedicaDAO getInstance() {
        return (instance == null ? (instance = new ReceitaMedicaDAO()) : instance);
    }

    public void cadastrar(ReceitaMedica receita) throws SQLException {
        String sql = "INSERT INTO receitaMedica (idPaciente, medicamentos, dataEmissao, observacoes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, receita.getPaciente().getId());
            stmt.setString(2, String.join(", ", receita.getMedicamentos()));
            stmt.setTimestamp(3, Timestamp.valueOf(receita.getDataEmissao()));
            stmt.setString(4, String.join(", ", receita.getObservacoes()));
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM receitaMedica WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void editar(ReceitaMedica receita) throws SQLException {
        String sql = "UPDATE receitaMedica SET idPaciente = ?, medicamentos = ?, dataEmissao = ?, observacoes = ? WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, receita.getPaciente().getId());
            stmt.setString(2, String.join(", ", receita.getMedicamentos()));
            stmt.setTimestamp(3, Timestamp.valueOf(receita.getDataEmissao()));
            stmt.setString(4, String.join(", ", receita.getObservacoes()));
            stmt.setInt(5, receita.getId());
            stmt.executeUpdate();
        }
    }

    public List<ReceitaMedica> listar() throws SQLException {
        List<ReceitaMedica> receitas = new ArrayList<>();
        String sql = "SELECT * FROM receitaMedica";
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ReceitaMedica receita = new ReceitaMedica();
                receita.setId(rs.getInt("id"));
                receita.setPaciente(buscarPacientePorId(rs.getInt("idPaciente")));
                receita.setMedicamentos(List.of(rs.getString("medicamentos").split(", ")));
                receita.setDataEmissao(rs.getTimestamp("dataEmissao").toLocalDateTime());
                receita.setObservacoes(List.of(rs.getString("observacoes").split(", ")));
                receitas.add(receita);
            }
        }
        return receitas;
    }

    public ReceitaMedica buscarPorId(int id) throws SQLException {
        ReceitaMedica receita = null;
        String sql = "SELECT * FROM receitaMedica WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    receita = new ReceitaMedica();
                    receita.setId(rs.getInt("id"));
                    receita.setPaciente(buscarPacientePorId(rs.getInt("idPaciente")));
                    receita.setMedicamentos(List.of(rs.getString("medicamentos").split(", ")));
                    receita.setDataEmissao(rs.getTimestamp("dataEmissao").toLocalDateTime());
                    receita.setObservacoes(List.of(rs.getString("observacoes").split(", ")));
                }
            }
        }
        return receita;
    }

    private Paciente buscarPacientePorId(int id) {
        Paciente paciente = new Paciente();
        String sql = "SELECT * FROM paciente WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    paciente.setId(rs.getInt("id"));
                    paciente.setNome(rs.getString("nome"));
                    paciente.setColoracao(rs.getString("coloracao"));
                    paciente.setEspecie(rs.getString("especie"));
                    paciente.setIdade(rs.getInt("idade"));
                    paciente.setRaca(rs.getString("raca"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paciente;
    }
}
