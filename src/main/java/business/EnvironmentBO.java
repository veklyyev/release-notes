package business;

import core.environment.APIStrategy;
import model.Environment;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentBO {

    public List<Environment> getEnvironmentsFromUrls(String... urls){
        APIStrategy dataExtractor = new APIStrategy();
        List<Environment> environments = new ArrayList<>();
        for(String url: urls){
            Environment env = new Environment();
            env.setName(url);
            dataExtractor.callURL(url);
            env.setBuildDate(dataExtractor.getBuildDate());
            env.setBuildVersion(dataExtractor.getBuildVersion());
            environments.add(env);
        }
        return environments;
    }
}
