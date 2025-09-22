package com.yuosef.accounts.Service;

import com.yuosef.accounts.Dtos.CustomerDetailsDto;

public interface ICustomerService {

    /**
     *
     * @param mobileNumber
     * @return
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);

}
