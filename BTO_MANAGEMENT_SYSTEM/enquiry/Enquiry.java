// ===========================
// File: enquiry/Enquiry.java
// ===========================
package enquiry;

public class Enquiry {
    private String content;
    private String response = "";
    private String projectName;
    private String responder = "";

    public Enquiry(String content) {
        this.content = content;
    }

    public Enquiry(String content, String projectName) {
        this.content = content;
        this.projectName = projectName;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}


