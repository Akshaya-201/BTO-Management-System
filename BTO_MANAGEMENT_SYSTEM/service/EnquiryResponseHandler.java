package service;

import user.Applicant;
import enquiry.Enquiry;
import project.BTOProject;

import java.util.*;

public class EnquiryResponseHandler {

    private static class EnquiryWrapper {
        Applicant applicant;
        Enquiry enquiry;
        String projectName;
        boolean canRespond;

        EnquiryWrapper(Applicant a, Enquiry e, String pName, boolean canRespond) {
            applicant = a;
            enquiry = e;
            projectName = pName;
            this.canRespond = canRespond;
        }
    }

    public static void respondToEnquiries(List<Applicant> applicants, List<BTOProject> projects, String responderName, boolean isManager) {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- Enquiries from Applicants ---");

        List<EnquiryWrapper> validEnquiries = new ArrayList<>();

        for (Applicant a : applicants) {
            for (Enquiry e : a.getEnquiries()) {
                String projectName = e.getProjectName();
                BTOProject matched = findProjectByName(projects, projectName);
                if (matched != null) {
                    boolean canRespond = isManager
                        ? matched.getManagerName().equals(responderName)
                        : matched.getOfficerNames().contains(responderName);

                    if (!isManager && !canRespond) continue; // officers can only see own projects

                    validEnquiries.add(new EnquiryWrapper(a, e, projectName, canRespond));
                }
            }
        }

        if (validEnquiries.isEmpty()) {
            System.out.println("No enquiries to respond to.");
            return;
        }

        for (int i = 0; i < validEnquiries.size(); i++) {
            EnquiryWrapper ew = validEnquiries.get(i);
            System.out.println((i + 1) + ". Project: " + ew.projectName);
            System.out.println("   From: " + ew.applicant.getName());
            System.out.println("   Enquiry: " + ew.enquiry.getContent());
            String response = ew.enquiry.getResponse();
            if (response != null && !response.isEmpty()) {
                System.out.println("   ↳ Response by: [" + ew.enquiry.getResponder() + "] " + response);
            }
        }

        System.out.print("Enter the number of the enquiry to respond to (0 to cancel): ");
        int sel = Integer.parseInt(sc.nextLine()) - 1;
        if (sel == -1) return;

        if (sel >= 0 && sel < validEnquiries.size()) {
            EnquiryWrapper selected = validEnquiries.get(sel);
            if (!selected.canRespond) {
                System.out.println("You are not authorized to respond to this enquiry.");
                return;
            }
            System.out.print("Enter response: ");
            String reply = sc.nextLine();
            selected.enquiry.setResponse(reply);
            selected.enquiry.setResponder((isManager ? "Manager " : "Officer ") + responderName);
            System.out.println("Response recorded.");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private static BTOProject findProjectByName(List<BTOProject> projects, String name) {
        for (BTOProject p : projects) {
            if (p.getProjectName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }
}  
