package io.allteran.codetask2022.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/task1")
public class Task1Controller {
    @Value("${uri.electricity-imbalance}")
    private String URI_TASK1;
    private final WebClient.Builder webClientBuilder;

    public Task1Controller(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/download-table")
    public HttpEntity<ByteArrayResource> main() {
        String response = webClientBuilder.build().get()
                .uri(URI_TASK1)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //let's download table with data that we need. Experimental way we concluded that it's table that starts with next HTML properties
        String htmlTable = response.substring(response.lastIndexOf("bigtable left-sticky")-12, response.lastIndexOf("</table>")+8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Statistics Electricity Imbalance.html");

        return new HttpEntity<>(new ByteArrayResource(htmlTable.getBytes()),headers);
    }
}
