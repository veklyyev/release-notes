package core.github;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.Map;

public class OrganizationRepository extends Repository {
    private String repositoryName;
    private String organizationName;

    public OrganizationRepository(GitHub gitHub, String organizationName, String repositoryName){
        super(gitHub);
        this.repositoryName = repositoryName;
        this.organizationName = organizationName;
    }

    @Override
    public GHRepository getRepository() throws IOException {
        Map<String, GHOrganization> organizations = super.getGitHub().getMyOrganizations();
        GHOrganization org = organizations.get(organizationName);
        Map<String, GHRepository> repositories = org.getRepositories();
        return repositories.get(repositoryName);
    }
}
