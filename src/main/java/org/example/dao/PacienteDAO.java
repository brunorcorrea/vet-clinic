package org.example.dao;

import org.example.model.EstadoCastracao;
import org.example.model.Paciente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO extends DAO {
    private static PacienteDAO instance;

    PacienteDAO() {
        getConnection();
        createTable();
    }

    public static PacienteDAO getInstance() {
        return (instance == null ? (instance = new PacienteDAO()) : instance);
    }

    public void cadastrar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (nome, estadoCastracao, idade, raca, coloracao, especie, foto) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getEstadoCastracao().name());
            stmt.setInt(3, paciente.getIdade());
            stmt.setString(4, paciente.getRaca());
            stmt.setString(5, paciente.getColoracao());
            stmt.setString(6, paciente.getEspecie());
            stmt.setBytes(7, paciente.getFoto());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void editar(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente SET nome = ?, estadoCastracao = ?, idade = ?, raca = ?, coloracao = ?, especie = ?, foto = ? WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getEstadoCastracao().name());
            stmt.setInt(3, paciente.getIdade());
            stmt.setString(4, paciente.getRaca());
            stmt.setString(5, paciente.getColoracao());
            stmt.setString(6, paciente.getEspecie());
            stmt.setBytes(7, paciente.getFoto());
            stmt.setInt(8, paciente.getId());
            stmt.executeUpdate();
        }
    }

    public List<Paciente> listar() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getInt("id"));
                paciente.setNome(rs.getString("nome"));
                paciente.setEstadoCastracao(EstadoCastracao.valueOf(rs.getString("estadoCastracao")));
                paciente.setIdade(rs.getInt("idade"));
                paciente.setRaca(rs.getString("raca"));
                paciente.setColoracao(rs.getString("coloracao"));
                paciente.setEspecie(rs.getString("especie"));
                paciente.setFoto(rs.getBytes("foto"));
                pacientes.add(paciente);
            }
        }
        return pacientes;
    }

    public Paciente buscarPorId(int id) throws SQLException {
        Paciente paciente = null;
        String sql = "SELECT * FROM paciente WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    paciente = new Paciente();
                    paciente.setId(rs.getInt("id"));
                    paciente.setNome(rs.getString("nome"));
                    paciente.setEstadoCastracao(EstadoCastracao.valueOf(rs.getString("estadoCastracao")));
                    paciente.setIdade(rs.getInt("idade"));
                    paciente.setRaca(rs.getString("raca"));
                    paciente.setColoracao(rs.getString("coloracao"));
                    paciente.setEspecie(rs.getString("especie"));
                    paciente.setFoto(rs.getBytes("foto"));
                }
            }
        }
        return paciente;
    }
}
