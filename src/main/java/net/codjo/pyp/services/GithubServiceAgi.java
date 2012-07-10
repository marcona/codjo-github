package net.codjo.pyp.services;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.codjo.pyp.PypApplication;
import org.apache.wicket.Component;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
/**
 *
 */
public class GithubServiceAgi {
    private String githubLogin = "codjo";
    private String githubPassword = "XXX";

    public static GithubServiceAgi getGithubService(Component wicketComponent) {
        return ((PypApplication)wicketComponent.getApplication()).getBrinService();
    }

    public List<PullRequest> buildPullRequestList() throws IOException {
        List<PullRequest> pullRequests = new ArrayList<PullRequest>();
        GitHubClient client = new GitHubClient();
        client.setCredentials(githubLogin, githubPassword);

        RepositoryService repositoryService = new RepositoryService(client);
        List<Repository> repositories = repositoryService.getRepositories();

        PullRequestService service = new PullRequestService(client);

        for (Repository repository : repositories) {
            System.out.println("repository.getName() = " + repository.getName());
            List<PullRequest> pullRequestList = getClosedPullRequestsFor(service, repository.getName());
            pullRequests.addAll(pullRequestList);
        }
        return pullRequests;
    }


    private List<PullRequest> getClosedPullRequestsFor(PullRequestService service, String githubRepositoryId)
          throws IOException {
        RepositoryId repo = new RepositoryId(githubLogin, githubRepositoryId);
        return service.getPullRequests(repo, "closed");
    }
}
