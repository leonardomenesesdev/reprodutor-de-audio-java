package testtt;

import Sounds.Musicas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Testtt {
    private JPanel panel1;
    private JButton backButton;
    private JButton nextButton;
    private JButton save;
    private JButton playButton;
    private JPanel Jpanelimg;
    private JButton pauseButton;
    private JPanel panel2;
    private JButton addMusic;
    private JList list1;
    private JButton listButton;
    private JProgressBar progressBar1;
    private JLabel JlabelEnd;
    private JLabel JLabelBegin;
    private JTextField textField1;
    private JButton novaPlaylist;
    private JButton escolherMusica;
    private JTable table1;
    private JList<String> playlistList;
    private DefaultListModel<String> playlistModel;
    private List<String> playlistCarregada;
    private Musicas mp3;
    private String caminhoArquivo;
    private boolean excluiClicado = false;
    public Testtt() {
        JFrame frame = new JFrame("Music Player");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("F");
        }
        //Area de texto
        TextArea area = new TextArea();
        area.setBounds(150, 30, 150, 150);
        frame.add(area);
        area.setVisible(false);

        JList list = new JList<>();
        list.setBounds(160, 30, 150, 150);
        frame.add(list);

        mp3 = new Musicas(playlistModel);

        JLabel songTitleLabel = new JLabel("Song Title", SwingConstants.CENTER);
        songTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        songTitleLabel.setBounds(135, 200, 200, 50);
        frame.add(songTitleLabel);

        JProgressBar progressBar1 = new JProgressBar();
        progressBar1.setBounds(105, 280, 240, 10);
        progressBar1.setValue(0);
        frame.add(progressBar1);

        JLabel JLabelBegin = new JLabel("0.00");
        JLabelBegin.setBounds(75, 280, 50,10 );
        frame.add(JLabelBegin);

        JLabel JLabelEnd = new JLabel("0.00");
        JLabelEnd.setBounds(355, 280, 50,10 );
        frame.add(JLabelEnd);

        JButton prevButton = new JButton("↩");
        prevButton.setBounds(135, 320, 50, 50);
        frame.add(prevButton);


        JButton escolherMusica = new JButton();
        escolherMusica.setText("Adicionar músicas");
        escolherMusica.setBounds(165, 500, 150, 30);
        escolherMusica.setVisible(false);
        frame.add(escolherMusica);

        JButton playButton = new JButton("▶");
        playButton.setBounds(200, 320, 50, 50);
        frame.add(playButton);

        JButton nextButton = new JButton("↪");
        nextButton.setBounds(265, 320, 50, 50);
        frame.add(nextButton);

        JSlider sliderVolume = new JSlider();
        sliderVolume.setBounds(5, 290, 20, 100);
        sliderVolume.setOrientation(JSlider.VERTICAL);
        sliderVolume.setValue(100);
        frame.add(sliderVolume);

        JButton pauseButton = new JButton("||");
        pauseButton.setBounds(200, 320, 50, 50);
        frame.add(pauseButton);

        JButton antesMusic = new JButton("⏪");
        antesMusic.setBounds(70, 320, 50, 50);
        frame.add(antesMusic);

        JButton proximaMusic = new JButton("⏩");
        proximaMusic.setBounds(330, 320, 50, 50);
        frame.add(proximaMusic);

        JButton paraMusica = new JButton("⏹");
        paraMusica.setBounds(400, 260, 55, 50);
        frame.add(paraMusica);

        JButton addMusic = new JButton("☰");
        addMusic.setBounds(5, 25, 60, 40);
        frame.add(addMusic);

        JLabel load = new JLabel("Carregar música");
        load.setBounds(5, 60, 100, 40);
        frame.add(load);

        JLabel listButton = new JLabel("Playlist", SwingConstants.CENTER);
        listButton.setBounds(200, 400, 80, 30);
        listButton.setFont(new Font("Arial", Font.BOLD, 16));

        frame.add(listButton);

        JTextField textField1 = new JTextField(SwingConstants.CENTER);
        textField1.setBounds(110, 450, 240, 30);
        frame.add(textField1);
        textField1.setVisible(false);

        JButton novaPlaylistButton = new JButton();
        novaPlaylistButton.setText("Nova Playlist");
        novaPlaylistButton.setBounds(60, 450, 150, 30);
        frame.add(novaPlaylistButton);

        JButton save = new JButton();
        save.setText("save");
        save.setVisible(false);
        save.setBounds(380, 450, 100, 30);
        frame.add(save);

        JButton carregarPlaylistButton = new JButton("Carregar Playlist");
        carregarPlaylistButton.setBounds(165, 500, 150, 30);
        frame.add(carregarPlaylistButton);

        JButton reproduzirPlaylistButton = new JButton("Adicionar música");
        reproduzirPlaylistButton.setBounds(270, 450, 150, 30);
        frame.add(reproduzirPlaylistButton);

        JButton excluirButton = new JButton("Excluir da playlist");
        excluirButton.setBounds(270, 500, 150, 30);
        excluirButton.setVisible(false);
        frame.add(excluirButton);

        frame.setVisible(true);
        addMusic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mp3.parar();
                playButton.setVisible(true);
                mp3.escolherArquivo();
                try {
                    mp3.iniciaPrograma(progressBar1, JLabelBegin);
                    JLabelEnd.setText(mp3.duracao());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }


                String caminhoArquivo = mp3.getAudioSelecionado();
                if (caminhoArquivo != null) {
                    if (caminhoArquivo.equals("C:\\Users\\lucio\\Downloads\\av3-main\\av3-main\\src\\Sounds\\Legend.mp3")) {
                        songTitleLabel.setText("Legend");
                    } else {
                        songTitleLabel.setText(new File(caminhoArquivo).getName());
                    }
                }
            }
        });


        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playButton.setVisible(false);
                pauseButton.setVisible(true);
                mp3.toca();
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playButton.setVisible(true);
                pauseButton.setVisible(false);
                mp3.pausa();
            }
        });

        novaPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField1.setText(mp3.criaPlaylist());
                textField1.setVisible(true);
                novaPlaylistButton.setVisible(false);
                escolherMusica.setVisible(true);
                save.setVisible(true);
                carregarPlaylistButton.setVisible(false);
                reproduzirPlaylistButton.setVisible(false);
            }
        });

        escolherMusica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mp3.procuraPlaylist(area);
                area.setVisible(true);
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mp3.salvarPlaylist(textField1);
                novaPlaylistButton.setVisible(true);
                area.setVisible(false);
                carregarPlaylistButton.setVisible(true);
                reproduzirPlaylistButton.setVisible(true);
                textField1.setVisible(false);
                escolherMusica.setVisible(false);
                save.setVisible(false);
                carregarPlaylistButton.setVisible(true);
            }
        });

        carregarPlaylistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirButton.setVisible(true);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int escolha = fileChooser.showOpenDialog(null);
                if (escolha == JFileChooser.APPROVE_OPTION) {
                    caminhoArquivo = fileChooser.getSelectedFile().getPath();
                    playlistCarregada = mp3.carregaPlaylist(caminhoArquivo, list);
                    songTitleLabel.setText(new File(caminhoArquivo).getName());
                    mp3.reproduzirPlaylist(playlistCarregada, progressBar1, JLabelBegin, JLabelEnd, songTitleLabel);
                    pauseButton.setVisible(true);
                    playButton.setVisible(false);
                }
            }
        });

        reproduzirPlaylistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mp3.adicionaNaPlaylist();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluiClicado = true;
                excluirButton.setVisible(false);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mp3.avancarSegundos();
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mp3.voltarSegundos();
            }
        });

        sliderVolume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                mp3.volume(sliderVolume);
            }
        });

        paraMusica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mp3.paraReproducao();
                progressBar1.setValue(0);
                pauseButton.setVisible(false);
                playButton.setVisible(true);
            }
        });
        antesMusic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {mp3.musicaAnterior(songTitleLabel, progressBar1, JLabelBegin, JLabelEnd);}
        });

        proximaMusic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {mp3.proximaMusica(songTitleLabel, progressBar1, JLabelBegin, JLabelEnd);}
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getButton());
                if(e.getButton() == MouseEvent.BUTTON3) {
                    mp3.reproduzEscolha(list, progressBar1, JLabelBegin, JLabelEnd, songTitleLabel);
                    playButton.setVisible(false);
                    pauseButton.setVisible(true);
                    return;
                }

                if(excluiClicado == true && e.getButton() == MouseEvent.BUTTON1){
                    mp3.removeMusica(list);
                    excluiClicado = false;
                    excluirButton.setVisible(true);
                    return;
                }
            }
        });

    }
}
