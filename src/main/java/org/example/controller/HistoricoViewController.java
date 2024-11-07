package org.example.controller;

import org.example.model.Historico;
import org.example.model.Paciente;
import org.example.view.tablemodels.HistoricoTableModel;

import java.time.LocalDateTime;
import java.util.List;

public class HistoricoViewController {
    private final HistoricoController historicoController = HistoricoController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();

    public List<Paciente> listarPacientes() throws Exception {
        return pacienteController.listarPacientes();
    }

    public void adicionarHistorico(Paciente paciente, List<String> vacinas, List<String> doencas, String peso, List<String> observacoes, LocalDateTime dataHora) throws Exception {
        Historico historico = new Historico();
        historico.setPaciente(paciente);
        historico.setVacinas(vacinas);
        historico.setDoencas(doencas);
        historico.setPeso(peso);
        historico.setDataHora(dataHora);
        historico.setObservacoes(observacoes);
        historicoController.adicionarHistorico(historico);
    }

    public void removerHistorico(int historicoId) throws Exception {
        Historico historico = new Historico();
        historico.setId(historicoId);
        historicoController.removerHistorico(historico);
    }

    public List<Historico> listarHistoricos() throws Exception {
        return historicoController.listarHistoricos();
    }

    public HistoricoTableModel criarHistoricoTableModel(String nomePaciente) throws Exception {
        List<Historico> historicos = listarHistoricos();

        if (nomePaciente != null && !nomePaciente.isEmpty()) {
            filtrarHistoricos(historicos, nomePaciente);
        }

        return new HistoricoTableModel(historicos);
    }

    private void filtrarHistoricos(List<Historico> historicos, String nomePaciente) {
        historicos.removeIf(historico -> shouldRemoveHistorico(nomePaciente, historico));
    }

    private boolean shouldRemoveHistorico(String nomePaciente, Historico historico) {
        return historico != null && historico.getPaciente() != null &&
                !historico.getPaciente().getNome().toLowerCase().contains(nomePaciente.toLowerCase()); //TODO if paciente is null, should it be removed, yes, it should
    }
}