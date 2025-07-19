package com.poo.blackjack.entities;

public class Jogador extends Participante {
    private String nome;

    public String getNome() {
        return nome;
    }

    public Jogador(String nome) {
        this.nome = nome;
    }
}