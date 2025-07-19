package com.poo.blackjack.entities;

import java.util.Scanner;

public class Participante {
    protected Mao mao = new Mao();

    public void adicionarCarta(Carta carta) {
        this.mao.adicionarCarta(carta);
    }

    public int getPontos() {
        return this.mao.getPontos();
    }

    public void limparMao() {
        this.mao.limpar();
    }

    public Mao getMao() {
        return this.mao;
    }

    private Scanner scanner = new Scanner(System.in);

    public void realizarJogada(Shoe shoe) {
        System.out.println("\n--- Vez do Croupier ---");
        System.out.println("Mão do Croupier: " + this.mao + " (" + getPontos() + " pontos)");
        while (getPontos() < 17) {
            System.out.println("Croupier compra uma carta...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Carta novaCarta = shoe.comprarCarta();
            adicionarCarta(novaCarta);
            System.out.println("Croupier comprou um(a) " + novaCarta);
            System.out.println("Nova mão do Croupier: " + this.mao + " (" + getPontos() + " pontos)");
        }
    }

    public void realizarJogada(Shoe shoe, Jogador jogador) {
        while (true) {
            System.out.println("\n" + jogador.getNome() + ", sua mão: " + jogador.mao + " (" + getPontos() + " pontos)");
            if (getPontos() >= 21) {
                break;
            }

            System.out.print("Digite (1) para COMPRAR uma carta ou (2) para PARAR: ");
            String escolha = scanner.nextLine();
            if ("1".equals(escolha)) {
                Carta novaCarta = shoe.comprarCarta();
                adicionarCarta(novaCarta);
                System.out.println(jogador.getNome() + " comprou um(a) " + novaCarta);
            } else if ("2".equals(escolha)) {
                System.out.println(jogador.getNome() + " decidiu parar.");
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}