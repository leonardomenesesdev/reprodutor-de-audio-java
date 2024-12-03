package Sounds;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Musicas  {
    private MediaPlayer mediaPlayer;
    private Media media;
    private String audioSelecionado;
    private DefaultListModel<String> playlistModel;
    private ArrayList<String> playlist = new ArrayList<>();
    private boolean terminou = false;
    private List<String> linhas = new ArrayList<>();
    private int index;
    public Musicas(DefaultListModel<String> playlistModel) {
        this.playlistModel = playlistModel;
    }

    public void escolherArquivo(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int escolha = fileChooser.showOpenDialog(null);
        if(escolha == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null,
                    "Operação cancelada",
                    "Reprodutor Av3",
                    JOptionPane.ERROR_MESSAGE);
        } else{
            JOptionPane.showMessageDialog(null,
                    "Arquivo: "+fileChooser.getSelectedFile().toPath(),
                    "Reprodutor Av3",
                    JOptionPane.INFORMATION_MESSAGE);
            audioSelecionado = fileChooser.getSelectedFile().toPath().toString(); //Talvez n precise do toString
        }
    }
    public void iniciaPrograma(JProgressBar progressBar, JLabel tempoAtual) throws InterruptedException {
        media = new Media(new File(audioSelecionado).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        Thread.sleep(3000);
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                progressBar.setValue((int) (progress * 100));
                double tempo = mediaPlayer.getCurrentTime().toMinutes();
                String tempoString = String.format("%.2f", tempo);
                tempoAtual.setText(tempoString);
            }
        });
    }
    public String getAudioSelecionado() {
        return audioSelecionado;
    }
    public void toca() {
        mediaPlayer.play();
    }

    public void pausa() {
        mediaPlayer.pause();
    }

    public void parar() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }

    public void procuraPlaylist(TextArea textArea){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int escolha = fileChooser.showOpenDialog(null);
        if(escolha == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null,
                    "Operação cancelada");
        } else{
            FileInputStream entrada = null;
            var arquivo = fileChooser.getSelectedFile().toPath().toString();
            playlist.add(fileChooser.getSelectedFile().toPath().toString());
            textArea.append(new File(arquivo).getName());
        }
    }

    public void salvarPlaylist(JTextField textField){
        File arq = new File(textField.getText()+".txt");
        try {
            FileWriter escritor = new FileWriter(arq, true);
            for(String musica:playlist){
                escritor.write(musica+"\n");
            }
            escritor.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String criaPlaylist(){
        String playlist = JOptionPane.showInputDialog(null, "Digite o nome do playlist");
        return playlist;
    }

    public String duracao() {
        double duracao = mediaPlayer.getMedia().getDuration().toMinutes();
        String duracaoString = String.format("%.2f", duracao);
        return duracaoString;
    }


    public List carregaPlaylist(String caminhoArquivo, JList list){
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))){
            String linha;
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String musica : linhas) {
                listModel.addElement(new File(musica).getName());
            }
            list.setModel(listModel);
            return linhas;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao carregar ou reproduzir playlist: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public void removeMusica(JList list){
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                index = list.locationToIndex(e.getPoint());
                String musicaSelecionada = (String) list.getModel().getElementAt(index);
                JOptionPane.showMessageDialog(null, "Removendo "+musicaSelecionada);
                linhas.remove(index);

                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (String musica : linhas) {
                    listModel.addElement(new File(musica).getName());
                }
                list.setModel(listModel);
            }
        });

    }

    public void reproduzEscolha(JList list, JProgressBar progressBar, JLabel tempoAtual, JLabel tempoFinal, JLabel songTitle ) {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                try {
                    Media media = new Media(new File(linhas.get(index)).toURI().toString());
                    mediaPlayer.stop();
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                        @Override
                        public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                            double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                            progressBar.setValue((int) (progress * 100));
                            double tempo = mediaPlayer.getCurrentTime().toMinutes();
                            String tempoString = String.format("%.2f", tempo);
                            tempoAtual.setText(tempoString);
                            double tempoFim = mediaPlayer.getStopTime().toMinutes();
                            String tempoFinalString = String.format("%.2f", tempoFim);
                            tempoFinal.setText(tempoFinalString);
                        }
                    });
                    songTitle.setText(new File(linhas.get(index)).getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao reproduzir música: " + ex.getMessage());
                }
            }
        });
    }

    public synchronized void reproduzirPlaylist(final List<String> playlist, JProgressBar progressBar, JLabel tempoAtual, JLabel tempoFinal, JLabel songTitle) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < playlist.size(); i++) {
                    var arquivo = new File(playlist.get(i)).toURI().toString();
                    media = new Media(new File(playlist.get(i)).toURI().toString());
                    var nomeMusica = playlist.get(i);
                    songTitle.setText(new File(nomeMusica).getName());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                        @Override
                        public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                            double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                            progressBar.setValue((int) (progress * 100));
                            double tempo = mediaPlayer.getCurrentTime().toMinutes();
                            String tempoString = String.format("%.2f", tempo);
                            tempoAtual.setText(tempoString);
                            double tempoFim = mediaPlayer.getStopTime().toMinutes();
                            String tempoFinalString = String.format("%.2f", tempoFim);
                            tempoFinal.setText(tempoFinalString);
                        }
                    });
                    final CountDownLatch latch = new CountDownLatch(1);

                    mediaPlayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer.stop();
                            mediaPlayer.dispose();
                            latch.countDown();
                        }
                    });
                    mediaPlayer.play();
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    public void paraReproducao(){
        mediaPlayer.stop();
    }
    public void avancarSegundos(){
        double tempoAtual = mediaPlayer.getCurrentTime().toSeconds();
        double avancarTempo = tempoAtual+10;
        if(avancarTempo>mediaPlayer.getTotalDuration().toSeconds()){
            avancarTempo = mediaPlayer.getTotalDuration().toSeconds();
        }
        mediaPlayer.seek(Duration.seconds(avancarTempo));
    }

    public void voltarSegundos() {
        double tempoAtual = mediaPlayer.getCurrentTime().toSeconds();
        double voltarTempo = tempoAtual - 10;
        if (voltarTempo == mediaPlayer.getStartTime().toSeconds()) {
            voltarTempo = mediaPlayer.getStartTime().toSeconds();
        }
        mediaPlayer.seek(Duration.seconds(voltarTempo));
    }

    public void volume(JSlider sliderVolume){
        double volume = sliderVolume.getValue() / 100.0;
        mediaPlayer.setVolume(volume);
    }

    public void adicionaNaPlaylist() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int escolha = fileChooser.showOpenDialog(null);
        if (escolha == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "Operação cancelada");
            return;
        }
        String caminhoPlaylist = fileChooser.getSelectedFile().toPath().toString();
        FileWriter escritor = new FileWriter(caminhoPlaylist, true);
        JFileChooser musicaChooser = new JFileChooser();
        musicaChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int escolhaMusica = musicaChooser.showOpenDialog(null);
        if (escolhaMusica == JFileChooser.APPROVE_OPTION) {
            String musicaNova = musicaChooser.getSelectedFile().toPath().toString();
            playlist.add(musicaNova);
            escritor.write(musicaNova + "\n");
            JOptionPane.showMessageDialog(null, "Música adicionada");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma música foi selecionada");
        }

        escritor.close();
    }

    //METODOS PARA MUSICA ANTERIOR E PROXIMA , COM SELETOR DE MUSICA ( TrocarMusica) -ENZO
    private void trocarMusica(String caminhoArquivo) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        media = new Media(new File(caminhoArquivo).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public int indiceAtual = -1;
    public void musicaAnterior(JLabel songTitle, JProgressBar progressBar, JLabel tempoAtual, JLabel tempoFinal) {
        if (linhas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "A playlist está vazia.");
            return;
        }

        if (indiceAtual > 0) {
            indiceAtual--;
            trocarMusica(linhas.get(indiceAtual)); // Troca pra música anterior
            songTitle.setText(new File(linhas.get(indiceAtual)).getName());
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                    progressBar.setValue((int) (progress * 100));
                    double tempo = mediaPlayer.getCurrentTime().toMinutes();
                    String tempoString = String.format("%.2f", tempo);
                    tempoAtual.setText(tempoString);
                    double tempoFim = mediaPlayer.getStopTime().toMinutes();
                    String tempoFinalString = String.format("%.2f", tempoFim);
                    tempoFinal.setText(tempoFinalString);
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Você já está na primeira música.");
        }
    }

    public void proximaMusica(JLabel songTitle, JProgressBar progressBar, JLabel tempoAtual, JLabel tempoFinal) {
        if (linhas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "A playlist está vazia.");
            return;
        }
        if (indiceAtual < linhas.size() - 1) {
            indiceAtual++;
            trocarMusica(linhas.get(indiceAtual));
            songTitle.setText(new File(linhas.get(indiceAtual)).getName());
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                    progressBar.setValue((int) (progress * 100));
                    double tempo = mediaPlayer.getCurrentTime().toMinutes();
                    String tempoString = String.format("%.2f", tempo);
                    tempoAtual.setText(tempoString);
                    double tempoFim = mediaPlayer.getStopTime().toMinutes();
                    String tempoFinalString = String.format("%.2f", tempoFim);
                    tempoFinal.setText(tempoFinalString);
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Você já está na última música.");
        }
    }
}


