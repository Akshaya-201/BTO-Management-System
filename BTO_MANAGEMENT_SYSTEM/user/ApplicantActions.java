// ===========================
// File: user/ApplicantActions.java
// ===========================
package user;

import project.BTOProject;

import java.util.List;
import java.util.Scanner;

public interface ApplicantActions {
    void applyForProject(BTOProject project, String flatType, List<HDBManager> managers, List<HDBOfficer> officers, List<BTOProject> projects);
    void viewApplication();
    void requestWithdrawal();
    void manageEnquiries(List<BTOProject> projects, Scanner sc);
    void viewEligibleProjects(List<BTOProject> allProjects);
}
