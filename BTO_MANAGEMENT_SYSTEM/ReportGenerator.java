package service;

import application.BTOApplication;
import project.BTOProject;
import user.Applicant;

import java.util.*;

public class ReportGenerator {

    public static void generateReport(List<Applicant> applicants, List<BTOProject> projects, Scanner sc, String managerName) {
        try {
            System.out.println("\n--- Generate Applicant Report ---");

            System.out.println("Choose a filter option for generating the report:");
            System.out.println("1. By Flat Type (2-Room / 3-Room)");
            System.out.println("2. By Project Name");
            System.out.println("3. By Applicant's Age");
            System.out.println("4. By Applicant's Marital Status");
            System.out.println("5. View All Applicants");
            System.out.print("Enter choice: ");
            int filterChoice = Integer.parseInt(sc.nextLine());

            List<Applicant> filteredApplicants = new ArrayList<>();
            for (Applicant a : applicants) {
                if (a.getApplication() != null && a.getApplication().getProject().getManagerName().equals(managerName)) {
                    filteredApplicants.add(a);
                }
            }

            switch (filterChoice) {
                case 1 -> {
                    System.out.print("Enter Flat Type (2-Room or 3-Room): ");
                    String flatType = sc.nextLine();
                    filteredApplicants.removeIf(a -> !flatType.equals(a.getApplication().getFlatType()));
                }
                case 2 -> {
                    System.out.print("Enter Project Name: ");
                    String projectName = sc.nextLine();
                    filteredApplicants.removeIf(a -> !projectName.equalsIgnoreCase(a.getApplication().getProject().getProjectName()));
                }
                case 3 -> {
                    System.out.print("Enter minimum age: ");
                    int minAge = Integer.parseInt(sc.nextLine());
                    filteredApplicants.removeIf(a -> a.getAge() < minAge);
                }
                case 4 -> {
                    System.out.print("Enter Marital Status (Single / Married): ");
                    String maritalStatus = sc.nextLine();
                    filteredApplicants.removeIf(a -> !a.getMaritalStatus().equalsIgnoreCase(maritalStatus));
                }
                case 5 -> {
                    // No extra filter needed
                }
                default -> System.out.println("Invalid option, showing all applicants.");
            }

            if (!filteredApplicants.isEmpty()) {
                System.out.println("\n--- Applicant Report ---");
                System.out.println("Name | NRIC | Age | Marital Status | Flat Type | Project Name");
                for (Applicant a : filteredApplicants) {
                    BTOApplication app = a.getApplication();
                    BTOProject proj = app.getProject();
                    System.out.println(a.getName() + " | " + a.getNric() + " | " + a.getAge() + " | " + a.getMaritalStatus()
                            + " | " + app.getFlatType() + " | " + (proj != null ? proj.getProjectName() : "N/A"));
                }
            } else {
                System.out.println("No applicants found for the selected filters.");
            }
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
} 
