package product;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Configuration
@EnableWebSecurity
public class JsonUserFactory {

    @Bean
    List<User> getUsers() throws IOException, ParseException {
        List<User> users = new ArrayList<>();

        Iterator<JSONObject> iter = parseJson().iterator();
        while (iter.hasNext()) {
            JSONObject jsonObject = iter.next();
            User user = new User(
                    (String) jsonObject.get("username"),
                    (String) jsonObject.get("password"),
                    (String) jsonObject.get("role")
            );
            users.add(user);
        }
        return users;
    }

    private JSONArray parseJson() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        File file = ResourceUtils.getFile("classpath:users.json");
        FileReader fileReader  = new FileReader(file);
        return (JSONArray) parser.parse(fileReader);
    }
}
