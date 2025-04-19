// ===========================
// File: util/UserLoader.java
// ===========================
package util;

import user.*;
import project.BTOProject;
import java.io.*;
import java.util.*;

public class UserLoader {
    public static void loadApplicants(List<Applicant> applicants) {
        try (BufferedReader br = new BufferedReader(new FileReader("ApplicantList.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                String nric = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String maritalStatus = data[3].trim();
                String password = data[4].trim();
                applicants.add(new Applicant(name, nric, password, age, maritalStatus));
            }
        } catch (IOException e) {
            System.out.println("Error reading applicant list: " + e.getMessage());
        }
    }

    public static void loadManagers(List<HDBManager> managers) {
        try (BufferedReader br = new BufferedReader(new FileReader("ManagerList.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                String nric = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String maritalStatus = data[3].trim();
                String password = data[4].trim();
                managers.add(new HDBManager(name, nric, password, age, maritalStatus));
            }
        } catch (IOException e) {
            System.out.println("Error reading manager list: " + e.getMessage());
        }
    }

    public static void loadOfficers(List<HDBOfficer> officers) {
        try (BufferedReader br = new BufferedReader(new FileReader("OfficerList.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                String nric = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String maritalStatus = data[3].trim();
                String password = data[4].trim();
                officers.add(new HDBOfficer(name, nric, password, age, maritalStatus));
            }
        } catch (IOException e) {
            System.out.println("Error reading officer list: " + e.getMessage());
        }
    }

    public static void loadProjects(List<BTOProject> projects) {
        try (BufferedReader br = new BufferedReader(new FileReader("ProjectList.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                String projectName = data[0].trim();
                String neighborhood = data[1].trim();
                String type1 = data[2].trim();
                int units1 = Integer.parseInt(data[3].trim());
                int price1 = Integer.parseInt(data[4].trim());
                String type2 = data[5].trim();
                int units2 = Integer.parseInt(data[6].trim());
                int price2 = Integer.parseInt(data[7].trim());
                String openDate = data[8].trim();
                String closeDate = data[9].trim();
                String manager = data[10].trim();
                int slots = Integer.parseInt(data[11].trim());
                List<String> officers = new ArrayList<>();
                if (data.length > 12 && !data[12].isEmpty()) {
                    String cleaned = data[12].replaceAll("^\"|\"$", "").trim();
                    officers = Arrays.stream(cleaned.split(";|,")).map(String::trim).toList();
                }
                projects.add(new BTOProject(projectName, neighborhood, type1, units1, price1,
                        type2, units2, price2, openDate, closeDate, manager, slots, officers));
            }
        } catch (IOException e) {
            System.out.println("Error reading project list: " + e.getMessage());
        }
    }
}
