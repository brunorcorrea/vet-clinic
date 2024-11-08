package org.example.model;

import java.util.Objects;

public enum StatusPagamento {

    PAGO("Pago"), PENDENTE("Pendente"), EM_ATRASO("Em Atraso");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public static StatusPagamento fromDescricao(String descricao) {
        String formattedDescription = getFormattedDescription(descricao);

        for (StatusPagamento statusPagamento : StatusPagamento.values()) {
            if (statusPagamento.descricao.equals(formattedDescription)) {
                return statusPagamento;
            }
        }

        throw new IllegalArgumentException("Descrição inválida: " + descricao);
    }


    private static String getFormattedDescription(String description) {
        String fomattedDescription = description.substring(0, 1).toUpperCase() + description.substring(1).toLowerCase();

        if (Objects.equals(fomattedDescription, "Em atraso") || Objects.equals(fomattedDescription, "Em Atraso")) {
            fomattedDescription = "Em Atraso";
        }

        return fomattedDescription;
    }

    public String getDescricao() {
        return descricao;
    }
}
