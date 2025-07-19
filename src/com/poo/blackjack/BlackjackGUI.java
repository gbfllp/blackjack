package com.poo.blackjack;

import com.poo.blackjack.entities.Carta;
import com.poo.blackjack.entities.Jogador;
import com.poo.blackjack.entities.Participante;
import com.poo.blackjack.entities.Shoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGUI extends JFrame {

    private List<Jogador> jogadores;
    private Participante croupier;
    private Shoe shoe;
    private int currentPlayerIndex;

    private JPanel dealerPanel;
    private JPanel playersAreaPanel;
    private List<JPanel> playerHandPanels;
    private List<JLabel> playerScoreLabels;

    private JButton hitButton;
    private JButton standButton;
    private JButton newGameButton;

    private JLabel dealerScoreLabel;
    private JLabel messageLabel;

    public BlackjackGUI() {
        setTitle("Mesa de Blackjack Multiplayer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(0, 102, 0));
        setContentPane(tablePanel);

        Integer[] options = {1, 2, 3};
        int numJogadores = (Integer) JOptionPane.showInputDialog(this,
                "Selecione o número de jogadores:",
                "Bem-vindo ao Blackjack!",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        jogadores = new ArrayList<>();
        for (int i = 0; i < numJogadores; i++) {
            String nome = JOptionPane.showInputDialog(this, "Digite o nome do Jogador " + (i + 1) + ":");
            if (nome == null || nome.trim().isEmpty()) {
                nome = "Jogador " + (i + 1);
            }
            jogadores.add(new Jogador(nome));
        }
        this.croupier = new Participante();

        setupUI(numJogadores);

        add(dealerPanel, BorderLayout.NORTH);
        add(playersAreaPanel, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        setVisible(true);
        startNewRound();
    }

    private void setupUI(int numJogadores) {
        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Mão do Croupier"));
        dealerPanel.setOpaque(false);

        playersAreaPanel = new JPanel(new GridLayout(1, numJogadores, 10, 10));
        playersAreaPanel.setOpaque(false);

        playerHandPanels = new ArrayList<>();
        playerScoreLabels = new ArrayList<>();

        for (Jogador jogador : jogadores) {
            JPanel playerContainer = new JPanel(new BorderLayout());
            playerContainer.setOpaque(false);

            JPanel handPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            handPanel.setOpaque(false);
            handPanel.setBorder(BorderFactory.createTitledBorder(jogador.getNome()));

            JLabel scoreLabel = new JLabel("Pontos: 0");
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

            playerContainer.add(handPanel, BorderLayout.CENTER);
            playerContainer.add(scoreLabel, BorderLayout.SOUTH);

            playerHandPanels.add(handPanel);
            playerScoreLabels.add(scoreLabel);
            playersAreaPanel.add(playerContainer);
        }
    }

    private JPanel createControlPanel() {
        JPanel mainControlPanel = new JPanel(new BorderLayout());
        mainControlPanel.setOpaque(false);

        dealerScoreLabel = new JLabel("Croupier: ?");
        dealerScoreLabel.setForeground(Color.WHITE);
        dealerScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainControlPanel.add(dealerScoreLabel, BorderLayout.NORTH);

        messageLabel = new JLabel("Nova rodada! Faça sua jogada.");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 18));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainControlPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        hitButton = new JButton("Comprar");
        standButton = new JButton("Parar");
        newGameButton = new JButton("Nova Rodada");
        newGameButton.setVisible(false);

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);
        mainControlPanel.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(e -> playerHits());
        standButton.addActionListener(e -> playerStands());
        newGameButton.addActionListener(e -> startNewRound());

        return mainControlPanel;
    }

    private void startNewRound() {
        shoe = new Shoe(4);
        croupier.limparMao();
        for (Jogador jogador : jogadores) {
            jogador.limparMao();
        }

        for (int i = 0; i < 2; i++) {
            for (Jogador jogador : jogadores) {
                jogador.adicionarCarta(shoe.comprarCarta());
            }
            croupier.adicionarCarta(shoe.comprarCarta());
        }

        currentPlayerIndex = 0;
        newGameButton.setVisible(false);
        updateActivePlayerUI();
        updateUI(false);

        checkPlayerStatus();
    }

    private void nextPlayerTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex < jogadores.size()) {
            updateActivePlayerUI();
            checkPlayerStatus();
        } else {
            croupierPlays();
        }
    }

    private void checkPlayerStatus() {
        Jogador currentPlayer = jogadores.get(currentPlayerIndex);
        if (currentPlayer.getPontos() >= 21) {
            Timer timer = new Timer(1000, e -> playerStands());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void playerHits() {
        Jogador currentPlayer = jogadores.get(currentPlayerIndex);
        currentPlayer.adicionarCarta(shoe.comprarCarta());
        updateUI(false);

        if (currentPlayer.getPontos() > 21) {
            messageLabel.setText(currentPlayer.getNome() + " estourou!");
            Timer timer = new Timer(1500, e -> nextPlayerTurn());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void playerStands() {
        Jogador currentPlayer = jogadores.get(currentPlayerIndex);
        messageLabel.setText(currentPlayer.getNome() + " parou.");

        Timer timer = new Timer(1000, e -> nextPlayerTurn());
        timer.setRepeats(false);
        timer.start();
    }

    private void croupierPlays() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        messageLabel.setText("Vez do Croupier...");
        updateUI(true);

        Timer croupierTimer = new Timer(1500, null);
        croupierTimer.addActionListener(e -> {
            if (croupier.getPontos() < 17) {
                messageLabel.setText("Croupier compra uma carta...");
                croupier.adicionarCarta(shoe.comprarCarta());
                updateUI(true);
            } else {
                croupierTimer.stop();
                determineWinner();
            }
        });
        croupierTimer.setRepeats(true);
        croupierTimer.start();
    }

    private void determineWinner() {
        int pontosCroupier = croupier.getPontos();
        StringBuilder finalMessage = new StringBuilder("<html>Fim da rodada!<br><br>");

        for(Jogador jogador : jogadores) {
            int pontosJogador = jogador.getPontos();
            finalMessage.append("<b>").append(jogador.getNome()).append("</b>: ");
            if (pontosJogador > 21) {
                finalMessage.append("Perdeu (Estourou!).<br>");
            } else if (pontosCroupier > 21 || pontosJogador > pontosCroupier) {
                finalMessage.append("Venceu!<br>");
            } else if (pontosJogador < pontosCroupier) {
                finalMessage.append("Perdeu para o Croupier.<br>");
            } else {
                finalMessage.append("Empate!<br>");
            }
        }
        finalMessage.append("</html>");
        messageLabel.setText(finalMessage.toString());
        endRound();
    }

    private void endRound() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        newGameButton.setVisible(true);
        updateUI(true);
    }

    private void updateActivePlayerUI() {
        for (int i = 0; i < playerHandPanels.size(); i++) {
            JPanel panel = playerHandPanels.get(i);
            String playerName = jogadores.get(i).getNome();
            if (i == currentPlayerIndex) {
                panel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.YELLOW, 2), playerName));
                messageLabel.setText("É a vez de " + playerName);
                hitButton.setEnabled(true);
                standButton.setEnabled(true);
            } else {
                panel.setBorder(BorderFactory.createTitledBorder(playerName));
            }
        }
    }

    private void updateUI(boolean revealDealerHand) {
        dealerPanel.removeAll();
        if (revealDealerHand) {
            for (Carta carta : croupier.getMao().getCartas()) {
                dealerPanel.add(new CardPanel(carta));
            }
            dealerScoreLabel.setText("Croupier: " + croupier.getPontos());
        } else {
            if (!croupier.getMao().getCartas().isEmpty()) {
                dealerPanel.add(new CardPanel(null));
                dealerPanel.add(new CardPanel(croupier.getMao().getCartas().get(1)));
            }
            dealerScoreLabel.setText("Croupier: ?");
        }

        for (int i = 0; i < jogadores.size(); i++) {
            JPanel handPanel = playerHandPanels.get(i);
            handPanel.removeAll();
            for (Carta carta : jogadores.get(i).getMao().getCartas()) {
                handPanel.add(new CardPanel(carta));
            }
            playerScoreLabels.get(i).setText("Pontos: " + jogadores.get(i).getPontos());
            handPanel.revalidate();
            handPanel.repaint();
        }

        dealerPanel.revalidate();
        dealerPanel.repaint();

        if (currentPlayerIndex >= jogadores.size()) {
            for (int i = 0; i < playerHandPanels.size(); i++) {
                playerHandPanels.get(i).setBorder(BorderFactory.createTitledBorder(jogadores.get(i).getNome()));
            }
        }
    }

    private class CardPanel extends JPanel {
        private Carta carta;

        public CardPanel(Carta carta) {
            this.carta = carta;
            setPreferredSize(new Dimension(80, 120));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (carta != null) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                String valor = carta.getValor();
                String naipe = carta.getNaipe();
                g.drawString(valor, 10, 20);
                g.drawString(valor, getWidth() - (g.getFontMetrics().stringWidth(valor) + 10), getHeight() - 10);
                g.setFont(new Font("Arial", Font.PLAIN, 32));
                if ("Copas".equals(naipe) || "Ouros".equals(naipe)) {
                    g.setColor(Color.RED);
                }
                g.drawString(getNaipeSymbol(naipe), getWidth() / 2 - 10, getHeight() / 2 + 10);
            } else {
                g.setColor(new Color(0, 0, 139));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
            }
        }

        private String getNaipeSymbol(String naipe) {
            switch(naipe) {
                case "Copas": return "♥";
                case "Ouros": return "♦";
                case "Paus": return "♣";
                case "Espadas": return "♠";
                default: return "";
            }
        }
    }
}