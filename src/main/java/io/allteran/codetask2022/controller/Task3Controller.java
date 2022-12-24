package io.allteran.codetask2022.controller;

import io.allteran.codetask2022.util.ResponseDecorator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.ResourceDecoder;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("task3")
public class Task3Controller {
    @Value("${uri.okg-se}")
    private String URI_TASK3;
    private final WebClient.Builder webClientBuilder;

    public Task3Controller(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("get-info")
    public ResponseEntity<String> getAndPrintO3() {

//        String response = webClientBuilder.build().get()
//                .uri(URI_TASK3)
//                .header("Content-Type", "text/html; charset=UTF-8")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();

//        String response = webClientBuilder.filter((request, next) -> next.exchange(request)
//                .map(r -> {
//                    ResponseDecorator decorated = new ResponseDecorator(r);
//                }))

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(URI_TASK3, String.class);

        return response;
    }
}
