// ===========================
// File: user/HDBOfficer.java
// ===========================
package user;

import project.BTOProject;
import service.EnquiryResponseHandler;
import application.BTOApplication;
import enquiry.Enquiry;
import java.util.*;

public class HDBOfficer extends Applicant implements OfficerActions {

    private util.FilterOptions filters = new util.FilterOptions();

    private Map<String, String> registrationStatus = new HashMap<>();

    public HDBOfficer(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
    }

    public void registerForProject(List<BTOProject> projects, Scanner sc) {
        System.out.println("\n--- Register to Join a Project ---");
        for (int i = 0; i < projects.size(); i++) {
            BTOProject p = projects.get(i);
            System.out.println((i + 1) + ". " + p.getProjectName() + " (" + p.getNeighborhood() + ")");
        }

        System.out.print("Select a project to register for: ");
        try {
            int index = Integer.parseInt(sc.nextLine()) - 1;
            if (index < 0 || index >= projects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            BTOProject selected = projects.get(index);

            // Prevent duplicate registration
            if (registrationStatus.containsKey(selected.getProjectName())) {
                System.out.println("You have already registered for this project.");
                return;
            }

            // Prevent registration if already handling another project in same application period
            for (String proj : registrationStatus.keySet()) {
                for (BTOProject p : projects) {
                    if (p.getProjectName().equals(proj)) {
                        if (dateOverlaps(p.getOpenDate(), p.getCloseDate(), selected.getOpenDate(), selected.getCloseDate())) {
                            System.out.println("You are already registered for a project in the same application period.");
                            return;
                        }
                    }
                }
            }

            // Prevent registration if already applied for the same project
            if (getApplication() != null && getApplication().getProject().equals(selected)) {
                System.out.println("You have already applied for this project and cannot register as an officer.");
                return;
            }

            registrationStatus.put(selected.getProjectName(), "Pending");
            System.out.println("Registration request submitted. Pending manager approval.");
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    public void viewOfficerRegistrationStatus(List<BTOProject> projects) {
        System.out.println("\n--- My Officer Registration Status ---");
        if (registrationStatus.isEmpty()) {
            System.out.println("No registration records found.");
            return;
        }
        for (String projectName : registrationStatus.keySet()) {
            System.out.println("Project: " + projectName + " | Status: " + registrationStatus.get(projectName));
        }
    }

    public Map<String, String> getRegistrationStatusMap() {
        return registrationStatus;
    }

    public void updateRegistrationStatus(String projectName, String status) {
        registrationStatus.put(projectName, status);
    }

    public boolean isApprovedForProject(String projectName) {
        return registrationStatus.containsKey(projectName) && registrationStatus.get(projectName).equals("Approved");
    }

    private boolean dateOverlaps(String start1, String end1, String start2, String end2) {
        return !(end1.compareTo(start2) < 0 || end2.compareTo(start1) < 0);
    }


    public List<BTOProject> getAssignedProjects(List<BTOProject> projects) {
        List<BTOProject> assignedProjects = new ArrayList<>();
        
        for (BTOProject p : projects) {
            if (p.getOfficerNames().contains(this.getName())) {
                assignedProjects.add(p);
            }
        }
        return assignedProjects;
    }    

    public void viewAssignedProjects(List<BTOProject> projects) {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- View Assigned Projects ---");
        filters.promptUpdate(sc);

        System.out.println("\n--- Projects Assigned to Officer " + getName() + " ---");
        for (BTOProject p : projects) {
            if (!filters.matches(p)) continue;
            for (String officer : p.getOfficerNames()) {
                if (officer.trim().equalsIgnoreCase(getName().trim())) {
                    p.displaySummary();
                    break; // break inner loop once match found
                }
            }
        }
    }
    

    public void viewApplicantsForAssignedProjects(List<BTOProject> projects, List<Applicant> applicants) {
        System.out.println("\n--- Applicants for Officer " + getName() + " ---");
        for (BTOProject p : projects) {
            if (p.getOfficerNames().contains(getName())) {
                System.out.println("\nProject: " + p.getProjectName());
                for (Applicant a : applicants) {
                    BTOApplication app = a.getApplication();
                    if (app != null && app.getProject().equals(p)) {
                        System.out.println("Applicant: " + a.getName() + " | Flat Type: " + app.getFlatType() + " | Status: " + app.getStatus());
                    }
                }
            }
        }
    }

    public void finalizeBooking(List<Applicant> applicants, List<BTOProject> projects, Scanner sc) {
        System.out.println("\n--- Finalize Flat Booking ---");

        List<BTOProject> myProjects = new ArrayList<>();
        for (BTOProject p : projects) {
            if (p.getOfficerNames().contains(this.getName())) {
                myProjects.add(p);
            }
        }

        if (myProjects.isEmpty()) {
            System.out.println("You are not assigned to any project.");
            return;
        }

        for (int i = 0; i < myProjects.size(); i++) {
            System.out.println((i + 1) + ". " + myProjects.get(i).getProjectName());
        }
        System.out.print("Select a project: ");
        int pIdx = Integer.parseInt(sc.nextLine()) - 1;
        if (pIdx < 0 || pIdx >= myProjects.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        BTOProject selected = myProjects.get(pIdx);

        List<Applicant> approved = new ArrayList<>();
        for (Applicant a : applicants) {
            if (a.getApplication() != null &&
                a.getApplication().getProject().equals(selected) &&
                a.getApplication().getStatus().equals("Approved")) {
                approved.add(a);
            }
        }

        if (approved.isEmpty()) {
            System.out.println("No approved applications for this project.");
            return;
        }

        for (int i = 0; i < approved.size(); i++) {
            System.out.println((i + 1) + ". " + approved.get(i).getName());
        }

        System.out.print("Select an applicant to finalize booking or 0 to cancel: ");
        int sel = Integer.parseInt(sc.nextLine()) - 1;
        if (sel == -1) return;

        Applicant chosen = approved.get(sel);
        String flatType = chosen.getApplication().getFlatType();

        if (flatType.equals(selected.getType1()) && selected.getUnitsType1() > 0) {
            selected.setUnitsType1(selected.getUnitsType1() - 1);
        } else if (flatType.equals(selected.getType2()) && selected.getUnitsType2() > 0) {
            selected.setUnitsType2(selected.getUnitsType2() - 1);
        } else {
            System.out.println("No more units available for this flat type.");
            return;
        }

        chosen.getApplication().setStatus("Booked");
        chosen.setFlatType(flatType);
        System.out.println("Flat booking finalized for: " + chosen.getName());
    }

    public void generateReceipt(List<Applicant> applicants, Scanner sc) {
        System.out.println("--- Generate Booking Receipt ---");
        List<Applicant> booked = new ArrayList<>();
        for (Applicant a : applicants) {
            if (a.getApplication() != null && a.getApplication().getStatus().equals("Booked")) {
                booked.add(a);
            }
        }

        if (booked.isEmpty()) {
            System.out.println("No booked applications found.");
            return;
        }

        for (int i = 0; i < booked.size(); i++) {
            System.out.println((i + 1) + ". " + booked.get(i).getName());
        }
        System.out.print("Select an applicant to generate receipt: ");
        int choice = Integer.parseInt(sc.nextLine()) - 1;
        if (choice < 0 || choice >= booked.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Applicant a = booked.get(choice);
        BTOApplication app = a.getApplication();
        BTOProject proj = app.getProject();
        System.out.println("=== Booking Receipt ===");
        System.out.println("Name: " + a.getName());
        System.out.println("NRIC: " + a.getNric());
        System.out.println("Age: " + a.getAge());
        System.out.println("Flat Type: " + app.getFlatType());
        System.out.println("Project: " + proj.getProjectName());
        System.out.println("Neighborhood: " + proj.getNeighborhood());
        System.out.println("Application Status: " + app.getStatus());
        System.out.println("=======================");
    }

    // public void respondToEnquiries(List<Applicant> applicants, List<BTOProject> projects) {
    //     class EnquiryWrapper {
    //         Applicant applicant;
    //         Enquiry enquiry;
    //         String projectName;
    //         EnquiryWrapper(Applicant a, Enquiry e, String pName) {
    //             applicant = a;
    //             enquiry = e;
    //             projectName = pName;
    //         }
    //     }
    //
    //     try {
    //         Scanner sc = new Scanner(System.in);
    //         System.out.println("--- Enquiries from Applicants (Your Projects Only) ---");
    //         List<EnquiryWrapper> validEnquiries = new ArrayList<>();
    //
    //         for (Applicant a : applicants) {
    //             for (Enquiry e : a.getEnquiries()) {
    //                 String projectName = e.getProjectName();
    //                 BTOProject related = null;
    //                 for (BTOProject p : projects) {
    //                     if (p.getProjectName().equals(projectName)) {
    //                         related = p;
    //                         break;
    //                     }
    //                 }
    //                 if (related != null && related.getOfficerNames().contains(this.getName())) {
    //                     validEnquiries.add(new EnquiryWrapper(a, e, projectName));
    //                 }
    //             }
    //         }
    //
    //         if (validEnquiries.isEmpty()) {
    //             System.out.println("No enquiries to respond to.");
    //             return;
    //         }
    //
    //         for (int i = 0; i < validEnquiries.size(); i++) {
    //             EnquiryWrapper ew = validEnquiries.get(i);
    //             System.out.println((i + 1) + ". Project: " + ew.projectName);
    //             System.out.println("   From: " + ew.applicant.getName());
    //             System.out.println("   Enquiry: " + ew.enquiry.getContent());
    //             String response = ew.enquiry.getResponse();
    //             if (response != null && !response.isEmpty()) {
    //                 System.out.println("   ↳ Response by: [" + ew.enquiry.getResponder() + "] " + response);
    //             }
    //         }
    //
    //         System.out.print("Enter the number of the enquiry to respond to (0 to cancel): ");
    //         int sel = Integer.parseInt(sc.nextLine()) - 1;
    //         if (sel == -1) return;
    //         if (sel >= 0 && sel < validEnquiries.size()) {
    //             EnquiryWrapper selected = validEnquiries.get(sel);
    //             System.out.print("Enter response: ");
    //             String reply = sc.nextLine();
    //             selected.enquiry.setResponse(reply);
    //             selected.enquiry.setResponder("Officer " + this.getName());
    //             System.out.println("Response recorded.");
    //         } else {
    //             System.out.println("Invalid selection.");
    //         }
    //
    //     } catch (Exception e) {
    //         System.out.println("Error responding to enquiries: " + e.getMessage());
    //     }
    // }
                        
    public void respondToEnquiries(List<Applicant> applicants, List<BTOProject> projects) {
        EnquiryResponseHandler.respondToEnquiries(applicants, projects, this.getName(), false);
    }
}
