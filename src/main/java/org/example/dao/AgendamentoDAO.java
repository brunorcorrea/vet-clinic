package org.example.dao;

import org.example.model.Agendamento;
import org.example.model.EstadoCastracao;
import org.example.model.Paciente;
import org.example.model.StatusAgendamento;

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
        String sql = "INSERT INTO agendamentos (idPaciente, dataHora, servico, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getPaciente().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setString(3, agendamento.getServico());
            stmt.setString(4, agendamento.getStatus().name());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM agendamentos WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void editar(Agendamento agendamento) throws SQLException {
        String sql = "UPDATE agendamentos SET paciente_id = ?, data_hora = ?, servico = ?, status = ? WHERE id = ?";
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
        String sql = "SELECT * FROM agendamentos";
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Agendamento agendamento = new Agendamento();
                agendamento.setId(rs.getInt("id"));

                agendamento.setPaciente(buscarPacientePorId(rs.getInt("paciente_id")));
                agendamento.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                agendamento.setServico(rs.getString("servico"));
                agendamento.setStatus(StatusAgendamento.valueOf(rs.getString("status")));
                agendamentos.add(agendamento);
            }
        }
        return agendamentos;
    }

    private Paciente buscarPacientePorId(int id) {
        Paciente paciente = new Paciente();
        String sql = "SELECT * FROM pacientes WHERE id = ?";
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
                    paciente.setEstadoCastracao(EstadoCastracao.valueOf(rs.getString("estado_castracao")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paciente;
    }
}