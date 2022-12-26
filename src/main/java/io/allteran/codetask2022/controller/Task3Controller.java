package io.allteran.codetask2022.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.allteran.codetask2022.dto.Task3DTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("task3")
public class Task3Controller {
    @Value("${uri.task3}")
    private String URI_TASK3;
    private final WebClient.Builder webClientBuilder;

    public Task3Controller(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @SneakyThrows
    @GetMapping("print-info")
    public ResponseEntity<String> getAndPrintO3() {
        String response = webClientBuilder.build().get()
                //thanks to reverse engineering and God I found exactly that URL that gives actual info about O3 level in JSON
                //it was stored in component---src-templates-startpage-container-js-cb21106a3721eccdf9d8.js
                .uri(URI_TASK3)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Task3DTO output = new ObjectMapper().readValue(response, Task3DTO.class);
        LocalDateTime time = LocalDateTime.parse(output.getTime().substring(0, output.getTime().lastIndexOf("+1")).replace(" ", ""));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timeString = time.format(dtf);

        System.out.println("value:" + output.getOutput() + ", time:" + timeString);

        return ResponseEntity.ok("SUCCESS");
    }
}
