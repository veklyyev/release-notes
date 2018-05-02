package business;

import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JiraBO {
    private JiraClient jira;
    private static String jiraKeyRegex = System.getProperty("jiraKeyRegex");

    public JiraBO(JiraClient jiraClient) {
        this.jira = jiraClient;
    }

    public Issue getTicketById(String jiraKey) {
        try {
            return jira.getIssue(jiraKey);
        } catch (JiraException ex) {
            System.out.println("Invalid Jira key! " + ex.getMessage());
        }
        return null;
    }

    public static String tryParseJiraKey(String text) {
        Pattern jiraKeyPattern = Pattern.compile(jiraKeyRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = jiraKeyPattern.matcher(text);
        if (matcher.find())
            return matcher.group(1).trim().replaceAll(" ", "-");
        else
            return null;
    }
}
