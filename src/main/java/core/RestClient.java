package core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

public class RestClient {

    public JSONObject ping(String url, String userName, String userPassword) {
        HttpResponse response;
        try {
            if(userName == null || userPassword == null ||
                    userName.equals(StringUtils.EMPTY) || userPassword.equals(StringUtils.EMPTY))
                response = Unirest.get(url).asString();
            else
                response = Unirest.get(url).basicAuth(userName, userPassword).asJson();
            return JSONObject.fromObject(response.getBody());
        } catch (UnirestException ex) {
            throw new RuntimeException("Something went wrong: " + ex.getMessage());
        }
    }
}
