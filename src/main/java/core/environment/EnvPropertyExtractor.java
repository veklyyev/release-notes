package core.environment;

import java.util.Date;

public interface EnvPropertyExtractor {
    Date getBuildDate();
    String getBuildVersion();
}
