package org.example.dao;

import org.example.model.Agendamento;
import org.example.model.StatusAgendamento;
import org.example.model.Veterinario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAO extends DAO {

    private static VeterinarioDAO instance;

    private VeterinarioDAO() {
        getConnection();
        createTable();
    }

    public static VeterinarioDAO getInstance() {
        if (instance == null) {
            instance = new VeterinarioDAO();
        }
        return instance;
    }

    public void cadastrar(Veterinario veterinario) throws SQLException {
        String sql = """
                    INSERT INTO veterinario (nome) VALUES (?)
                """;
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setString(1, veterinario.getNome());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = """
                    DELETE FROM veterinario WHERE id = ?
                """;
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void editar(Veterinario veterinario) throws SQLException {
        String sql = """
                    UPDATE veterinario SET nome = ? WHERE id = ?
                """;
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setString(1, veterinario.getNome());
            stmt.setInt(2, veterinario.getId());
            stmt.executeUpdate();
        }
    }

    public List<Veterinario> listar() throws SQLException {
        List<Veterinario> veterinarios = new ArrayList<>();
        String sql = """
                    SELECT * FROM veterinario
                """;
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Veterinario veterinario = new Veterinario();
                veterinario.setId(rs.getInt("id"));
                veterinario.setNome(rs.getString("nome"));
                veterinario.setAgendamentos(buscarAgendamentosPorVeterinarioId(veterinario.getId()));
                veterinarios.add(veterinario);
            }
        }
        return veterinarios;
    }

    private List<Agendamento> buscarAgendamentosPorVeterinarioId(int id) throws SQLException {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = """
                    SELECT * FROM agendamento WHERE idVeterinario = ?
                """;
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Agendamento agendamento = new Agendamento();
                    agendamento.setId(rs.getInt("id"));
                    agendamento.setDataHora(rs.getTimestamp("dataHora").toLocalDateTime());
                    agendamento.setServico(rs.getString("servico"));
                    agendamento.setStatus(StatusAgendamento.valueOf(rs.getString("status")));
                    agendamento.setPaciente(PacienteDAO.getInstance().buscarPorId(rs.getInt("idPaciente")));
                    agendamentos.add(agendamento);
                }
            }
        }
        return agendamentos;
    }
}