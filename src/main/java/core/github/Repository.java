package core.github;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public abstract class Repository {
    private GitHub gitHub;

    GitHub getGitHub() {
        return gitHub;
    }

    Repository(GitHub gitHub){
        this.gitHub = gitHub;
    }

    public abstract GHRepository getRepository() throws IOException;
}
