package org.example.controller;

import org.example.model.Agendamento;
import org.example.model.Paciente;
import org.example.model.StatusAgendamento;
import org.example.model.Veterinario;
import org.example.view.tablemodels.AgendamentoTableModel;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoViewController {
    private final AgendamentoController agendamentoController = AgendamentoController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();
    private final VeterinarioController veterinarioController = VeterinarioController.getInstance();

    public List<Paciente> listarPacientes() throws Exception {
        return pacienteController.listarPacientes();
    }

    public List<Veterinario> listarVeterinarios() throws Exception {
        return veterinarioController.listarVeterinarios();
    }

    public void adicionarAgendamento(Paciente paciente, Veterinario veterinario, String servico, StatusAgendamento status, LocalDateTime dataHora) throws Exception {
        Agendamento agendamento = new Agendamento();
        agendamento.setPaciente(paciente);
        agendamento.setVeterinario(veterinario);
        agendamento.setServico(servico);
        agendamento.setStatus(status);
        agendamento.setDataHora(dataHora);
        agendamentoController.adicionarAgendamento(agendamento);
    }

    public void removerAgendamento(int agendamentoId) throws Exception {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(agendamentoId);
        agendamentoController.removerAgendamento(agendamento);
    }

    public List<Agendamento> listarAgendamentos() throws Exception {
        return agendamentoController.listarAgendamentos();
    }

    public AgendamentoTableModel criarAgendamentoTableModel() throws Exception {
        List<Agendamento> agendamentos = listarAgendamentos();
        return new AgendamentoTableModel(agendamentos);
    }
}