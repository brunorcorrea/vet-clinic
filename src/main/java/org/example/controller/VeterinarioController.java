package org.example.controller;

import org.example.model.Veterinario;

import java.util.Collections;
import java.util.List;

public class VeterinarioController {

    public VeterinarioController() {

    }

    public List<Veterinario> listarVeterinarios() {
//        return null; //TODO search in VeterinarioDAO
        return List.of(new Veterinario(1, Collections.emptyList(), "Bruno"));
    }
}
