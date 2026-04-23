package com.example;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

public class AppGUI {

    static ArrayList<Song> userSongs = new ArrayList<>();
    static Clip currentClip;
    static Song currentSong;

    public static void main(String[] args) {

        loadUserSettings();

        JFrame frame = new JFrame("Spotify App");
        frame.setSize(550, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton homeButton = new JButton("Home");
        homeButton.setBounds(50, 30, 120, 30);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(200, 30, 120, 30);

        JButton libraryButton = new JButton("Library");
        libraryButton.setBounds(350, 30, 120, 30);

        JButton playButton = new JButton("Play");
        playButton.setBounds(50, 100, 120, 30);

        JButton pauseButton = new JButton("Pause");
        pauseButton.setBounds(200, 100, 120, 30);

        JButton resumeButton = new JButton("Resume");
        resumeButton.setBounds(350, 100, 120, 30);

        JButton stopButton = new JButton("Stop");
        stopButton.setBounds(120, 170, 120, 30);

        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(280, 170, 120, 30);

        JButton commentButton = new JButton("Add Comment");
        commentButton.setBounds(120, 230, 120, 30);

        JButton favoriteButton = new JButton("Toggle Favorite");
        favoriteButton.setBounds(280, 230, 150, 30);

        frame.add(homeButton);
        frame.add(searchButton);
        frame.add(libraryButton);
        frame.add(playButton);
        frame.add(pauseButton);
        frame.add(resumeButton);
        frame.add(stopButton);
        frame.add(quitButton);
        frame.add(commentButton);
        frame.add(favoriteButton);

        homeButton.addActionListener(e -> showHome(frame));
        searchButton.addActionListener(e -> searchSongs(frame));
        libraryButton.addActionListener(e -> showLibrary(frame));
        playButton.addActionListener(e -> playFirstSong(frame));
        pauseButton.addActionListener(e -> pauseSong(frame));
        resumeButton.addActionListener(e -> resumeSong(frame));
        stopButton.addActionListener(e -> stopSong(frame));
        quitButton.addActionListener(e -> quitProgram());
        commentButton.addActionListener(e -> addComment(frame));
        favoriteButton.addActionListener(e -> toggleFavorite(frame));

        frame.setVisible(true);
    }

    public static void showHome(JFrame frame) {
        JOptionPane.showMessageDialog(
            frame,
            "Welcome to your Spotify-like GUI app!\nUse the buttons to browse and control music."
        );
    }

    public static void searchSongs(JFrame frame) {
        if (userSongs.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No songs loaded.");
            return;
        }

        String search = JOptionPane.showInputDialog(frame, "Enter title search:");
        if (search == null || search.trim().isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (Song song : userSongs) {
            if (song.getTitle().toLowerCase().contains(search.toLowerCase())) {
                sb.append(song.getTitle())
                  .append(" - ")
                  .append(song.getArtist())
                  .append("\n");
            }
        }

        if (sb.length() == 0) {
            JOptionPane.showMessageDialog(frame, "No matches found.");
        } else {
            JOptionPane.showMessageDialog(frame, sb.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void showLibrary(JFrame frame) {
        if (userSongs.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No songs loaded.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (Song song : userSongs) {
            sb.append(song.getTitle())
              .append(" - ")
              .append(song.getArtist())
              .append("\nFavorite: ")
              .append(song.isFavorite())
              .append("\nComments: ")
              .append(song.getComments())
              .append("\n\n");
        }

        JOptionPane.showMessageDialog(frame, sb.toString(), "Library", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void playFirstSong(JFrame frame) {
        if (userSongs.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No songs loaded.");
            return;
        }

        Song song = userSongs.get(0);
        playSong(song, frame);
    }

    public static void pauseSong(JFrame frame) {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            JOptionPane.showMessageDialog(frame, "Paused.");
        } else {
            JOptionPane.showMessageDialog(frame, "No song is currently playing.");
        }
    }

    public static void resumeSong(JFrame frame) {
        if (currentClip != null) {
            currentClip.start();
            JOptionPane.showMessageDialog(frame, "Resumed.");
        } else {
            JOptionPane.showMessageDialog(frame, "No song loaded.");
        }
    }

    public static void stopSong(JFrame frame) {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.setMicrosecondPosition(0);
            JOptionPane.showMessageDialog(frame, "Stopped.");
        } else {
            JOptionPane.showMessageDialog(frame, "No song loaded.");
        }
    }

    public static void addComment(JFrame frame) {
        if (userSongs.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No songs loaded.");
            return;
        }

        Song song = userSongs.get(0);

        String newComment = JOptionPane.showInputDialog(
            frame,
            "Enter new comment for " + song.getTitle()
        );

        if (newComment != null) {
            song.setComments(newComment);

            JOptionPane.showMessageDialog(
                frame,
                "Updated comment:\n" + song.getComments()
            );
        }
    }

    public static void toggleFavorite(JFrame frame) {
        if (userSongs.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No songs loaded.");
            return;
        }

        Song song = userSongs.get(0);
        song.setFavorite(!song.isFavorite());

        JOptionPane.showMessageDialog(
            frame,
            song.getTitle() + " favorite status is now: " + song.isFavorite()
        );
    }

    public static void loadUserSettings() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("musicapp/user-settings.json");
            Song[] songsArray = gson.fromJson(reader, Song[].class);
            reader.close();

            userSongs = new ArrayList<>(Arrays.asList(songsArray));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading user-settings.json");
            e.printStackTrace();
        }
    }

    public static void playSong(Song song, JFrame frame) {
        try {
            if (currentClip != null) {
                currentClip.stop();
                currentClip.close();
            }

            File file = new File(song.getFilePath());
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            currentClip = AudioSystem.getClip();
            currentClip.open(audio);
            currentClip.start();

            currentSong = song;

            JOptionPane.showMessageDialog(
                frame,
                "Playing: " + song.getTitle()
                    + "\nArtist: " + song.getArtist()
                    + "\nFavorite: " + song.isFavorite()
                    + "\nComments: " + song.getComments()
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error playing file.");
            e.printStackTrace();
        }
    }

    public static void quitProgram() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
        }

        System.exit(0);
    }
}