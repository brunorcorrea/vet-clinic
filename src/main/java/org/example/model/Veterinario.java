package org.example.model;

import java.util.List;

public class Veterinario {

    private int id;
    private List<Agendamento> agendamentos;
    private String nome;

    public Veterinario() {
    }

    public Veterinario(int id, List<Agendamento> agendamentos, String nome) {
        this.id = id;
        this.agendamentos = agendamentos;
        this.nome = nome;
    }

    public void cadastrar() {
        //TODO implement this method
    }

    public void excluir() {
        //TODO implement this method
    }

    public void editar() {
        //TODO implement this method
    }

    public void agendar() {
        //TODO implement this method
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
