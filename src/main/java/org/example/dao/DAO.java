package org.example.dao;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DAO {
    public static final String DBURL = "jdbc:sqlite:./vet2024.db";
    private static Connection con;

    public static Connection getConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(DBURL);
            } catch (SQLException e) {
                System.err.println("Exception: " + e.getMessage());
            }
        }
        return con;
    }

    protected ResultSet getResultSet(String query) {
        Statement s;
        ResultSet rs = null;
        try {
            s = con.createStatement();
            rs = s.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return rs;
    }

    protected int executeUpdate(PreparedStatement queryStatement) throws SQLException {
        return queryStatement.executeUpdate();
    }

    protected int lastId(String tableName, String primaryKey) {
        Statement s;
        int lastId = -1;
        try {
            s = con.createStatement();
            ResultSet rs = s.executeQuery("""
                        SELECT MAX(%s) AS id FROM %s
                    """.formatted(primaryKey, tableName));
            if (rs.next()) {
                lastId = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return lastId;
    }

    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    protected final boolean createTables() {
        try {
            PreparedStatement stmt;
            stmt = DAO.getConnection().prepareStatement("""
                        CREATE TABLE IF NOT EXISTS proprietario(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            cpf VARCHAR,
                            nomeCompleto VARCHAR,
                            telefone VARCHAR,
                            endereco VARCHAR
                        );
                    """);
            executeUpdate(stmt);

            stmt = DAO.getConnection().prepareStatement("""
                        CREATE TABLE IF NOT EXISTS faturamento(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            idProprietario VARCHAR,
                            valorTotal REAL,
                            status VARCHAR,
                            dataVencimento VARCHAR,
                            FOREIGN KEY (idProprietario) REFERENCES proprietario(id)
                        );
                    """);
            executeUpdate(stmt);

            stmt = DAO.getConnection().prepareStatement("""
                        CREATE TABLE IF NOT EXISTS paciente(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            nome VARCHAR,
                            estadoCastracao VARCHAR,
                            idade INTEGER,
                            raca VARCHAR,
                            coloracao VARCHAR,
                            especie VARCHAR,
                        );
                    """);
            executeUpdate(stmt);

            stmt = DAO.getConnection().prepareStatement("""
                        CREATE TABLE IF NOT EXISTS produto(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            nome VARCHAR,
                            tipo VARCHAR,
                            preco REAL
                        );
                    """);
            executeUpdate(stmt);

            stmt = DAO.getConnection().prepareStatement("""
                        CREATE TABLE IF NOT EXISTS estoque(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            idProduto INTEGER,
                            quantidade INTEGER,
                            quantidadeMinima INTEGER,
                            necessitaReposicao INTEGER,
                            FOREIGN KEY (idProduto) REFERENCES produto(id)
                        );
                    """);
            executeUpdate(stmt);

            stmt = DAO.getConnection().prepareStatement("""
                        CREATE TABLE IF NOT EXISTS agendamento(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            dataHora VARCHAR,
                            servico VARCHAR,
                            status VARCHAR,
                            idVeterinario INTEGER,
                            idPaciente INTEGER,
                            FOREIGN KEY (idVeterinario) REFERENCES veterinario(id),
                            FOREIGN KEY (idPaciente) REFERENCES paciente(id)
                        );
                    """);
            executeUpdate(stmt);
//TODO agendamento depende de veterinario e veterinario depende de agendamento, como resolver?

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}