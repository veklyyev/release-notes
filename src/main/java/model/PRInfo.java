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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PRInfo prInfo = (PRInfo) o;

        if (assignee != null ? !assignee.equals(prInfo.assignee) : prInfo.assignee != null) return false;
        if (status != null ? !status.equals(prInfo.status) : prInfo.status != null) return false;
        return PRTitle != null ? PRTitle.equals(prInfo.PRTitle) : prInfo.PRTitle == null;
    }

    @Override
    public int hashCode() {
        int result = assignee != null ? assignee.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (PRTitle != null ? PRTitle.hashCode() : 0);
        return result;
    }
}
