package org.example.model;

import java.util.Objects;

public enum EstadoCastracao {

    FERTIL("Fértil"), CASTRADO("Castrado");

    private final String descricao;

    EstadoCastracao(String descricao) {
        this.descricao = descricao;
    }

    public static EstadoCastracao fromDescricao(String descricao) {
        String formattedDescription = getFormattedDescription(descricao);

        for (EstadoCastracao estadoCastracao : EstadoCastracao.values()) {
            if (estadoCastracao.descricao.equals(formattedDescription)) {
                return estadoCastracao;
            }
        }

        throw new IllegalArgumentException("Descrição inválida: " + descricao);
    }

    private static String getFormattedDescription(String description) {
        String fomattedDescription = description.substring(0, 1).toUpperCase() + description.substring(1).toLowerCase();

        if (Objects.equals(fomattedDescription, "Fertil")) {
            fomattedDescription = "Fértil";
        }

        return fomattedDescription;
    }

    public String getDescricao() {
        return descricao;
    }
}
