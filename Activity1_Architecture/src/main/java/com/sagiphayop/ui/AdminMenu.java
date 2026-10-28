package com.sagiphayop.ui;

import com.sagiphayop.dao.UserDao;
import com.sagiphayop.model.User;

import java.util.Scanner;

/** Admin menu placeholder; admin features are added in later deliverables. */
public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;
    private final User currentAdmin;

    public AdminMenu(UserDao userDao, Scanner scanner, User currentAdmin) {
        this.userDao = userDao;
        this.scanner = scanner;
        this.currentAdmin = currentAdmin;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== ADMIN MENU (" + currentAdmin.username() + ") ===");
            System.out.println("(Admin features coming soon)");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            if (scanner.nextLine().trim().equals("0")) {
                System.out.println("Logged out.");
                return;
            }
            System.out.println("Invalid choice.");
        }
    }
}
