package core.github;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class MyRepository extends Repository {
    private String repositoryName;

    public MyRepository(GitHub gitHub, String repositoryNmae){
        super(gitHub);
        this.repositoryName = repositoryNmae;
    }

    @Override
    public GHRepository getRepository() throws IOException {
        return super.getGitHub().getMyself().getAllRepositories().get(repositoryName);
    }
}
