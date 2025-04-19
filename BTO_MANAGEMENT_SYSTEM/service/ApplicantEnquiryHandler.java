package service;

import user.Applicant;
import enquiry.Enquiry;
import project.BTOProject;

import java.util.List;
import java.util.Scanner;

public class ApplicantEnquiryHandler {

    public void manageEnquiries(Applicant applicant, List<BTOProject> projects, Scanner sc) {
        List<Enquiry> enquiries = applicant.getEnquiries();
        while (true) {
            System.out.println("\nEnquiries:");
            for (int i = 0; i < enquiries.size(); i++) {
                Enquiry enquiry = enquiries.get(i);
                System.out.println((i + 1) + ". [" + enquiry.getProjectName() + "] " + enquiry.getContent());
                String response = enquiry.getResponse();
                if (response != null && !response.isEmpty()) {
                    String responder = enquiry.getResponder();
                    String prefix = "Unknown";
                    if (responder != null && !responder.isEmpty()) {
                        if (responder.startsWith("Officer ")) {
                            prefix = "Officer";
                            responder = responder.replace("Officer ", "");
                        } else if (responder.startsWith("Manager ")) {
                            prefix = "Manager";
                            responder = responder.replace("Manager ", "");
                        } else {
                            prefix = responder;
                        }
                        System.out.println("  -> Response by: [" + prefix + "] " + responder + ": " + response);
                    } else {
                        System.out.println("  -> Response by: [Unknown] " + response);
                    }
                }
            }

            System.out.println("\n1. Add\n2. Edit\n3. Delete\n4. Back");
            System.out.print("Choose an option: ");
            String input = sc.nextLine();
            if (input.isEmpty()) continue;
            int c = Integer.parseInt(input);

            switch (c) {
                case 1 -> addEnquiry(applicant, projects, sc);
                case 2 -> editEnquiry(enquiries, sc);
                case 3 -> deleteEnquiry(enquiries, sc);
                case 4 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addEnquiry(Applicant applicant, List<BTOProject> projects, Scanner sc) {
        System.out.println("Available Projects:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i + 1) + ". " + projects.get(i).getProjectName());
        }
        System.out.print("Select project number to enquire about: ");
        int pIdx = Integer.parseInt(sc.nextLine()) - 1;
        if (pIdx < 0 || pIdx >= projects.size()) {
            System.out.println("Invalid project selection.");
            return;
        }
        String projectName = projects.get(pIdx).getProjectName();
        System.out.print("Enter enquiry: ");
        String content = sc.nextLine();
        if (!content.isEmpty()) applicant.getEnquiries().add(new Enquiry(content, projectName));
        else System.out.println("Enquiry cannot be empty.");
    }

    private void editEnquiry(List<Enquiry> enquiries, Scanner sc) {
        System.out.print("Which one to edit? ");
        int idx = Integer.parseInt(sc.nextLine()) - 1;
        if (idx >= 0 && idx < enquiries.size()) {
            System.out.print("New content: ");
            enquiries.get(idx).setContent(sc.nextLine());
        } else System.out.println("Invalid index.");
    }

    private void deleteEnquiry(List<Enquiry> enquiries, Scanner sc) {
        System.out.print("Which one to delete? ");
        int idx = Integer.parseInt(sc.nextLine()) - 1;
        if (idx >= 0 && idx < enquiries.size()) enquiries.remove(idx);
        else System.out.println("Invalid index.");
    }
} 
