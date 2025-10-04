package com.yuosef.accounts.Service.client;

import com.yuosef.accounts.Dtos.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallBack implements LoansFeignClient{
    @Override
    public ResponseEntity<LoansDto> fetchloanDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
