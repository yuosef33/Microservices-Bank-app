package com.yuosef.accounts.Service;

import com.yuosef.accounts.Dtos.CustomerDto;

public interface IAccountsService {

    //create customer @param customerDto
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber
     * @return
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto
     * @return
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param accountNumber
     * @return
     */
    boolean deleteAccount(String mobileNumber);
}
