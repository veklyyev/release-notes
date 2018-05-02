package core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.sf.json.JSONObject;

public class RestClient {

    public JSONObject ping(String url) {
        try {
            //HttpResponse response = Unirest.get(url).basicAuth("compass", "!!g0g0ap1").asJson();
            HttpResponse response = Unirest.get(url).asString();
            return JSONObject.fromObject(response.getBody());
        } catch (UnirestException ex) {
            throw new RuntimeException("Something went wrong: " + ex.getMessage());
        }
    }
}
