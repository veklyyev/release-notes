package business;

import core.github.Repository;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


class GitHubBO {
    private List<GHPullRequest> closedPRs;

    GitHubBO(Repository gitHubRepository){
        init(gitHubRepository);
    }

    private void init(Repository gitHubRepository){
        try {
            closedPRs = gitHubRepository.getRepository().getPullRequests(GHIssueState.CLOSED);
            closedPRs.sort(Comparator.comparing(GHPullRequest::getMergedAt));
        } catch (IOException ex){
            throw new RuntimeException("Something went wrong " + ex.getMessage());
        } catch (NullPointerException ex){
            throw new RuntimeException("Cannot connect to repository " + ex.getMessage());
        }
    }

    List<GHPullRequest> filterMergedPRs(Date date){
        return closedPRs.stream()
                .filter(x -> (x.getMergedAt() != null && x.getMergedAt().before(date)))
                .collect(Collectors.toList());
    }
}
