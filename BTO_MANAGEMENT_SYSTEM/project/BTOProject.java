// ===========================
// File: project/BTOProject.java
// ===========================
package project;

import java.util.*;

public class BTOProject {
    private String projectName;
    private String neighborhood;
    private String type1;
    private int unitsType1;
    private int priceType1;
    private String type2;
    private int unitsType2;
    private int priceType2;
    private String openDate;
    private String closeDate;
    private String managerName;
    private int officerSlots;
    private List<String> officerNames;
    private boolean isVisible = true;

    public BTOProject(String projectName, String neighborhood, String type1, int unitsType1, int priceType1,
                      String type2, int unitsType2, int priceType2, String openDate, String closeDate,
                      String managerName, int officerSlots, List<String> officerNames) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.type1 = type1;
        this.unitsType1 = unitsType1;
        this.priceType1 = priceType1;
        this.type2 = type2;
        this.unitsType2 = unitsType2;
        this.priceType2 = priceType2;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.managerName = managerName;
        this.officerSlots = officerSlots;
        this.officerNames = officerNames;
    }

    public String getProjectName() { return projectName; }
    public String getNeighborhood() { return neighborhood; }
    public String getType1() { return type1; }
    public int getUnitsType1() { return unitsType1; }
    public int getPriceType1() { return priceType1; }
    public String getType2() { return type2; }
    public int getUnitsType2() { return unitsType2; }
    public int getPriceType2() { return priceType2; }
    public String getOpenDate() { return openDate; }
    public String getCloseDate() { return closeDate; }
    public String getManagerName() { return managerName; }
    public int getOfficerSlots() { return officerSlots; }
    public List<String> getOfficerNames() { return officerNames; }
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
    public void setManager(String manager) { managerName = manager; }
    public void setOfficerNames(List<String> officerNames) { this.officerNames = officerNames; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
    public void setType1(String type1) { this.type1 = type1; }
    public void setUnitsType1(int unitsType1) { this.unitsType1 = unitsType1; }
    public void setPriceType1(int priceType1) { this.priceType1 = priceType1; }
    public void setType2(String type2) { this.type2 = type2; }
    public void setUnitsType2(int unitsType2) { this.unitsType2 = unitsType2; }
    public void setPriceType2(int priceType2) { this.priceType2 = priceType2; }
    public void setOpenDate(String openDate) { this.openDate = openDate; }
    public void setCloseDate(String closeDate) { this.closeDate = closeDate; }
    public void setOfficerSlots(int officerSlots) { this.officerSlots = officerSlots; }

    
    

    public void displaySummary() {
        System.out.println("Project: " + projectName + ", " + neighborhood);
        System.out.println("  " + type1 + " - $" + priceType1 + " (" + unitsType1 + " units)");
        System.out.println("  " + type2 + " - $" + priceType2 + " (" + unitsType2 + " units)");
        System.out.println("  Application Period: " + openDate + " to " + closeDate);
        System.out.println("-------------------------------------------");
    }
}