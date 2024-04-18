package com.cloud.validator.controller;

import com.cloud.validator.dto.SaveFailResponse;
import com.cloud.validator.exceptions.CalculateException;
import com.cloud.validator.exceptions.FileMissingException;
import com.cloud.validator.exceptions.FileStorageException;
import com.cloud.validator.dto.CalculateRequest;
import com.cloud.validator.dto.SaveRequest;
import com.cloud.validator.dto.SaveResponse;
import com.cloud.validator.service.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


// comment
@RestController
public class Controller {

    @Autowired
    private Service service;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/store-file")
    public ResponseEntity<String> saveFile(@RequestBody SaveRequest request) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        // Set content type to application/json
        headers.set("Content-Type", "application/json");
        SaveResponse saveResponse = new SaveResponse();
        try {
            service.saveFile(request);
        } catch (FileMissingException e) {
            SaveFailResponse saveFailResponse = new SaveFailResponse();
            saveFailResponse.setFile(null);
            saveFailResponse.setError(e.getMessage());
            String response = objectMapper.writeValueAsString(saveFailResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (FileStorageException e) {
            SaveFailResponse saveFailResponse = new SaveFailResponse();
            saveFailResponse.setFile(request.getFile());
            saveFailResponse.setError(e.getMessage());
            String response = objectMapper.writeValueAsString(saveFailResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        saveResponse.setFile(request.getFile());
        saveResponse.setMessage("Success.");
        String response = objectMapper.writeValueAsString(saveResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/calculate")
    public ResponseEntity<String> calculate(@RequestBody CalculateRequest request) {
        HttpHeaders headers = new HttpHeaders();

        // Set content type to application/json
        headers.set("Content-Type", "application/json");
        try {
            String response = service.calcualte(request);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (CalculateException e) {
            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Check Version:2");
    }
}
