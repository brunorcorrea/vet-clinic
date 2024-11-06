package org.example.controller;

import org.example.model.EstadoCastracao;
import org.example.model.Paciente;
import org.example.model.Proprietario;
import org.example.view.tablemodels.PacienteTableModel;

import java.util.List;

public class PacienteViewController {
    private final PacienteController pacienteController = PacienteController.getInstance();
    private final ProprietarioController proprietarioController = ProprietarioController.getInstance();

    public List<Proprietario> listarProprietarios() throws Exception {
        return proprietarioController.listarProprietarios();
    }

    public void adicionarPaciente(String nome, String estadoCastracao, String raca, int idade, String coloracao, String especie, byte[] foto, Proprietario proprietario) throws Exception {
        Paciente paciente = new Paciente();
        paciente.setNome(nome);
        paciente.setEstadoCastracao(EstadoCastracao.fromDescricao(estadoCastracao));
        paciente.setIdade(idade);
        paciente.setRaca(raca);
        paciente.setColoracao(coloracao);
        paciente.setEspecie(especie);
        paciente.setFoto(foto);
        paciente.setProprietario(proprietario);
        pacienteController.adicionarPaciente(paciente);
    }

    public void removerPaciente(int pacienteId) throws Exception {
        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);
        pacienteController.removerPaciente(paciente);
    }

    public List<Paciente> listarPacientes() throws Exception {
        return pacienteController.listarPacientes();
    }

    public PacienteTableModel criarPacienteTableModel(String nomeProprietario) throws Exception {
        List<Paciente> pacientes = listarPacientes();
        if (nomeProprietario != null && !nomeProprietario.isEmpty()) {
            filtrarPacientes(pacientes, nomeProprietario);
        }

        return new PacienteTableModel(pacientes);
    }

    private void filtrarPacientes(List<Paciente> pacientes, String nomeProprietario) {
        pacientes.removeIf(paciente -> shouldRemovePaciente(nomeProprietario, paciente));
    }

    private boolean shouldRemovePaciente(String nomeProprietario, Paciente paciente) {
        return paciente != null && paciente.getProprietario() != null &&
                !paciente.getProprietario().getNomeCompleto().toLowerCase().contains(nomeProprietario.toLowerCase()); //TODO if proprietario is null, should it be removed, yes, it should
    }
}