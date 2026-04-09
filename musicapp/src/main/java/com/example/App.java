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
            System.out.println("[Q]uit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {

                case "H":
                    try {
                        File file = new File("musicapp/songs/F J Blues - Unknown Man.wav");
                        javax.sound.sampled.AudioInputStream audio =
                                javax.sound.sampled.AudioSystem.getAudioInputStream(file);

                        if (currentClip != null && currentClip.isRunning()) {
                            currentClip.stop();
                            currentClip.close();
                        }

                        currentClip = javax.sound.sampled.AudioSystem.getClip();
                        currentClip.open(audio);
                        currentClip.start();

                        System.out.println("Playing song...");
                    } catch (Exception e) {
                        System.out.println("Error playing file");
                        e.printStackTrace();
                    }
                    break;

                case "S":
                    System.out.println("You selected Search by title.");
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
                            try {
                                File file = listOfFiles[songChoice - 1];
                                javax.sound.sampled.AudioInputStream audio =
                                        javax.sound.sampled.AudioSystem.getAudioInputStream(file);

                                if (currentClip != null && currentClip.isRunning()) {
                                    currentClip.stop();
                                    currentClip.close();
                                }

                                currentClip = javax.sound.sampled.AudioSystem.getClip();
                                currentClip.open(audio);
                                currentClip.start();

                                System.out.println("Playing: " + file.getName());
                            } catch (Exception e) {
                                System.out.println("Error playing selected file");
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Invalid number.");
                        }
                    }

                    break;

                case "Q":
                    running = false;

                    if (currentClip != null && currentClip.isRunning()) {
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
}