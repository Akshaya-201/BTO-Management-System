// ===========================
// File: user/ManagerActions.java
// ===========================
package user;

import java.util.*;
import project.BTOProject;

public interface ManagerActions {
    void assignOfficersToProject(List<BTOProject> projects, List<HDBOfficer> officers, Scanner sc);
    void createProject(List<BTOProject> projects, List<HDBOfficer> officers, Scanner sc);
    void editOrDeleteProject(List<BTOProject> projects, List<HDBOfficer> officers, Scanner sc);
    void toggleProjectVisibility(List<BTOProject> projects, Scanner sc);
    void processWithdrawalRequests(List<Applicant> applicants, Scanner sc);
    void processOfficerRegistrations(List<HDBOfficer> officers, List<BTOProject> projects, Scanner sc);
    void processApplications(List<Applicant> applicants, List<BTOProject> projects, Scanner sc);
    void generateReport(List<Applicant> applicants, List<BTOProject> projects, Scanner sc);
    void viewAllProjects(List<BTOProject> projects, Scanner sc);
    void viewMyProjects(List<BTOProject> projects, Scanner sc);
    void respondToEnquiries(List<Applicant> applicants, List<BTOProject> projects);
}