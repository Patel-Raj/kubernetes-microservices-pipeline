package com.cloud.calculator.service;

import com.cloud.calculator.dto.CalculateRequest;
import com.cloud.calculator.dto.CalculateSuccessResponse;

public interface Service {
    CalculateSuccessResponse calculate(CalculateRequest request);
}
