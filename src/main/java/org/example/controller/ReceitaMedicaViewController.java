package org.example.controller;

import org.example.model.Paciente;
import org.example.model.ReceitaMedica;
import org.example.view.tablemodels.ReceitaMedicaTableModel;

import java.time.LocalDateTime;
import java.util.List;

public class ReceitaMedicaViewController {
    private final ReceitaMedicaController receitaMedicaController = ReceitaMedicaController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();

    public List<Paciente> listarPacientes() throws Exception {
        return pacienteController.listarPacientes();
    }

    public void adicionarReceitaMedica(Paciente paciente, List<String> medicamentos, List<String> observacoes, LocalDateTime dataEmissao) throws Exception {
        ReceitaMedica receitaMedica = new ReceitaMedica();
        receitaMedica.setPaciente(paciente);
        receitaMedica.setMedicamentos(medicamentos);
        receitaMedica.setObservacoes(observacoes);
        receitaMedica.setDataEmissao(dataEmissao);
        receitaMedicaController.adicionarReceitaMedica(receitaMedica);
    }

    public void removerReceitaMedica(int receitaMedicaId) throws Exception {
        ReceitaMedica receitaMedica = new ReceitaMedica();
        receitaMedica.setId(receitaMedicaId);
        receitaMedicaController.removerReceitaMedica(receitaMedica);
    }

    public List<ReceitaMedica> listarReceitasMedica() throws Exception {
        return receitaMedicaController.listarReceitasMedica();
    }

    public ReceitaMedicaTableModel criarReceitaMedicaTableModel(String nomePaciente) throws Exception {
        List<ReceitaMedica> receitasMedicas = listarReceitasMedica();

        if (nomePaciente != null && !nomePaciente.isEmpty()) {
            filtrarReceitasMedicas(receitasMedicas, nomePaciente);
        }

        return new ReceitaMedicaTableModel(receitasMedicas);
    }

    private void filtrarReceitasMedicas(List<ReceitaMedica> receitasMedicas, String nomePaciente) {
        receitasMedicas.removeIf(receitaMedica -> shouldRemoveReceitasMedica(nomePaciente, receitaMedica));
    }

    private boolean shouldRemoveReceitasMedica(String nomePaciente, ReceitaMedica receitaMedica) {
        return receitaMedica == null || receitaMedica.getPaciente() == null ||
                receitaMedica.getPaciente().getNome() == null ||
                !receitaMedica.getPaciente().getNome().toLowerCase().contains(nomePaciente.toLowerCase());
    }
}