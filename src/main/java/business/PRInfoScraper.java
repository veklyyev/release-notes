package business;

import core.github.MyRepository;
import model.Environment;
import model.PRInfo;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;

import java.util.*;
import java.util.stream.Collectors;

public class PRInfoScraper {
    private GitHubBO gitHubBO;
    private JiraBO jiraBO;
    private List<Environment> environments;

    public PRInfoScraper(JiraClient jira, GitHub gitHub, List<Environment> environments){
        this.jiraBO = new JiraBO(jira);
        this.gitHubBO = new GitHubBO(new MyRepository(gitHub, System.getProperty("repositoryGitHub")));
        this.environments = environments;
    }

    private List<PRInfo> fetchPRInfo(Date beforeDate){
        List<PRInfo> prInfos = new ArrayList<>();
        List<GHPullRequest> pullRequests = gitHubBO.filterMergedPRs(beforeDate);
        pullRequests.forEach(pr -> {
            Issue issue = jiraBO.getTicketById(JiraBO.tryParseJiraKey(pr.getTitle()));
            prInfos.add(new PRInfo(pr.getTitle(), issue.getStatus().getName(), issue.getAssignee().getName()));
        });
        return prInfos;
    }

    public Map<Environment, List<PRInfo>> getEnvironmentsInfo() {
        Map<Environment, List<PRInfo>> prsInfo = new HashMap<>();
        for(Environment env: environments){
            List<PRInfo> prInfos = fetchPRInfo(env.getBuildDate());
            prsInfo.put(env, prInfos);
        }
        return cutData(prsInfo);
    }

    public static Map<Environment, List<PRInfo>> cutData(Map<Environment, List<PRInfo>> prsInfo){

        List<Environment> environments = prsInfo.keySet().stream()
                .sorted(Comparator.comparing(Environment::getBuildDate))
                .collect(Collectors.toList());

        Map<Environment, List<PRInfo>> cutMap = new LinkedHashMap<>();

        List<PRInfo> newerEnv = new ArrayList<>();
        for(Environment env: environments){
            List<PRInfo> olderEnv = new ArrayList<>(prsInfo.get(env));
            olderEnv.removeAll(newerEnv);
            cutMap.put(env, olderEnv);
            newerEnv = prsInfo.get(env);
        }
        return cutMap;
    }
}
