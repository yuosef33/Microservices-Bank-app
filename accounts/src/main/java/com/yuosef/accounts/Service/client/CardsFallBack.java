package com.yuosef.accounts.Service.client;

import com.yuosef.accounts.Dtos.CardsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallBack implements CardsFeignClient{

    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
        return null;
    }

}
