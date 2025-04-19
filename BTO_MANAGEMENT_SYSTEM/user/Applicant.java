// ===========================
// File: user/Applicant.java
// ===========================
package user;

import application.BTOApplication;
import enquiry.Enquiry;
import project.BTOProject;
import service.ApplicantEnquiryHandler;

import java.util.*;
import util.FilterOptions;

public class Applicant implements User, ApplicantActions {
    private String name;
    private String nric;
    private String password;
    private int age;
    private String maritalStatus; 
    private BTOApplication application;
    private List<Enquiry> enquiries = new ArrayList<>();
    private FilterOptions filters = new FilterOptions();


    public Applicant(String name, String nric, String password, int age, String maritalStatus) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    public int getAge() { return age; }
    public String getName() { return name; }
    public String getNric() { return nric; }
    public String getPassword() { return password; }
    public BTOApplication getApplication() { return application; }
    public List<Enquiry> getEnquiries() { return enquiries; }
    public String getMaritalStatus() { return maritalStatus; }

    public void setPassword(String pw) { password = pw; }
    public void setFlatType(String flatType) {
        if (application != null) {
            application.setFlatType(flatType);
        }
    }

    public FilterOptions getFilters() {
        return filters;
    }

    public void setFilters(FilterOptions filters) {
        this.filters = filters;
    }

    public void clearApplication() { application = null; }
    
    public void viewEligibleProjects(List<BTOProject> allProjects) {
        Scanner sc = new Scanner(System.in);
        filters.promptUpdate(sc);

        System.out.println("\n--- Eligible BTO Projects ---");
        for (BTOProject p : allProjects) {
            if (p.isVisible() && filters.matches(p)) {
                if (maritalStatus.equals("Single") && age >= 35) {
                    // Singles >= 35 can only apply for 2-Room
                    if (p.getType1().equals("2-Room") || p.getType2().equals("2-Room")) {
                        System.out.println("Project: " + p.getProjectName() + ", " + p.getNeighborhood());
                        if (p.getType1().equals("2-Room"))
                            System.out.println("  2-Room - $" + p.getPriceType1() + " (" + p.getUnitsType1() + " units)");
                        if (p.getType2().equals("2-Room"))
                            System.out.println("  2-Room - $" + p.getPriceType2() + " (" + p.getUnitsType2() + " units)");
                        System.out.println("  Application Period: " + p.getOpenDate() + " to " + p.getCloseDate());
                        System.out.println("----------------------------------------");
                    }
                } else if (maritalStatus.equals("Married") && age >= 21) {
                    // Married >= 21 can view all flat types
                    p.displaySummary();
                }
            }
        }
    }

    public void applyForProject(BTOProject project, String flatType, List<HDBManager> managers, List<HDBOfficer> officers, List<BTOProject> projects) {
        if (application != null) {
            System.out.println("You already have an ongoing application.");
            return;
        }
        for (HDBManager manager : managers) {
            if (manager.getName().equals(this.getName())) {
                System.out.println("As a BTO Manager, you cannot apply for this project.");
                return;
            }
        }

        for (HDBOfficer officer : officers) {
            if (officer.getName().equals(this.getName()) && officer.getAssignedProjects(projects).contains(project)) {
                System.out.println("You are already an officer for this project and cannot apply for it.");
                return;
            }
        }

        if (maritalStatus.equals("Single") && age < 35) {
            System.out.println("Singles must be at least 35 years old to apply.");
            return;
        }
        if (maritalStatus.equals("Single") && !flatType.equals("2-Room")) {
            System.out.println("Singles can only apply for 2-Room flats.");
            return;
        }
        if (!flatType.equals(project.getType1()) && !flatType.equals(project.getType2())) {
            System.out.println("Invalid flat type for this project.");
            return;
        }
        application = new BTOApplication("Pending", project, flatType);
        System.out.println("Application submitted successfully! Status: Pending");
    }

    public void viewApplication() {
        if (application == null) {
            System.out.println("No application found.");
            return;
        }
        System.out.println("\n--- Your Application ---");
        System.out.println("Status: " + application.getStatus());
        BTOProject p = application.getProject();
        System.out.println("Project: " + p.getProjectName() + " (" + p.getNeighborhood() + ")");
        System.out.println("Flat Type: " + application.getFlatType());
    }

    public void requestWithdrawal() {
        try {
            if (application == null) {
                System.out.println("No application to withdraw.");
                return;
            }
            if (application.isWithdrawalRequested()) {
                System.out.println("You have already requested to withdraw this application.");
                return;
            }
            application.setWithdrawalRequested(true);
            System.out.println("Withdrawal request submitted successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred while processing your withdrawal request: " + e.getMessage());
        }
    }

    public void manageEnquiries(List<BTOProject> projects, Scanner sc) {
        new ApplicantEnquiryHandler().manageEnquiries(this, projects, sc);
    }
}
