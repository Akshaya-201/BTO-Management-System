// ===============================
// File: ui/ManagerUIHandler.java
// ===============================
package UI;

import user.*;
import project.BTOProject;
import java.util.*;

public class ManagerUIHandler {
    public static void launchMenu(HDBManager m, List<BTOProject> projects, List<HDBOfficer> officers, List<Applicant> applicants, Scanner sc) {
        while (true) {
            System.out.println("\n--- HDB Manager Menu ---");
            System.out.println("1. View All Projects");
            System.out.println("2. View My Projects");
            System.out.println("3. Create New Project");
            System.out.println("4. Toggle Project Visibility");
            System.out.println("5. Process Withdrawal Requests");
            System.out.println("6. Edit/Delete Projects");
            System.out.println("7. Approve Officer Registrations");
            System.out.println("8. Process Applicant Applications");
            System.out.println("9. Respond to Enquiries");
            System.out.println("10. Generate Report");
            System.out.println("11. Change Password");
            System.out.println("12. Logout");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> m.viewAllProjects(projects, sc);
                case 2 -> m.viewMyProjects(projects, sc);
                case 3 -> m.createProject(projects, officers, sc);
                case 4 -> m.toggleProjectVisibility(projects, sc);
                case 5 -> m.processWithdrawalRequests(applicants, sc);
                case 6 -> m.editOrDeleteProject(projects, officers, sc);
                case 7 -> m.processOfficerRegistrations(officers, projects, sc);
                case 8 -> m.processApplications(applicants, projects, sc);
                case 9 -> m.respondToEnquiries(applicants, projects);
                case 10 -> m.generateReport(applicants, projects, sc);
                case 11 -> {
                    System.out.print("New Password: ");
                    m.setPassword(sc.nextLine());
                }
                case 12 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
