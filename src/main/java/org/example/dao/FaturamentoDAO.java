package org.example.dao;

import org.example.model.Faturamento;
import org.example.model.Proprietario;
import org.example.model.StatusPagamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FaturamentoDAO extends DAO {
    private static FaturamentoDAO instance;

    FaturamentoDAO() {
        getConnection();
        createTable();
    }

    public static FaturamentoDAO getInstance() {
        return (instance == null ? (instance = new FaturamentoDAO()) : instance);
    }

    public void cadastrar(Faturamento faturamento) throws SQLException {
        String sql = "INSERT INTO faturamento (idProprietario, valorTotal, status, dataVencimento) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, faturamento.getProprietario().getId());
            stmt.setDouble(2, faturamento.getValorTotal());
            stmt.setString(3, faturamento.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(faturamento.getDataVencimento()));
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM faturamento WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void excluirPorProprietario(int proprietarioId) throws SQLException {
        String sql = "DELETE FROM faturamento WHERE idProprietario = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, proprietarioId);
            stmt.executeUpdate();
        }
    }

    public void editar(Faturamento faturamento) throws SQLException {
        String sql = "UPDATE faturamento SET idProprietario = ?, valorTotal = ?, status = ?, dataVencimento = ? WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, faturamento.getProprietario().getId());
            stmt.setDouble(2, faturamento.getValorTotal());
            stmt.setString(3, faturamento.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(faturamento.getDataVencimento()));
            stmt.setInt(5, faturamento.getId());
            stmt.executeUpdate();
        }
    }

    public List<Faturamento> listar() throws SQLException {
        List<Faturamento> faturamentos = new ArrayList<>();
        String sql = "SELECT * FROM faturamento";
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Faturamento faturamento = new Faturamento();
                faturamento.setId(rs.getInt("id"));
                faturamento.setProprietario(buscarProprietarioPorId(rs.getInt("idProprietario")));
                faturamento.setValorTotal(rs.getDouble("valorTotal"));
                faturamento.setStatus(StatusPagamento.valueOf(rs.getString("status")));
                faturamento.setDataVencimento(rs.getTimestamp("dataVencimento").toLocalDateTime());
                faturamentos.add(faturamento);
            }
        }
        return faturamentos;
    }

    private Proprietario buscarProprietarioPorId(int id) {
        Proprietario proprietario = new Proprietario();
        String sql = "SELECT * FROM proprietario WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    proprietario.setId(rs.getInt("id"));
                    proprietario.setNomeCompleto(rs.getString("nomeCompleto"));
                    proprietario.setEndereco(rs.getString("endereco"));
                    proprietario.setTelefone(rs.getString("telefone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proprietario;
    }
}
