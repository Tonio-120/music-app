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
        ArrayList<Song> songs = loadSongs();
        boolean running = true;

        while (running) {

            displayMainMenu();

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {

                case "H":
                    showHome();
                    break;

                case "S":
                    searchSongs(songs, scanner);
                    break;

                case "A":
                    searchByArtist(songs, scanner);
                    break;

                case "G":
                    searchByGenre(songs, scanner);
                    break;

                case "L":
                    showLibrary(songs, scanner);
                    break;

                case "F":
                    showFavorites(songs, scanner);
                    break;

                case "Q":
                    running = false;
                    quitProgram();
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }

    public static void displayMainMenu() {

        System.out.println("\n===== MENU =====");
        System.out.println("[H] Home");
        System.out.println("[S] Search by title");
        System.out.println("[A] Search by artist");
        System.out.println("[G] Search by genre");
        System.out.println("[L] Library");
        System.out.println("[F] Favorites");
        System.out.println("[Q] Quit");
    }

    public static void showHome() {

        System.out.println("\n===== HOME =====");
        System.out.println("Welcome to your Spotify-like music app!");
        System.out.println("Use the menu to search songs, view your library,");
        System.out.println("play music, and manage favorites.");
    }

    public static ArrayList<Song> loadSongs() {

        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("musicapp/audio-library.json");
            Song[] songsArray = gson.fromJson(reader, Song[].class);
            reader.close();
            return new ArrayList<>(Arrays.asList(songsArray));

        } catch (Exception e) {
            System.out.println("Error loading songs.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void searchSongs(ArrayList<Song> songs, Scanner scanner) {

        System.out.print("Enter search term: ");
        String search = scanner.nextLine().toLowerCase();

        ArrayList<Song> matches = new ArrayList<>();

        for (Song song : songs) {
            if (song.getTitle().toLowerCase().contains(search)) {
                matches.add(song);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }

        System.out.println("\n=== Results ===");
        printSongList(matches);

        Song selectedSong = chooseSong(matches, scanner);

        if (selectedSong != null) {
            playSong(selectedSong);
            songControls(selectedSong, scanner);
        }
    }

    public static void searchByArtist(ArrayList<Song> songs, Scanner scanner) {

        System.out.print("Enter artist name: ");
        String search = scanner.nextLine().toLowerCase();

        ArrayList<Song> matches = new ArrayList<>();

        for (Song song : songs) {
            if (song.getArtist().toLowerCase().contains(search)) {
                matches.add(song);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No artist matches found.");
            return;
        }

        System.out.println("\n=== Artist Results ===");
        printSongList(matches);

        Song selectedSong = chooseSong(matches, scanner);

        if (selectedSong != null) {
            playSong(selectedSong);
            songControls(selectedSong, scanner);
        }
    }

    public static void searchByGenre(ArrayList<Song> songs, Scanner scanner) {

        System.out.print("Enter genre: ");
        String search = scanner.nextLine().toLowerCase();

        ArrayList<Song> matches = new ArrayList<>();

        for (Song song : songs) {
            if (song.getGenre().toLowerCase().contains(search)) {
                matches.add(song);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No genre matches found.");
            return;
        }

        System.out.println("\n=== Genre Results ===");
        printSongList(matches);

        Song selectedSong = chooseSong(matches, scanner);

        if (selectedSong != null) {
            playSong(selectedSong);
            songControls(selectedSong, scanner);
        }
    }

    public static void showLibrary(ArrayList<Song> songs, Scanner scanner) {

        if (songs.isEmpty()) {
            System.out.println("No songs loaded.");
            return;
        }

        System.out.println("\n=== Your Songs ===");
        printSongList(songs);

        Song selectedSong = chooseSong(songs, scanner);

        if (selectedSong != null) {
            playSong(selectedSong);
            songControls(selectedSong, scanner);
        }
    }

    public static void showFavorites(ArrayList<Song> songs, Scanner scanner) {

        ArrayList<Song> favorites = new ArrayList<>();

        for (Song song : songs) {
            if (song.isFavorite()) {
                favorites.add(song);
            }
        }

        if (favorites.isEmpty()) {
            System.out.println("No favorite songs.");
            return;
        }

        System.out.println("\n=== Favorite Songs ===");
        printSongList(favorites);

        Song selectedSong = chooseSong(favorites, scanner);

        if (selectedSong != null) {
            playSong(selectedSong);
            songControls(selectedSong, scanner);
        }
    }

    public static void printSongList(ArrayList<Song> songs) {

        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            String favoriteLabel = song.isFavorite() ? " [Favorite]" : "";
            System.out.println((i + 1) + ". " + song.getTitle() + " - " + song.getArtist() + favoriteLabel);
        }
    }

    public static Song chooseSong(ArrayList<Song> songs, Scanner scanner) {

        System.out.print("Choose song number (0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) {
                return null;
            }

            if (choice >= 1 && choice <= songs.size()) {
                return songs.get(choice - 1);
            }

            System.out.println("Invalid choice.");

        } catch (Exception e) {
            System.out.println("Please enter a valid number.");
        }

        return null;
    }

    public static void playSong(Song song) {

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

            printSongDetails(song);

        } catch (Exception e) {
            System.out.println("Error playing file.");
            e.printStackTrace();
        }
    }

    public static void printSongDetails(Song song) {

        System.out.println("\n=== NOW PLAYING ===");
        System.out.println("Title: " + song.getTitle());
        System.out.println("Artist: " + song.getArtist());
        System.out.println("Year: " + song.getYear());
        System.out.println("Genre: " + song.getGenre());
        System.out.println("Favorite: " + song.isFavorite());
        System.out.println("Comments: " + song.getComments());
        System.out.println("File Path: " + song.getFilePath());
    }

    public static void songControls(Song song, Scanner scanner) {

        boolean controlling = true;

        while (controlling) {

            System.out.println("\n=== SONG CONTROLS ===");
            System.out.println("[P] Pause");
            System.out.println("[R] Resume");
            System.out.println("[X] Stop");
            System.out.println("[W] Rewind 5 seconds");
            System.out.println("[D] Forward 5 seconds");
            System.out.println("[T] Toggle Favorite");
            System.out.println("[C] Add Comment");
            System.out.println("[V] View Comment");
            System.out.println("[B] Back");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {

                case "P":
                    pauseSong();
                    break;

                case "R":
                    resumeSong();
                    break;

                case "X":
                    stopSong();
                    break;

                case "W":
                    rewindSong();
                    break;

                case "D":
                    forwardSong();
                    break;

                case "T":
                    toggleFavorite(song);
                    break;

                case "C":
                    addComment(song, scanner);
                    break;

                case "V":
                    viewComment(song);
                    break;

                case "B":
                    controlling = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void pauseSong() {

        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            System.out.println("Paused.");
        } else {
            System.out.println("No song is currently playing.");
        }
    }

    public static void resumeSong() {

        if (currentClip != null) {
            currentClip.start();
            System.out.println("Resumed.");
        } else {
            System.out.println("No song loaded.");
        }
    }

    public static void stopSong() {

        if (currentClip != null) {
            currentClip.stop();
            currentClip.setMicrosecondPosition(0);
            System.out.println("Stopped.");
        } else {
            System.out.println("No song loaded.");
        }
    }

    public static void rewindSong() {

        if (currentClip != null) {
            long newPosition = currentClip.getMicrosecondPosition() - 5_000_000;

            if (newPosition < 0) {
                newPosition = 0;
            }

            currentClip.setMicrosecondPosition(newPosition);
            System.out.println("Rewinded 5 seconds.");
        } else {
            System.out.println("No song loaded.");
        }
    }

    public static void forwardSong() {

        if (currentClip != null) {
            long newPosition = currentClip.getMicrosecondPosition() + 5_000_000;
            long songLength = currentClip.getMicrosecondLength();

            if (newPosition > songLength) {
                newPosition = songLength;
            }

            currentClip.setMicrosecondPosition(newPosition);
            System.out.println("Forwarded 5 seconds.");
        } else {
            System.out.println("No song loaded.");
        }
    }

    public static void toggleFavorite(Song song) {

        song.setFavorite(!song.isFavorite());

        if (song.isFavorite()) {
            System.out.println(song.getTitle() + " added to favorites.");
        } else {
            System.out.println(song.getTitle() + " removed from favorites.");
        }

        System.out.println("Favorite Status: " + song.isFavorite());
    }

    public static void addComment(Song song, Scanner scanner) {

        System.out.print("Enter your comment: ");
        song.setComments(scanner.nextLine());

        System.out.println("Comment added.");
        System.out.println("Comment: " + song.getComments());
    }

    public static void viewComment(Song song) {

        System.out.println("\n=== SONG COMMENT ===");

        if (song.getComments() == null || song.getComments().isEmpty()) {
            System.out.println("No comments added.");
        } else {
            System.out.println(song.getComments());
        }
    }

    public static void quitProgram() {

        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
        }

        System.out.println("Goodbye!");
    }
}