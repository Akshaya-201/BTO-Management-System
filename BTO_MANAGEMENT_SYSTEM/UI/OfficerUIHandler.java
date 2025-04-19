// ===========================
// File: ui/OfficerUIHandler.java
// ===========================
package UI;

import user.*;
import project.BTOProject;
import java.util.*;

public class OfficerUIHandler {

    public static void launchMenu(HDBOfficer officer, List<BTOProject> projects, List<Applicant> applicants, List<HDBManager> managers, List<HDBOfficer> officers, Scanner sc) {
        while (true) {
            System.out.println("\n--- HDB Officer Menu ---");
            System.out.println("1. View Assigned Projects");
            System.out.println("2. View Applicants for My Projects");
            System.out.println("3. Finalize Flat Booking");
            System.out.println("4. Respond to Enquiries");
            System.out.println("5. Generate Booking Receipt");
            System.out.println("6. Change Password");
            System.out.println("7. View Projects");
            System.out.println("8. Apply for Project");
            System.out.println("9. View My Application");
            System.out.println("10. Manage My Enquiries");
            System.out.println("11. Request Withdrawal");
            System.out.println("12. Register to Join a Project");
            System.out.println("13. View Officer Registration Status");
            System.out.println("14. Logout");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> officer.viewAssignedProjects(projects);
                case 2 -> officer.viewApplicantsForAssignedProjects(projects, applicants);
                case 3 -> officer.finalizeBooking(applicants, projects, sc);
                case 4 -> officer.respondToEnquiries(applicants, projects);
                case 5 -> officer.generateReceipt(applicants, sc);
                case 6 -> {
                    System.out.print("New Password: ");
                    officer.setPassword(sc.nextLine());
                }
                case 7 -> officer.viewEligibleProjects(projects);
                case 8 -> {
                    System.out.println("Enter the name of the project to apply for:");
                    String pname = sc.nextLine();
                    BTOProject selected = null;
                    for (BTOProject p : projects) {
                        if (p.getProjectName().equalsIgnoreCase(pname)) {
                            selected = p;
                            break;
                        }
                    }
                    if (selected == null) {
                        System.out.println("Project not found.");
                    } else {
                        System.out.println("Choose flat type to apply for: " + selected.getType1() + " or " + selected.getType2());
                        String flatType = sc.nextLine();
                        officer.applyForProject(selected, flatType, managers, officers, projects);
                    }
                }
                case 9 -> officer.viewApplication();
                case 10 -> officer.manageEnquiries(projects, sc);
                case 11 -> officer.requestWithdrawal();
                case 12 -> officer.registerForProject(projects, sc);
                case 13 -> officer.viewOfficerRegistrationStatus(projects);
                case 14 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
