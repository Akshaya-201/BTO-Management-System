// ===========================
// File: main.java
// ===========================

package mainapp;

import user.*;
import util.*;
import project.BTOProject;
import application.BTOApplication;
import java.util.*;

import UI.ApplicantUIHandler;
import UI.ManagerUIHandler;
import UI.OfficerUIHandler;

class Main {
    static Scanner sc = new Scanner(System.in);
    static List<Applicant> applicants = new ArrayList<>();
    static List<HDBManager> managers = new ArrayList<>();
    static List<HDBOfficer> officers = new ArrayList<>();
    static List<BTOProject> projects = new ArrayList<>();

    public static void main(String[] args) {
        UserLoader.loadApplicants(applicants);
        UserLoader.loadManagers(managers);
        UserLoader.loadOfficers(officers);
        UserLoader.loadProjects(projects);

        while (true) { loginMenu(); }
    }

    static void loginMenu() {
        System.out.println("Welcome to the BTO Management System!");
        System.out.println("Select user type:\n1. Applicant\n2. HDB Manager\n3. HDB Officer\n4. Exit");
        int userType = Integer.parseInt(sc.nextLine());

        if (userType == 4) {
            System.out.println("Exiting program...");
            System.exit(0);
        }

        System.out.print("Enter NRIC (case sensitive): ");
        String nric = sc.nextLine();
        System.out.print("Enter password: ");
        String pw = sc.nextLine();

        switch (userType) {
            case 1 -> {
                for (Applicant a : applicants) {
                    if (a.getNric().equals(nric) && a.getPassword().equals(pw)) {
                        System.out.println("Welcome Applicant " + a.getName());
                        ApplicantUIHandler.launchMenu(a,projects,managers,officers,sc);
                        return;
                    }
                }
            }
            case 2 -> {
                for (HDBManager m : managers) {
                    if (m.getNric().equals(nric) && m.getPassword().equals(pw)) {
                        System.out.println("Welcome Manager " + m.getName());
                        ManagerUIHandler.launchMenu(m, projects, officers, applicants, sc);
                        return;
                    }
                }
            }
            case 3 -> {
                for (HDBOfficer o : officers) {
                    if (o.getNric().equals(nric) && o.getPassword().equals(pw)) {
                        System.out.println("Welcome Officer " + o.getName());
                        OfficerUIHandler.launchMenu(o, projects, applicants, managers, officers, sc);
                        return;
                    }
                }
            }
        }
        System.out.println("Invalid login. Try again.");
    }
}




