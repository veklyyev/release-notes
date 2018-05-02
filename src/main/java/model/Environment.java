package model;

import java.util.Date;

public class Environment {
    private String name;
    private Date buildDate;
    private String buildVersion;

    public Environment(){}

    public String getName() {
        return name;
    }

    public Date getBuildDate() {
        return (Date)buildDate.clone();
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = (Date)buildDate.clone();
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }
}
