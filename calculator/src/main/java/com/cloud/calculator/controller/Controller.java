package com.cloud.calculator.controller;

import com.cloud.calculator.config.RestTemplateConfig;
import com.cloud.calculator.dto.CalculateErrorResponse;
import com.cloud.calculator.dto.CalculateRequest;
import com.cloud.calculator.dto.CalculateSuccessResponse;
import com.cloud.calculator.exceptions.FileMissingException;
import com.cloud.calculator.exceptions.FileNameNotPresentException;
import com.cloud.calculator.service.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/calculate")
    public ResponseEntity<String> calculate(@RequestBody CalculateRequest request) throws JsonProcessingException {
        CalculateErrorResponse calculateErrorResponse = new CalculateErrorResponse();
        try {
            CalculateSuccessResponse response = service.calculate(request);
            String validResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.status(HttpStatus.OK).body(validResponse);
        } catch (FileNameNotPresentException e) {
            calculateErrorResponse.setFile(null);
            calculateErrorResponse.setError(e.getMessage());
            String response = objectMapper.writeValueAsString(calculateErrorResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (FileMissingException e) {
            calculateErrorResponse.setFile(request.getFile());
            calculateErrorResponse.setError(e.getMessage());
            String response = objectMapper.writeValueAsString(calculateErrorResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
