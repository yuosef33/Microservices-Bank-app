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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomersServiceImpl implements ICustomerService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    public CustomersServiceImpl(
            AccountsRepository accountsRepository,
            CustomerRepository customerRepository,
            CardsFeignClient cardsFeignClient,
            LoansFeignClient loansFeignClient) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
        this.cardsFeignClient = cardsFeignClient;
        this.loansFeignClient = loansFeignClient;
    }


    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId) {
        Customer customer= customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotValidException("Customer not found", "mobileNumber", mobileNumber));

        Accounts account= accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotValidException("Account not found", "customerId", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto= CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountMapper.mapToAccountsDto(account, new AccountDto()));
//        ResponseEntity<LoansDto> loansDtoResponseEntity;
//        ResponseEntity<CardsDto> cardsDtoResponseEntity;
//        try {
//          loansDtoResponseEntity = loansFeignClient.fetchloanDetails(correlationId,mobileNumber);
//        }catch (Exception e){
//            loansDtoResponseEntity=null;
//            }
//            if(loansDtoResponseEntity!=null) {
//                customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
//            }else{
//                customerDetailsDto.setLoansDto(null);
//            }

        //        try {
//            cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId,mobileNumber);
//        }catch (Exception e){
//            cardsDtoResponseEntity=null;
//        }
//        if(cardsDtoResponseEntity!=null) {
//            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
//        }else {
//            customerDetailsDto.setCardsDto(null);
//        }

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchloanDetails(correlationId, mobileNumber);
        if(null != loansDtoResponseEntity) {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if(null != cardsDtoResponseEntity) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }



        return customerDetailsDto;
    }
}
