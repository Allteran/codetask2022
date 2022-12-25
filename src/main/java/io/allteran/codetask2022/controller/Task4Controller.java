package io.allteran.codetask2022.controller;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("task4")
public class Task4Controller {
    @Value("${uri.task4}")
    private String URI_TASK4;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public Task4Controller(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/print-info")
    public ResponseEntity<String> printToConsole() {
        String response = webClientBuilder.build().get()
                .uri(URI_TASK4)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        String[] responseArray = response.split("[A-Z]{2}\\d{2}201[5-9]");
        for (int i = 1; i < responseArray.length; i++) {
            String name = responseArray[i].substring(14, 23).replace(",", "").replace(" ", "");
            if(name.endsWith("A")) {
                continue;
            }

            String[] dataLines = responseArray[i].split("\n");
            String year = dataLines[1].substring(0, 4);
            int maxWindSpeed = 0;
            for (int j = 1; j < dataLines.length; j++) {
                int windSpeed = Integer.parseInt(dataLines[j].substring(37, 42).replace(",", "").replace(" ", ""));
                if(windSpeed > maxWindSpeed) {
                    maxWindSpeed = windSpeed;
                }
            }
            System.out.println("Year: " + year + "," + "name: " + name + ", maximum sustained wind-speed: " + maxWindSpeed);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download-info")
    public HttpEntity<ByteArrayResource> printToFile() {
        String response = webClientBuilder.build().get()
                .uri(URI_TASK4)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        String[] responseArray = response.split("[A-Z]{2}\\d{2}201[5-9]");

        String output = "";
        for (int i = 1; i < responseArray.length; i++) {
            String name = responseArray[i].substring(14, 23).replace(",", "").replace(" ", "");
            if(name.endsWith("A")) {
                continue;
            }
            String[] dataLines = responseArray[i].split("\n");
            String year = dataLines[1].substring(0, 4);
            int maxWindSpeed = 0;
            for (int j = 1; j < dataLines.length; j++) {
                int windSpeed = Integer.parseInt(dataLines[j].substring(37, 42).replace(",", "").replace(" ", ""));
                if(windSpeed > maxWindSpeed) {
                    maxWindSpeed = windSpeed;
                }
            }
            output = output + "\nYear: " + year + "," + "name: " + name + ", maximum sustained wind-speed: " + maxWindSpeed;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Storm Wind Statistic.txt");
        return new HttpEntity<>(new ByteArrayResource(output.getBytes()), headers);
    }
}
