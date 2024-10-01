package org.example.dao;

import org.example.model.Proprietario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProprietarioDAO extends DAO {

    private static ProprietarioDAO instance;

    public ProprietarioDAO() {
        getConnection();
        createTable();
    }

    public static ProprietarioDAO getInstance() {
        return (instance == null ? (instance = new ProprietarioDAO()) : instance);
    }

    public void cadastrar(Proprietario proprietario) throws SQLException {
        String sql = "INSERT INTO proprietario (cpf, nomeCompleto, telefone, endereco) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setString(1, proprietario.getCpf());
            stmt.setString(2, proprietario.getNomeCompleto());
            stmt.setString(3, proprietario.getTelefone());
            stmt.setString(4, proprietario.getEndereco());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM proprietario WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void editar(Proprietario proprietario) throws SQLException {
        String sql = "UPDATE proprietario SET cpf = ?, nomeCompleto = ?, telefone = ?, endereco = ? WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setString(1, proprietario.getCpf());
            stmt.setString(2, proprietario.getNomeCompleto());
            stmt.setString(3, proprietario.getTelefone());
            stmt.setString(4, proprietario.getEndereco());
            stmt.setInt(5, proprietario.getId());
            stmt.executeUpdate();
        }
    }

    public List<Proprietario> listar() throws SQLException {
        List<Proprietario> proprietarios = new ArrayList<>();
        String sql = "SELECT * FROM proprietario";
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Proprietario proprietario = new Proprietario();
                proprietario.setId(rs.getInt("id"));
                proprietario.setCpf(rs.getString("cpf"));
                proprietario.setNomeCompleto(rs.getString("nome_completo"));
                proprietario.setTelefone(rs.getString("telefone"));
                proprietario.setEndereco(rs.getString("endereco"));
                proprietarios.add(proprietario);
            }
        }
        return proprietarios;
    }

    public Proprietario buscarPorId(int id) throws SQLException {
        Proprietario proprietario = null;
        String sql = "SELECT * FROM proprietario WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    proprietario = new Proprietario();
                    proprietario.setId(rs.getInt("id"));
                    proprietario.setCpf(rs.getString("cpf"));
                    proprietario.setNomeCompleto(rs.getString("nome_completo"));
                    proprietario.setTelefone(rs.getString("telefone"));
                    proprietario.setEndereco(rs.getString("endereco"));
                }
            }
        }
        return proprietario;
    }
}
