package com.yuosef.accounts.Service.Impls;

import com.yuosef.accounts.Daos.AccountsRepository;
import com.yuosef.accounts.Daos.CustomerRepository;
import com.yuosef.accounts.Dtos.AccountDto;
import com.yuosef.accounts.Dtos.CardsDto;
import com.yuosef.accounts.Dtos.CustomerDetailsDto;
import com.yuosef.accounts.Dtos.LoansDto;
import com.yuosef.accounts.Exceptions.ResourceNotValidException;
import com.yuosef.accounts.Mapper.AccountMapper;
import com.yuosef.accounts.Mapper.CustomerMapper;
import com.yuosef.accounts.Models.Accounts;
import com.yuosef.accounts.Models.Customer;
import com.yuosef.accounts.Service.ICustomerService;
import com.yuosef.accounts.Service.client.CardsFeignClient;
import com.yuosef.accounts.Service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomerService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;


    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer= customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotValidException("Customer not found", "mobileNumber", mobileNumber));

        Accounts account= accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotValidException("Account not found", "customerId", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto= CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountMapper.mapToAccountsDto(account, new AccountDto()));
        ResponseEntity<LoansDto> loansDtoResponseEntity;
        ResponseEntity<CardsDto> cardsDtoResponseEntity;
        try {
          loansDtoResponseEntity = loansFeignClient.fetchloanDetails(mobileNumber);
        }catch (Exception e){
            loansDtoResponseEntity=null;
            }
            if(loansDtoResponseEntity!=null) {
                customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
            }else{
                customerDetailsDto.setLoansDto(null);
            }

        try {
            cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        }catch (Exception e){
            cardsDtoResponseEntity=null;
        }
        if(cardsDtoResponseEntity!=null) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }else {
            customerDetailsDto.setCardsDto(null);
        }


        return customerDetailsDto;
    }
}
