package org.jenkinsci.plugin;

import business.EnvironmentBO;
import business.PRInfoScraper;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import model.Environment;
import model.PRInfo;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.JiraClient;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.kohsuke.github.GitHub;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReleaseNotesPrinter extends Builder {
    private static final Logger LOGGER = Logger.getLogger(ReleaseNotesPrinter.class.getName());

    private final String jiraLink;
    private final String ghRepositoryName;
    private final String ghOrganizationName;
    private final String jiraKeyRegex;
    private final String buildDateRegex;
    private final String buildVersionRegex;
    private final String devPath;
    private final String qaPath;
    private final String prodPath;


    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public ReleaseNotesPrinter(String jiraLink, String ghRepositoryName, String ghOrganizationName, String jiraKeyRegex,
                               String buildDateRegex, String buildVersionRegex,String devPath, String qaPath, String prodPath) {
        LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
        this.jiraLink = jiraLink;
        this.ghRepositoryName = ghRepositoryName;
        this.ghOrganizationName = ghOrganizationName;
        this.jiraKeyRegex = jiraKeyRegex;
        this.buildDateRegex = buildDateRegex;
        this.buildVersionRegex = buildVersionRegex;
        this.devPath = devPath;
        this.qaPath = qaPath;
        this.prodPath = prodPath;
    }

    /**
     * Get values from <tt>config.jelly</tt>.
     */

    public String getJiraLink() {
        return jiraLink;
    }

    public String getGhRepositoryName() {
        return ghRepositoryName;
    }

    public String getGhOrganizationName() {
        return ghOrganizationName;
    }

    public String getJiraKeyRegex() {
        return jiraKeyRegex;
    }

    public String getBuildDateRegex() {
        return buildDateRegex;
    }

    public String getBuildVersionRegex() {
        return buildVersionRegex;
    }

    public String getDevPath() {
        return devPath;
    }

    public String getQaPath() {
        return qaPath;
    }

    public String getProdPath() {
        return prodPath;
    }


    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException  {

        System.setOut(listener.getLogger());
        System.setErr(listener.getLogger());

        listener.getLogger().println("[Release notes] Start");
        listener.getLogger().println("[Release notes] Getting Global Configuration");

        setSystemProperties();

        launcher.getChannel().close();

        listener.getLogger().println("[Release notes] Getting PR data");

        GitHub gitHub = GitHub.connectUsingPassword(getDescriptor().getGhLogin(), getDescriptor().getGhPassword());
        JiraClient jira = new JiraClient(getJiraLink(), new BasicCredentials(getDescriptor().getJiraUsername(),
                getDescriptor().getJiraPassword()));
        List<Environment> environments = new EnvironmentBO().getEnvironmentsFromUrls(getDevPath(), getQaPath(), getProdPath());
        PRInfoScraper scraper = new PRInfoScraper(jira, gitHub, environments);
        Map<Environment, List<PRInfo>> prsInfo = scraper.getEnvironmentsInfo();
        listener.getLogger().println("[Release notes] Generating report");
        Report.generateReport(build.getWorkspace() + "", prsInfo);

        listener.getLogger().println("[Release notes] Done");
        return true;
    }

    private void setSystemProperties() {
        System.setProperty("reportRootPath", "testOutput");
        System.setProperty("repositoryGitHub", getGhRepositoryName());
        System.setProperty("buildDateRegex", getBuildDateRegex());
        System.setProperty("buildVersionRegex", getBuildVersionRegex());
        System.setProperty("jiraKeyRegex", getJiraKeyRegex());
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link ReleaseNotesPrinter}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/com/qvc/jenkins/plugin/SeleniumFramework/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private String jiraUsername;
        private String jiraPassword;

        private String ghLogin;
        private String ghPassword;

        private String apiUsername;
        private String apiPassword;

        /**
         * In order to load the persisted global configuration, you have to
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        /**
         * This human readable frameworkJAR is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Release notes printer";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information, set that to properties and call save()
            LOGGER.info("!!!!MAAAAAAAAAAAAAIN CLASSSSSSSSSSSSSSSS!!!!!!!!!!!!!!!!!!!!!!1");
            jiraUsername = formData.getString("jiraUsername");
            jiraPassword = formData.getString("jiraPassword");
            ghLogin = formData.getString("ghLogin");
            ghPassword = formData.getString("ghPassword");
            apiUsername = formData.getString("apiUsername");
            apiPassword = formData.getString("apiPassword");

            save();
            return super.configure(req,formData);
        }

        public String getJiraUsername() {
            return jiraUsername;
        }

        public String getJiraPassword() {
            return jiraPassword;
        }

        public String getGhLogin() {
            return ghLogin;
        }

        public String getGhPassword() {
            return ghPassword;
        }

        public String getApiUsername() {
            return apiUsername;
        }

        public String getApiPassword() {
            return apiPassword;
        }
    }
}