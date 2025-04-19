// ===========================
// File: user/HDBManager.java
// ===========================

package user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import project.BTOProject;
import service.EnquiryResponseHandler;
import service.ReportGenerator;
import util.FilterOptions;

public class HDBManager implements User, ManagerActions{
    private String name;
    private String nric;
    private String password;
    private int age;
    private String maritalStatus;
    private FilterOptions filters = new FilterOptions();

    public HDBManager(String name, String nric, String password, int age, String maritalStatus) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
    }
    public String getName() { return name; }
    public String getNric() { return nric; }
    public String getPassword() { return password; }
    public void setPassword(String pw) { this.password = pw; }


    public void assignOfficersToProject(List<BTOProject> projects, List<HDBOfficer> officers, Scanner sc) {
        System.out.println("--- Assign Officers to Project ---");

        // Step 1: Show manager's projects
        List<BTOProject> myProjects = new ArrayList<>();
        for (BTOProject p : projects) {
            if (p.getManagerName().equals(this.name)) {
                myProjects.add(p);
            }
        }
        if (myProjects.isEmpty()) {
            System.out.println("You have no projects to assign officers to.");
            return;
        }

        for (int i = 0; i < myProjects.size(); i++) {
            BTOProject p = myProjects.get(i);
            System.out.println((i + 1) + ". " + p.getProjectName() + " (" + p.getOfficerNames().size() + "/" + p.getOfficerSlots() + " officers assigned)");
        }

        System.out.print("Select a project: ");
        try {
            int projIdx = Integer.parseInt(sc.nextLine()) - 1;
            if (projIdx < 0 || projIdx >= myProjects.size()) {
                System.out.println("Invalid project selection.");
                return;
            }

            BTOProject selectedProject = myProjects.get(projIdx);
            List<String> assigned = selectedProject.getOfficerNames();

            if (assigned.size() >= selectedProject.getOfficerSlots()) {
                System.out.println("This project already has the maximum number of officers.");
                return;
            }

            // Step 2: Show available officers
            List<HDBOfficer> available = new ArrayList<>();
            for (HDBOfficer o : officers) {
                // Check if the officer has applied for the current project, if so, they cannot register
                if (!assigned.contains(o.getName()) && o.getApplication() != null &&
                    o.getApplication().getProject().equals(selectedProject)) {
                    System.out.println("Officer " + o.getName() + " has already applied for this project and cannot register to handle it.");
                } else if (!assigned.contains(o.getName())) {
                    available.add(o);
                }
            }
            
            if (available.isEmpty()) {
                System.out.println("No more officers available for assignment.");
                return;
            }

            for (int i = 0; i < available.size(); i++) {
                System.out.println((i + 1) + ". " + available.get(i).getName());
            }

            System.out.print("Enter officer numbers to assign (comma-separated): ");
            String[] selections = sc.nextLine().split(",");
            for (String s : selections) {
                int idx = Integer.parseInt(s.trim()) - 1;
                if (idx >= 0 && idx < available.size() && assigned.size() < selectedProject.getOfficerSlots()) {
                    String officerName = available.get(idx).getName();
                    assigned.add(officerName);
                }
            }

            selectedProject.setOfficerNames(assigned);
            System.out.println("Officers successfully assigned.");

        } catch (Exception e) {
            System.out.println("Error assigning officers: " + e.getMessage());
        }
    }

    public void createProject(List<BTOProject> projects, List<HDBOfficer> officers, Scanner sc) {
        try {
            for (BTOProject p : projects) {
                if (p.getManagerName().equals(this.name) && p.isVisible() && isWithinApplicationPeriod(p)) {
                    System.out.println("You already have an active project (visible + within application period) and cannot create another.");
                    return;
                }
            }

            System.out.println("--- Create New BTO Project ---");
            System.out.print("Project Name: ");
            String name = sc.nextLine();
            for (BTOProject p : projects) {
                if (p.getProjectName().equalsIgnoreCase(name)) {
                    System.out.println("A project with this name already exists. Please choose a different name.");
                    return;
                }
            }

            System.out.print("Neighborhood: ");
            String neighborhood = sc.nextLine();

            System.out.print("Flat Type 1: ");
            String type1 = sc.nextLine();
            System.out.print("Number of units for Type 1: ");
            int units1 = Integer.parseInt(sc.nextLine());
            System.out.print("Selling price for Type 1: ");
            int price1 = Integer.parseInt(sc.nextLine());

            System.out.print("Flat Type 2: ");
            String type2 = sc.nextLine();
            System.out.print("Number of units for Type 2: ");
            int units2 = Integer.parseInt(sc.nextLine());
            System.out.print("Selling price for Type 2: ");
            int price2 = Integer.parseInt(sc.nextLine());

            System.out.print("Application opening date (YYYY-MM-DD): ");
            String openDate = sc.nextLine();
            System.out.print("Application closing date (YYYY-MM-DD): ");
            String closeDate = sc.nextLine();
            if (closeDate.compareTo(openDate) <= 0) {
                System.out.println("Error: Closing date must be later than opening date.");
                return;
            }

            System.out.print("Officer Slots (max 10): ");
            int officerSlots = Integer.parseInt(sc.nextLine());
            if (officerSlots < 1 || officerSlots > 10) {
                System.out.println("Invalid number of officer slots. Must be between 1 and 10.");
                return;
            }

            BTOProject newProject = new BTOProject(
                name, neighborhood, type1, units1, price1, type2, units2, price2, openDate, closeDate, this.name, officerSlots, new ArrayList<>());
            assignOfficersToProject(Collections.singletonList(newProject), officers, sc);    
            projects.add(newProject);
            System.out.println("Project created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating project: " + e.getMessage());
        }
    }

    private boolean isWithinApplicationPeriod(BTOProject p) {
        String today = java.time.LocalDate.now().toString();
        return today.compareTo(p.getOpenDate()) >= 0 && today.compareTo(p.getCloseDate()) <= 0;
    }

    public void editOrDeleteProject(List<BTOProject> projects, List<HDBOfficer> officers, Scanner sc) {
        System.out.println("\n--- Edit/Delete BTO Project ---");

        List<BTOProject> myProjects = new ArrayList<>();
        for (BTOProject p : projects) {
            if (p.getManagerName().equals(this.name)) {
                myProjects.add(p);
            }
        }

        if (myProjects.isEmpty()) {
            System.out.println("You have no projects to manage.");
            return;
        }

        for (int i = 0; i < myProjects.size(); i++) {
            System.out.println((i + 1) + ". " + myProjects.get(i).getProjectName());
        }

        System.out.print("Select a project to edit/delete (or 0 to cancel): ");
        try {
            int idx = Integer.parseInt(sc.nextLine()) - 1;
            if (idx == -1) return;

            if (idx < 0 || idx >= myProjects.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            BTOProject selected = myProjects.get(idx);

            System.out.println("1. Edit Project Name\n2. Edit Neighborhood\n3. Edit Flat Types, Units, Prices\n4. Edit Dates\n5. Edit Officer Slots\n6. Edit Assigned Officers\n7. Delete Project\n8. Cancel");
            System.out.print("Choose an action: ");
            int action = Integer.parseInt(sc.nextLine());

            switch (action) {
                case 1 -> {
                    System.out.print("Enter new project name: ");
                    selected.setProjectName(sc.nextLine());
                    System.out.println("Project name updated.");
                }
                case 2 -> {
                    System.out.print("Enter new neighborhood: ");
                    selected.setNeighborhood(sc.nextLine());
                    System.out.println("Neighborhood updated.");
                }
                case 3 -> {
                    System.out.print("Enter new Flat Type 1: ");
                    selected.setType1(sc.nextLine());
                    System.out.print("Enter new units for Type 1: ");
                    selected.setUnitsType1(Integer.parseInt(sc.nextLine()));
                    System.out.print("Enter new price for Type 1: ");
                    selected.setPriceType1(Integer.parseInt(sc.nextLine()));
                    System.out.print("Enter new Flat Type 2: ");
                    selected.setType2(sc.nextLine());
                    System.out.print("Enter new units for Type 2: ");
                    selected.setUnitsType2(Integer.parseInt(sc.nextLine()));
                    System.out.print("Enter new price for Type 2: ");
                    selected.setPriceType2(Integer.parseInt(sc.nextLine()));
                    System.out.println("Flat types, units and prices updated.");
                }
                case 4 -> {
                    System.out.print("Enter new opening date (YYYY-MM-DD): ");
                    selected.setOpenDate(sc.nextLine());
                    System.out.print("Enter new closing date (YYYY-MM-DD): ");
                    selected.setCloseDate(sc.nextLine());
                    System.out.println("Dates updated.");
                }
                case 5 -> {
                    System.out.print("Enter new officer slot count: ");
                    selected.setOfficerSlots(Integer.parseInt(sc.nextLine()));
                    System.out.println("Officer slot count updated.");
                }
                case 6 -> {
                    assignOfficersToProject(Collections.singletonList(selected), officers, sc);
                }
                case 7 -> {
                    projects.remove(selected);
                    System.out.println("Project deleted successfully.");
                }
                case 8 -> System.out.println("Cancelled.");
                default -> System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("Error editing project: " + e.getMessage());
        }
    }

    public void toggleProjectVisibility(List<BTOProject> projects, Scanner sc) {
        System.out.println("\n--- Toggle Project Visibility ---");
        List<BTOProject> myProjects = new ArrayList<>();

        for (BTOProject p : projects) {
            if (p.getManagerName().equals(this.name)) {
                myProjects.add(p);
            }
        }
    
        if (myProjects.isEmpty()) {
            System.out.println("You are not managing any projects.");
            return;
        }

        for (int i = 0; i < myProjects.size(); i++) {
            BTOProject p = myProjects.get(i);
            System.out.println((i + 1) + ". " + p.getProjectName() + " (Visible: " + p.isVisible() + ")");
        }
        System.out.print("Enter project number to toggle: ");
        try {
            int idx = Integer.parseInt(sc.nextLine()) - 1;
            if (idx >= 0 && idx < projects.size()) {
                BTOProject selected = myProjects.get(idx);
                boolean turningOn = !selected.isVisible();
                if (turningOn && isWithinApplicationPeriod(selected)) {
                    for (BTOProject p : myProjects) {
                        if (p != selected && p.isVisible() && isWithinApplicationPeriod(p)) {
                            System.out.println("Cannot turn visibility ON. You already have an active project during this application period.");
                            return;
                        }
                    }
                }
                selected.setVisible(turningOn);
                System.out.println("Visibility for " + selected.getProjectName() + " set to " + turningOn);
            } else {
                System.out.println("Invalid index.");
            }
        } catch (Exception e) {
            System.out.println("Error toggling visibility: " + e.getMessage());
        }
    }

    public void processWithdrawalRequests(List<Applicant> applicants, Scanner sc) {
        System.out.println("\n--- Withdrawal Requests (Managed Projects Only) ---");
        List<Applicant> pending = new ArrayList<>();
    
        for (Applicant a : applicants) {
            if (a.getApplication() != null &&
                a.getApplication().isWithdrawalRequested() &&
                a.getApplication().getProject().getManagerName().equals(this.name)) {
                pending.add(a);
            }
        }
    
        if (pending.isEmpty()) {
            System.out.println("No withdrawal requests for your projects at the moment.");
            return;
        }
    
        for (int i = 0; i < pending.size(); i++) {
            Applicant a = pending.get(i);
            String projectName = a.getApplication().getProject().getProjectName();
            System.out.println((i + 1) + ". " + a.getName() + " - Project: " + projectName);
        }
    
        System.out.print("Enter number to process or 0 to go back: ");
        try {
            int idx = Integer.parseInt(sc.nextLine()) - 1;
            if (idx == -1) return;
    
            if (idx >= 0 && idx < pending.size()) {
                Applicant selected = pending.get(idx);
                System.out.print("Approve withdrawal for " + selected.getName() + "? (yes/no): ");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("yes")) {
                    selected.clearApplication();
                    System.out.println("Withdrawal approved and application removed.");
                } else {
                    selected.getApplication().setWithdrawalRequested(false);
                    System.out.println("Withdrawal request rejected.");
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Error processing request: " + e.getMessage());
        }
    }

    public void processOfficerRegistrations(List<HDBOfficer> officers, List<BTOProject> projects, Scanner sc) {
        System.out.println("--- Process Officer Registrations ---");

        List<BTOProject> myProjects = new ArrayList<>();
        for (BTOProject p : projects) {
            if (p.getManagerName().equals(this.name)) {
                myProjects.add(p);
            }
        }

        if (myProjects.isEmpty()) {
            System.out.println("You have no projects to manage officers for.");
            return;
        }

        for (int i = 0; i < myProjects.size(); i++) {
            System.out.println((i + 1) + ". " + myProjects.get(i).getProjectName());
        }

        System.out.print("Select a project: ");
        try {
            int projIdx = Integer.parseInt(sc.nextLine()) - 1;
            if (projIdx < 0 || projIdx >= myProjects.size()) {
                System.out.println("Invalid project selection.");
                return;
            }

            BTOProject selected = myProjects.get(projIdx);
            List<String> assigned = new ArrayList<>(selected.getOfficerNames());
            int availableSlots = selected.getOfficerSlots() - assigned.size();

            List<HDBOfficer> pending = new ArrayList<>();
            for (HDBOfficer o : officers) {
                String status = o.getRegistrationStatusMap().get(selected.getProjectName());
                if (!assigned.contains(o.getName()) && status != null && status.equals("Pending")) {
                    pending.add(o);
                }
            }

            if (pending.isEmpty()) {
                System.out.println("No pending officer registrations.");
                return;
            }

            for (int i = 0; i < pending.size(); i++) {
                System.out.println((i + 1) + ". " + pending.get(i).getName());
            }

            System.out.print("Enter officer number to approve (or 0 to cancel): ");
            int sel = Integer.parseInt(sc.nextLine()) - 1;
            if (sel == -1) return;

            if (sel >= 0 && sel < pending.size() && assigned.size() < selected.getOfficerSlots()) {
                HDBOfficer approvedOfficer = pending.get(sel);
                approvedOfficer.updateRegistrationStatus(selected.getProjectName(), "Approved");
                assigned.add(pending.get(sel).getName());
                selected.setOfficerNames(assigned);
                System.out.println("Officer " + approvedOfficer.getName() + " approved and assigned to project.");
                System.out.println("Remaining officer slots: " + (selected.getOfficerSlots() - assigned.size()));
            } else {
                System.out.println("Invalid selection or no available slots.");
            }
        } catch (Exception e) {
            System.out.println("Error processing officer registration: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    public void processApplications(List<Applicant> applicants, List<BTOProject> projects, Scanner sc) {
        System.out.println("\n--- Process BTO Applications ---");

        List<BTOProject> myProjects = new ArrayList<>();
        for (BTOProject p : projects) {
            if (p.getManagerName().equals(this.name)) {
                myProjects.add(p);
            }
        }

        if (myProjects.isEmpty()) {
            System.out.println("You have no projects to process applications for.");
            return;
        }

        for (int i = 0; i < myProjects.size(); i++) {
            System.out.println((i + 1) + ". " + myProjects.get(i).getProjectName());
        }

        System.out.print("Select a project: ");
        try {
            int projIdx = Integer.parseInt(sc.nextLine()) - 1;
            if (projIdx < 0 || projIdx >= myProjects.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            BTOProject selected = myProjects.get(projIdx);

            List<Applicant> pending = new ArrayList<>();
            for (Applicant a : applicants) {
                if (a.getApplication() != null &&
                    a.getApplication().getProject().equals(selected) &&
                    a.getApplication().getStatus().equals("Pending")) {
                    pending.add(a);
                }
            }

            if (pending.isEmpty()) {
                System.out.println("No pending applications for this project.");
                return;
            }

            for (int i = 0; i < pending.size(); i++) {
                Applicant a = pending.get(i);
                System.out.println((i + 1) + ". " + a.getName() + " - " + a.getApplication().getFlatType());
            }

            System.out.print("Enter number to approve/reject or 0 to cancel: ");
            int sel = Integer.parseInt(sc.nextLine()) - 1;
            if (sel == -1) return;

            if (sel >= 0 && sel < pending.size()) {
                Applicant selectedApp = pending.get(sel);
                System.out.print("Approve or Reject (a/r): ");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("a")) {
                    selectedApp.getApplication().setStatus("Approved");
                    System.out.println("Application approved.");
                } else if (choice.equalsIgnoreCase("r")) {
                    selectedApp.getApplication().setStatus("Rejected");
                    System.out.println("Application rejected.");
                } else {
                    System.out.println("Invalid option.");
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Error processing applications: " + e.getMessage());
        }
    }

    public void generateReport(List<Applicant> applicants, List<BTOProject> projects, Scanner sc) {
        ReportGenerator.generateReport(applicants, projects, sc, this.getName());
    }

    public void viewAllProjects(List<BTOProject> projects, Scanner sc) {
        System.out.println("\n--- All BTO Projects ---");
        filters.promptUpdate(sc);

        List<BTOProject> filtered = new ArrayList<>();
        for (BTOProject p : projects) {
            if (filters.matches(p)) {
                filtered.add(p);
            }
        }
        filtered.sort(Comparator.comparing(BTOProject::getProjectName));

        if (filtered.isEmpty()) {
            System.out.println("No matching projects.");
        } else {
            for (BTOProject p : filtered) {
                p.displaySummary();
            }
        }
    }

    public void viewMyProjects(List<BTOProject> projects, Scanner sc) {
        System.out.println("\n--- My Projects ---");
        filters.promptUpdate(sc);

        List<BTOProject> filtered = new ArrayList<>();
        for (BTOProject p : projects) {
            if (p.getManagerName().equals(this.name) && filters.matches(p)) {
                filtered.add(p);
            }
        }
        filtered.sort(Comparator.comparing(BTOProject::getProjectName));

        if (filtered.isEmpty()) {
            System.out.println("No matching projects found.");
        } else {
            for (BTOProject p : filtered) {
                p.displaySummary();
            }
        }
    }

    // public void respondToEnquiries(List<Applicant> applicants, List<BTOProject> projects) {
    //     class EnquiryWrapper {
    //         Applicant applicant;
    //         Enquiry enquiry;
    //         String projectName;
    //         boolean canRespond;
    //
    //         EnquiryWrapper(Applicant a, Enquiry e, String pName, boolean canRespond) {
    //             applicant = a;
    //             enquiry = e;
    //             projectName = pName;
    //             this.canRespond = canRespond;
    //         }
    //     }
    //
    //     try {
    //         Scanner sc = new Scanner(System.in);
    //         System.out.println("--- Enquiries from Applicants (All Projects View) ---");
    //         List<EnquiryWrapper> allEnquiries = new ArrayList<>();
    //
    //         for (Applicant a : applicants) {
    //             for (Enquiry e : a.getEnquiries()) {
    //                 String projectName = e.getProjectName();
    //                 BTOProject matched = null;
    //                 for (BTOProject p : projects) {
    //                     if (p.getProjectName().equals(projectName)) {
    //                         matched = p;
    //                         break;
    //                     }
    //                 }
    //                 if (matched != null) {
    //                     boolean canRespond = matched.getManagerName().equals(this.getName());
    //                     allEnquiries.add(new EnquiryWrapper(a, e, projectName, canRespond));
    //                 }
    //             }
    //         }
    //
    //         if (allEnquiries.isEmpty()) {
    //             System.out.println("No enquiries found.");
    //             return;
    //         }
    //
    //         for (int i = 0; i < allEnquiries.size(); i++) {
    //             EnquiryWrapper ew = allEnquiries.get(i);
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
    //
    //         if (sel >= 0 && sel < allEnquiries.size()) {
    //             EnquiryWrapper selected = allEnquiries.get(sel);
    //             if (!selected.canRespond) {
    //                 System.out.println("You are not authorized to respond to this enquiry.");
    //                 return;
    //             }
    //             System.out.print("Enter response: ");
    //             String reply = sc.nextLine();
    //             selected.enquiry.setResponse(reply);
    //             selected.enquiry.setResponder("Manager " + this.getName());
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
        EnquiryResponseHandler.respondToEnquiries(applicants, projects, this.getName(), true);
    }

    public FilterOptions getFilters() {
        return filters;
    }

    public void setFilters(FilterOptions filters) {
        this.filters = filters;
    }

}



