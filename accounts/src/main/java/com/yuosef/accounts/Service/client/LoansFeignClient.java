package com.yuosef.accounts.Service.client;

import com.yuosef.accounts.Dtos.CardsDto;
import com.yuosef.accounts.Dtos.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("loans")
public interface LoansFeignClient {

    @GetMapping(value = "/api/fetch",consumes = "application/json")
    public ResponseEntity<LoansDto> fetchloanDetails(@RequestParam String mobileNumber);

}
