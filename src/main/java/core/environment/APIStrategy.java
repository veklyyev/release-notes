package core.environment;

import core.RestClient;
import net.sf.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class APIStrategy implements EnvPropertyExtractor {
    private RestClient restClient;
    private String buildDateRegex;
    private String buildVersionRegex;
    private JSONObject jsonData;


    public APIStrategy (){
        this.restClient = new RestClient();
        this.buildDateRegex = System.getProperty("buildDateRegex");
        this.buildVersionRegex = System.getProperty("buildVersionRegex");
    }

    public void callURL(String url){
        jsonData = restClient.ping(url);
    }

    @Override
    public Date getBuildDate() {
        String dateString = extractDataFromJson(jsonData, buildDateRegex);
        ZoneOffset offset = ZoneOffset.UTC;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        ZonedDateTime zdt = ZonedDateTime.of(dateTime, offset);
        return Date.from(zdt.toInstant());
    }

    @Override
    public String getBuildVersion() {
        return extractDataFromJson(jsonData, buildVersionRegex);
    }

    private String extractDataFromJson(JSONObject json, String dateRegex) {
        return json.get(dateRegex).toString();
    }
}
