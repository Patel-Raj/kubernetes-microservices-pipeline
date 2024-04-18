package com.cloud.validator.service.impl;

import com.cloud.validator.exceptions.CalculateException;
import com.cloud.validator.exceptions.FileMissingException;
import com.cloud.validator.dto.CalculateRequest;
import com.cloud.validator.dto.SaveRequest;
import com.cloud.validator.service.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Value("${directory}")
    private String directory;

    @Value("${calculator-service-url}")
    private String calculatorServiceUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void saveFile(SaveRequest request) {
        if (request.getFile() == null) {
            throw new FileMissingException("Invalid JSON input.");
        }

        if (!request.getFile().contains(".")) {
            throw new FileMissingException("Error while storing the file to the storage.");
        }

        try {
            String filename = directory + request.getFile();
            Path directoryPath = Paths.get(directory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectory(directoryPath);
            }
            Path path = Paths.get(filename);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, request.getData().getBytes());

        } catch (IOException e) {
            throw new FileMissingException("Error while storing the file to the storage.");
        }
    }

    @Override
    public String calcualte(CalculateRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = null;
        try {
            requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            ResponseEntity<String> response = restTemplate.exchange(calculatorServiceUrl + "calculate", HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (HttpServerErrorException e) {
            throw new CalculateException(e.getResponseBodyAsString());
        }
    }
}
