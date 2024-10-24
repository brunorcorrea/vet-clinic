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

    public ReceitaMedicaTableModel criarReceitaMedicaTableModel() throws Exception {
        List<ReceitaMedica> receitasMedicas = listarReceitasMedica();
        return new ReceitaMedicaTableModel(receitasMedicas);
    }
}