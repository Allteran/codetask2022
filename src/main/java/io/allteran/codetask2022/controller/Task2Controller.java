package io.allteran.codetask2022.controller;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/task2")
public class Task2Controller {
    @Value("${uri.electricity-imbalance}")
    private String URI_EL_IMBALANCE;
    @Value("${uri.ote-cr}")
    private String URI_OTE_CR;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public Task2Controller(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @SneakyThrows
    @GetMapping(value = "/get-info", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<String> getInfo() {
        String htmlBody = webClientBuilder.build().get()
                .uri(URI_EL_IMBALANCE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        String xlsLink = URI_OTE_CR + htmlBody.substring(htmlBody.lastIndexOf("/pubweb/attachments"), htmlBody.indexOf(".xls", htmlBody.lastIndexOf("/pubweb/attachments"))) + ".xls";
        RestTemplate restTemplate = new RestTemplate();
        byte[] fileBytes = restTemplate.getForObject(xlsLink, byte[].class);

        Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(fileBytes));

        Sheet mainSheet = workbook.getSheet("Imbalances V0");

        String dateHeader = mainSheet.getRow(2).getCell(0).getStringCellValue();
        String date = dateHeader.substring(dateHeader.lastIndexOf("-")+2);

        int cellIndex = 1;
        while(mainSheet.getRow(5).getCell(cellIndex) != null) {
            String header = "";
            //case for headers
            String fullHeader = mainSheet.getRow(5).getCell(cellIndex).getStringCellValue();
            header = fullHeader.substring(0, fullHeader.lastIndexOf("(") - 1).replace("\n", "");
            int rowIndex = 6;
            while(mainSheet.getRow(rowIndex) != null) {
                int timeInt = (int) mainSheet.getRow(rowIndex).getCell(0).getNumericCellValue();
                String time = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("H").parse("" + timeInt));
                double value = mainSheet.getRow(rowIndex).getCell(cellIndex).getNumericCellValue();
                String output = header + ";" + date + " " + time + "; " + value;
                System.out.println(output);
                rowIndex++;
            }
            cellIndex++;
        }
        return ResponseEntity.ok("SUCCESSFUL");
    }
}
