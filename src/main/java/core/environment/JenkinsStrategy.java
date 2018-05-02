package core.environment;

import java.util.Date;

public class JenkinsStrategy implements EnvPropertyExtractor {
    /*
    TODO implement getting dates from Jenkins builds
     */
    @Override
    public Date getBuildDate() {
        return null;
    }

    @Override
    public String getBuildVersion() {
        return null;
    }
}
