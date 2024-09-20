package org.example.model;

import java.time.LocalDateTime;

public class Agendamento {

    private int id;
    private Paciente paciente;
    private LocalDateTime dataHora;
    private String servico;
    private StatusAgendamento status;

    public void cadastrar() {
        //TODO implement this method
    }

    public void excluir() {
        //TODO implement this method
    }

    public void editar() {
        //TODO implement this method
    }

    public void listar() {
        //TODO implement this method
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
}
