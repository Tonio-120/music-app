package com.example;

import java.io.File;
import java.util.Scanner;

public class App {

    static javax.sound.sampled.Clip currentClip;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

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
                    playSong("musicapp/songs/F J Blues - Unknown Man.wav");
                    break;

                case "S":
                    File searchFolder = new File("musicapp/songs");
                    File[] searchFiles = searchFolder.listFiles();

                    System.out.print("Enter song name to search: ");
                    String search = scanner.nextLine().toLowerCase();

                    System.out.println("=== Results ===");

                    if (searchFiles != null) {
                        int count = 1;

                        for (int i = 0; i < searchFiles.length; i++) {
                            if (searchFiles[i].getName().toLowerCase().contains(search)) {
                                System.out.println(count + ". " + searchFiles[i].getName());
                                count++;
                            }
                        }

                        if (count == 1) {
                            System.out.println("No songs found.");
                            break;
                        }

                        System.out.print("Enter number to play: ");
                        int choiceNum = Integer.parseInt(scanner.nextLine());

                        int currentIndex = 1;

                        for (int i = 0; i < searchFiles.length; i++) {
                            if (searchFiles[i].getName().toLowerCase().contains(search)) {
                                if (currentIndex == choiceNum) {
                                    playSong(searchFiles[i].getPath());
                                    break;
                                }
                                currentIndex++;
                            }
                        }
                    }
                    break;

                case "L":
                    File folder = new File("musicapp/songs");
                    File[] listOfFiles = folder.listFiles();

                    System.out.println("=== Your Songs ===");

                    if (listOfFiles != null) {
                        for (int i = 0; i < listOfFiles.length; i++) {
                            if (listOfFiles[i].isFile()) {
                                System.out.println((i + 1) + ". " + listOfFiles[i].getName());
                            }
                        }

                        System.out.print("Enter song number to play: ");
                        int songChoice = Integer.parseInt(scanner.nextLine());

                        if (songChoice >= 1 && songChoice <= listOfFiles.length) {
                            playSong(listOfFiles[songChoice - 1].getPath());
                        } else {
                            System.out.println("Invalid number.");
                        }
                    }
                    break;

                case "P":
                    if (currentClip != null && currentClip.isRunning()) {
                        currentClip.stop();
                        System.out.println("Song paused.");
                    } else {
                        System.out.println("No song is currently playing.");
                    }
                    break;

                case "R":
                    if (currentClip != null) {
                        currentClip.start();
                        System.out.println("Song resumed.");
                    } else {
                        System.out.println("No song loaded.");
                    }
                    break;

                case "X":
                    if (currentClip != null) {
                        currentClip.stop();
                        currentClip.setFramePosition(0);
                        System.out.println("Song stopped.");
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

                    System.out.println("Quitting the application. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }

            System.out.println();
        }

        scanner.close();
    }

    public static void playSong(String path) {
        try {
            if (currentClip != null) {
                currentClip.stop();
                currentClip.close();
            }

            File file = new File(path);
            javax.sound.sampled.AudioInputStream audio =
                    javax.sound.sampled.AudioSystem.getAudioInputStream(file);

            currentClip = javax.sound.sampled.AudioSystem.getClip();
            currentClip.open(audio);
            currentClip.start();

            System.out.println("Playing: " + file.getName());
        } catch (Exception e) {
            System.out.println("Error playing file");
            e.printStackTrace();
        }
    }
}