package com.example;

import java.util.Scanner;

public class App {
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
                    System.out.println("You selected Home.");
                    break;

                case "S":
                    System.out.println("You selected Search by title.");
                    break;

                case "L":
                    System.out.println("You selected Library.");
                    break;

                case "Q":
                    running = false;
                    System.out.println("Quitting the application. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }

            System.out.println(); // adds space between loops
        }

        scanner.close();
    }
}