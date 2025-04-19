// ===========================
// File: ui/ApplicantUIHandler.java
// ===========================
package UI;

import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import project.BTOProject;

import java.util.*;

public class ApplicantUIHandler {
    
    public static void launchMenu(Applicant applicant, List<BTOProject> projects, List<HDBManager> managers, List<HDBOfficer> officers, Scanner sc) {
        while (true) {
            System.out.println("\n--- Applicant Menu ---");
            System.out.println("1. View Eligible Projects");
            System.out.println("2. Apply for Project");
            System.out.println("3. View Application Status");
            System.out.println("4. Request to Withdraw Application");
            System.out.println("5. Manage Enquiries");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");

            System.out.print("Enter choice: ");
            String input = sc.nextLine();
            if (input.isEmpty()) continue;
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> applicant.viewEligibleProjects(projects);
                case 2 -> {
                    System.out.println("Available Projects:");
                    for (int i = 0; i < projects.size(); i++) {
                        System.out.println((i + 1) + ". " + projects.get(i).getProjectName());
                    }
                    System.out.print("Select project number to apply: ");
                    int pIdx = Integer.parseInt(sc.nextLine()) - 1;
                    if (pIdx < 0 || pIdx >= projects.size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    BTOProject selected = projects.get(pIdx);
                    System.out.print("Enter flat type to apply for (2-Room/3-Room): ");
                    String type = sc.nextLine();
                    applicant.applyForProject(selected, type, managers, officers, projects);
                }
                case 3 -> applicant.viewApplication();
                case 4 -> applicant.requestWithdrawal();
                case 5 -> applicant.manageEnquiries(projects, sc);
                case 6 -> {
                    System.out.print("Enter new password: ");
                    applicant.setPassword(sc.nextLine());
                    System.out.println("Password updated.");
                }
                case 7 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
