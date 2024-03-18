package utils;

import dto.CodeHunterRequest;
import dto.CodeHunterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpUtil {
    public static ResponseEntity<CodeHunterResponse> postCodeHunter(String url, CodeHunterRequest request){
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<CodeHunterResponse> forEntity = restTemplate.postForEntity(url, request, CodeHunterResponse.class);
            return forEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
