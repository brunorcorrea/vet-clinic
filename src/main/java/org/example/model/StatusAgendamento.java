package org.example.model;

import java.util.Objects;

public enum StatusAgendamento {

    AGENDADO("Agendado"), CONCLUIDO("Concluído"), CANCELADO("Cancelado");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public static StatusAgendamento fromDescricao(String descricao) {
        String formattedDescription = getFormattedDescription(descricao);

        for (StatusAgendamento statusAgendamento : StatusAgendamento.values()) {
            if (statusAgendamento.descricao.equals(formattedDescription)) {
                return statusAgendamento;
            }
        }

        throw new IllegalArgumentException("Descrição inválida: " + descricao);
    }

    private static String getFormattedDescription(String description) {
        String fomattedDescription = description.substring(0, 1).toUpperCase() + description.substring(1).toLowerCase();

        if (Objects.equals(fomattedDescription, "Concluido")) {
            fomattedDescription = "Concluído";
        }

        return fomattedDescription;
    }

    public String getDescricao() {
        return descricao;
    }
}
