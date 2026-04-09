package com.example;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.google.gson.Gson;

public class App {

    static Clip currentClip;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        ArrayList<Song> songs = loadSongs();

        while (running) {
            System.out.println("===== MENU =====");
            System.out.println("[H]ome");
            System.out.println("[S]earch by title");
            System.out.println("[L]ibrary");
            System.out.println("[P]ause");
            System.out.println("[R]esume");
            System.out.println("[X] Stop");
            System.out.println("[Q]uit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {

                case "H":
                    System.out.println("Home selected.");
                    break;

                case "S":
                    System.out.print("Enter search term: ");
                    String search = scanner.nextLine().toLowerCase();

                    ArrayList<Song> matches = new ArrayList<>();

                    System.out.println("=== Results ===");
                    for (Song song : songs) {
                        if (song.title.toLowerCase().contains(search)) {
                            matches.add(song);
                            System.out.println(matches.size() + ". " + song.title + " - " + song.artist);
                        }
                    }

                    if (matches.size() == 0) {
                        System.out.println("No matches found.");
                    } else {
                        System.out.print("Choose song number: ");
                        int pick = Integer.parseInt(scanner.nextLine());

                        if (pick >= 1 && pick <= matches.size()) {
                            playSong(matches.get(pick - 1));
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    }
                    break;

                case "L":
                    System.out.println("=== Your Songs ===");
                    for (int i = 0; i < songs.size(); i++) {
                        Song song = songs.get(i);
                        System.out.println((i + 1) + ". " + song.title + " - " + song.artist);
                    }

                    if (songs.size() > 0) {
                        System.out.print("Enter song number to play: ");
                        int songChoice = Integer.parseInt(scanner.nextLine());

                        if (songChoice >= 1 && songChoice <= songs.size()) {
                            playSong(songs.get(songChoice - 1));
                        } else {
                            System.out.println("Invalid song number.");
                        }
                    } else {
                        System.out.println("No songs loaded.");
                    }
                    break;

                case "P":
                    if (currentClip != null && currentClip.isRunning()) {
                        currentClip.stop();
                        System.out.println("Paused.");
                    } else {
                        System.out.println("No song is currently playing.");
                    }
                    break;

                case "R":
                    if (currentClip != null) {
                        currentClip.start();
                        System.out.println("Resumed.");
                    } else {
                        System.out.println("No song loaded.");
                    }
                    break;

                case "X":
                    if (currentClip != null) {
                        currentClip.stop();
                        currentClip.close();
                        System.out.println("Stopped.");
                    } else {
                        System.out.println("No song loaded.");
                    }
                    break;

                case "Q":
                    running = false;
                    if (currentClip != null) {
                        currentClip.stop();
                        currentClip.close();
                    }
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }

    public static ArrayList<Song> loadSongs() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("musicapp/audio-library.json");

            Song[] songsArray = gson.fromJson(reader, Song[].class);
            reader.close();

            return new ArrayList<>(Arrays.asList(songsArray));
        } catch (Exception e) {
            System.out.println("Error loading songs from JSON.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void playSong(Song song) {
        try {
            if (currentClip != null) {
                currentClip.stop();
                currentClip.close();
            }

            File file = new File(song.filePath);
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            currentClip = AudioSystem.getClip();

            currentClip.open(audio);
            currentClip.start();

            System.out.println("Playing:");
            System.out.println("Title: " + song.title);
            System.out.println("Artist: " + song.artist);
            System.out.println("Year: " + song.year);
            System.out.println("Genre: " + song.genre);
            System.out.println("File Path: " + song.filePath);

        } catch (Exception e) {
            System.out.println("Error playing file.");
            e.printStackTrace();
        }
    }
}