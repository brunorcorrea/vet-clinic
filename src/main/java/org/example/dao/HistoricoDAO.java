package org.example.dao;

import org.example.model.Historico;
import org.example.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO extends DAO {
    private static HistoricoDAO instance;

    HistoricoDAO() {
        getConnection();
        createTable();
    }

    public static HistoricoDAO getInstance() {
        return (instance == null ? (instance = new HistoricoDAO()) : instance);
    }

    public void cadastrar(Historico historico) throws SQLException {
        String sql = "INSERT INTO historico (idPaciente, vacinas, doencas, peso, observacoes, dataHora) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, historico.getPaciente().getId());
            stmt.setString(2, String.join(",", historico.getVacinas()));
            stmt.setString(3, String.join(",", historico.getDoencas()));
            stmt.setString(4, historico.getPeso());
            stmt.setString(5, String.join(",", historico.getObservacoes()));
            stmt.setTimestamp(6, Timestamp.valueOf(historico.getDataHora()));
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM historico WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void excluirPorPaciente(int pacienteId) throws SQLException {
        String sql = "DELETE FROM historico WHERE idPaciente = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, pacienteId);
            stmt.executeUpdate();
        }
    }

    public void editar(Historico historico) throws SQLException {
        String sql = "UPDATE historico SET idPaciente = ?, vacinas = ?, doencas = ?, peso = ?, observacoes = ?, dataHora = ? WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, historico.getPaciente().getId());
            stmt.setString(2, String.join(",", historico.getVacinas()));
            stmt.setString(3, String.join(",", historico.getDoencas()));
            stmt.setString(4, historico.getPeso());
            stmt.setString(5, String.join(",", historico.getObservacoes()));
            stmt.setTimestamp(6, Timestamp.valueOf(historico.getDataHora()));
            stmt.setInt(7, historico.getId());
            stmt.executeUpdate();
        }
    }

    public List<Historico> listar() throws SQLException {
        List<Historico> historicos = new ArrayList<>();
        String sql = "SELECT * FROM historico";
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Historico historico = new Historico();
                historico.setId(rs.getInt("id"));
                historico.setPaciente(buscarPacientePorId(rs.getInt("idPaciente")));
                historico.setVacinas(List.of(rs.getString("vacinas").split(",")));
                historico.setDoencas(List.of(rs.getString("doencas").split(",")));
                historico.setPeso(rs.getString("peso"));
                historico.setObservacoes(List.of(rs.getString("observacoes").split(",")));
                historico.setDataHora(rs.getTimestamp("dataHora").toLocalDateTime());
                historicos.add(historico);
            }
        }
        return historicos;
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
