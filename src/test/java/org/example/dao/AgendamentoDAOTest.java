package org.example.dao;

import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgendamentoDAOTest {


    @BeforeEach
    void setUp() {
        DAO.getConnection();
        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
        agendamentoDAO.createTable();
    }

    @Test
    void getInstance() {
        assertNotNull(AgendamentoDAO.getInstance());
    }

    @Test
    void cadastrar() throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setPaciente(new Paciente(1, "Paciente Teste", EstadoCastracao.CASTRADO, 2, "Border Collie", "Preto e branco", "Cachorro", null));
        agendamento.setDataHora(LocalDateTime.now());
        agendamento.setServico("Servico Teste");
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setVeterinario(new Veterinario(1, null, "Veterinario Teste"));

        AgendamentoDAO.getInstance().cadastrar(agendamento);

        List<Agendamento> agendamentos = AgendamentoDAO.getInstance().listar();
        Agendamento agendamentoCadastrado = agendamentos.getFirst();
        assertFalse(agendamentos.isEmpty());
        assertEquals(agendamento.getPaciente().getId(), agendamentoCadastrado.getPaciente().getId());
        assertEquals(agendamento.getDataHora(), agendamentoCadastrado.getDataHora());
        assertEquals(agendamento.getServico(), agendamentoCadastrado.getServico());
        assertEquals(agendamento.getStatus(), agendamentoCadastrado.getStatus());
        assertEquals(agendamento.getVeterinario().getId(), agendamentoCadastrado.getVeterinario().getId());
    }

    @Test
    void excluir() throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setPaciente(new Paciente(1, "Paciente Teste", EstadoCastracao.CASTRADO, 2, "Border Collie", "Preto e branco", "Cachorro", null));
        agendamento.setDataHora(LocalDateTime.now());
        agendamento.setServico("Servico Teste");
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        AgendamentoDAO.getInstance().cadastrar(agendamento);

        AgendamentoDAO.getInstance().excluir(1);

        assertTrue(AgendamentoDAO.getInstance().listar().isEmpty());
    }

    @Test
    void editar() throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setPaciente(new Paciente(1, "Paciente Teste", EstadoCastracao.CASTRADO, 2, "Border Collie", "Preto e branco", "Cachorro", null));
        agendamento.setDataHora(LocalDateTime.now());
        agendamento.setServico("Servico Editado");
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);

        AgendamentoDAO.getInstance().editar(agendamento);
    }

    @Test
    void listar() throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setPaciente(new Paciente(1, "Paciente Teste", EstadoCastracao.CASTRADO, 2, "Border Collie", "Preto e branco", "Cachorro", null));
        agendamento.setDataHora(LocalDateTime.now());
        agendamento.setServico("Servico Teste");
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setVeterinario(new Veterinario(1, null, "Veterinario Teste"));

        AgendamentoDAO.getInstance().cadastrar(agendamento);

        List<Agendamento> agendamentos = AgendamentoDAO.getInstance().listar();
        Agendamento agendamentoCadastrado = agendamentos.getFirst();
        assertFalse(agendamentos.isEmpty());
        assertEquals(agendamento.getPaciente().getId(), agendamentoCadastrado.getPaciente().getId());
        assertEquals(agendamento.getDataHora(), agendamentoCadastrado.getDataHora());
        assertEquals(agendamento.getServico(), agendamentoCadastrado.getServico());
        assertEquals(agendamento.getStatus(), agendamentoCadastrado.getStatus());
        assertEquals(agendamento.getVeterinario().getId(), agendamentoCadastrado.getVeterinario().getId());
    }
}