package io.allteran.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class MainController {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public MainController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("download-table")
    public HttpEntity<ByteArrayResource> main() {
        String response = webClientBuilder.build().get()
                .uri("https://www.ote-cr.cz/en/statistics/electricity-imbalances")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        String htmlTable = response.substring(response.lastIndexOf("bigtable left-sticky")-12, response.lastIndexOf("</table>")+8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Statistics Electricity Imbalance.html");

        return new HttpEntity<>(new ByteArrayResource(htmlTable.getBytes()),headers);
    }
}
