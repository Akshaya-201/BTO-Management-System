// ===========================
// File: user/OfficerActions.java
// ===========================
package user;

import java.util.*;
import project.BTOProject;

public interface OfficerActions {
    void registerForProject(List<BTOProject> projects, Scanner sc);
    void viewOfficerRegistrationStatus(List<BTOProject> projects);
    Map<String, String> getRegistrationStatusMap();
    void updateRegistrationStatus(String projectName, String status);
    boolean isApprovedForProject(String projectName);
    List<BTOProject> getAssignedProjects(List<BTOProject> projects);
    void viewAssignedProjects(List<BTOProject> projects);
    void viewApplicantsForAssignedProjects(List<BTOProject> projects, List<Applicant> applicants);
    void finalizeBooking(List<Applicant> applicants, List<BTOProject> projects, Scanner sc);
    void generateReceipt(List<Applicant> applicants, Scanner sc);
    void respondToEnquiries(List<Applicant> applicants, List<BTOProject> projects);
}
