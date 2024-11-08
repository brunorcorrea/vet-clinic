package org.example.dao;

import org.example.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO extends DAO {
    private static AgendamentoDAO instance;

    AgendamentoDAO() {
        getConnection();
        createTable();
    }

    public static AgendamentoDAO getInstance() {
        return (instance == null ? (instance = new AgendamentoDAO()) : instance);
    }

    public void cadastrar(Agendamento agendamento) throws SQLException {
        String sql = "INSERT INTO agendamento (idPaciente, dataHora, servico, status, idVeterinario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getPaciente().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setString(3, agendamento.getServico());
            stmt.setString(4, agendamento.getStatus().name());
            stmt.setInt(5, agendamento.getVeterinario().getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM agendamento WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void excluirPorVeterinario(int veterinarioId) throws SQLException {
        String sql = "DELETE FROM agendamento WHERE idVeterinario = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, veterinarioId);
            stmt.executeUpdate();
        }
    }

    public void excluirPorPaciente(int pacienteId) throws SQLException {
        String sql = "DELETE FROM agendamento WHERE idPaciente = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, pacienteId);
            stmt.executeUpdate();
        }
    }

    public void editar(Agendamento agendamento) throws SQLException {
        String sql = "UPDATE agendamento SET idPaciente = ?, dataHora = ?, servico = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getPaciente().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setString(3, agendamento.getServico());
            stmt.setString(4, agendamento.getStatus().name());
            stmt.setInt(5, agendamento.getId());
            stmt.executeUpdate();
        }
    }

    public List<Agendamento> listar() throws SQLException {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT * FROM agendamento";
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Agendamento agendamento = new Agendamento();
                agendamento.setId(rs.getInt("id"));
                Veterinario veterinario = buscarVeterinarioPorId(rs.getInt("idVeterinario"));
                veterinario.getAgendamentos().add(agendamento);
                agendamento.setVeterinario(veterinario);
                agendamento.setPaciente(buscarPacientePorId(rs.getInt("idPaciente")));
                agendamento.setDataHora(rs.getTimestamp("dataHora").toLocalDateTime());
                agendamento.setServico(rs.getString("servico"));
                agendamento.setStatus(StatusAgendamento.valueOf(rs.getString("status")));
                agendamentos.add(agendamento);
            }
        }
        return agendamentos;
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
                    paciente.setEstadoCastracao(EstadoCastracao.valueOf(rs.getString("estadoCastracao")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paciente;
    }

    private Veterinario buscarVeterinarioPorId(int id) {
        Veterinario veterinario = new Veterinario();
        String sql = "SELECT * FROM veterinario WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    veterinario.setId(rs.getInt("id"));
                    veterinario.setNome(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return veterinario;
    }
}