package com.cloud.calculator.service.impl;


import com.cloud.calculator.dto.CalculateRequest;
import com.cloud.calculator.dto.CalculateSuccessResponse;
import com.cloud.calculator.exceptions.FileMissingException;
import com.cloud.calculator.exceptions.FileNameNotPresentException;
import com.cloud.calculator.service.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Value("${directory}")
    private String directory;

    @Override
    public CalculateSuccessResponse calculate(CalculateRequest request) {
        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw new FileNameNotPresentException("Invalid JSON input.");
        }

        return parseAndCalculate(request);
    }

    private CalculateSuccessResponse parseAndCalculate(CalculateRequest request) {
        String filePath = directory + request.getFile();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileMissingException("File not found.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0;
            int sum = 0;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 2) {
                    throw new FileMissingException("Input file not in CSV format.");
                }

                if (index == 0 && (!"product".equalsIgnoreCase(values[0].trim()) || !"amount".equalsIgnoreCase(values[1].trim()))) {
                    throw new FileMissingException("Input file not in CSV format.");
                }

                if (index != 0) {
                    String productName = values[0].trim();
                    Integer price;
                    try {
                        price = Integer.parseInt(values[1].trim());
                    } catch (Exception e) {
                        throw new FileMissingException("Input file not in CSV format.");
                    }

                    if (request.getProduct().equalsIgnoreCase(productName)) {
                        sum += price;
                    }
                }
                index++;
            }

            if (index == 0) {
                throw new FileMissingException("Input file not in CSV format.");
            }

            CalculateSuccessResponse calculateSuccessResponse = new CalculateSuccessResponse();
            calculateSuccessResponse.setFile(request.getFile());
            calculateSuccessResponse.setSum(sum);
            return calculateSuccessResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
