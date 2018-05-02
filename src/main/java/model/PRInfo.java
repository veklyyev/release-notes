package model;

public class PRInfo{
    private String assignee;
    private String status;
    private String PRTitle;

    public PRInfo(String PRTitle, String status, String assignee){
        this.assignee = assignee;
        this.status = status;
        this.PRTitle = PRTitle;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getStatus() {
        return status;
    }

    public String getPRTitle() {
        return PRTitle;
    }
}
