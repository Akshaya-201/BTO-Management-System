package application;

import project.BTOProject;

public class BTOApplication {
    private String status; // "Pending", "Successful", etc.
    private BTOProject project;
    private String flatType;
    private boolean withdrawalRequested = false;

    public BTOApplication(String status, BTOProject project, String flatType) {
        this.status = status;
        this.project = project;
        this.flatType = flatType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BTOProject getProject() {
        return project;
    }

    public String getFlatType() {
        return flatType;
    }

    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    public void setWithdrawalRequested(boolean withdrawalRequested) {
        this.withdrawalRequested = withdrawalRequested;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }
    
}