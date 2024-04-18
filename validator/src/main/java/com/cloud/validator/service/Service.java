package com.cloud.validator.service;

import com.cloud.validator.dto.CalculateRequest;
import com.cloud.validator.dto.SaveRequest;

public interface Service {
    void saveFile(SaveRequest request);

    String calcualte(CalculateRequest request);
}
