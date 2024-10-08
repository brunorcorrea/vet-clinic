package org.example.model;

public enum StatusAgendamento {

    AGENDADO("Agendado"), CONCLUIDO("Concluído"), CANCELADO("Cancelado");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public static StatusAgendamento fromDescricao(String descricao) {
        for (StatusAgendamento statusAgendamento : StatusAgendamento.values()) {
            if (statusAgendamento.descricao.equals(descricao)) {
                return statusAgendamento;
            }
        }
        return null;
    }

    public String getDescricao() {
        return descricao;
    }
}
