package com.yuosef.accounts.Service.Impls;

import com.yuosef.accounts.Daos.AccountsRepository;
import com.yuosef.accounts.Daos.CustomerRepository;
import com.yuosef.accounts.Dtos.AccountDto;
import com.yuosef.accounts.Dtos.CustomerDto;
import com.yuosef.accounts.Exceptions.CustomerAlreadyExistsException;
import com.yuosef.accounts.Exceptions.ResourceNotValidException;
import com.yuosef.accounts.Mapper.AccountMapper;
import com.yuosef.accounts.Mapper.CustomerMapper;
import com.yuosef.accounts.Models.Accounts;
import com.yuosef.accounts.Models.Customer;
import com.yuosef.accounts.Service.IAccountsService;
import com.yuosef.accounts.constants.AccountsConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountsService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;


    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        Optional<Customer> optionalCustomer=customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("this mobileNumber already in use: "+customerDto.getMobileNumber());
        }
        Customer savedCustomer=customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));

    }



    /**
     *
     * @param customer
     *
     * @return new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber=0;
        while (true) {
             randomAccNumber = 1000000000L + new Random().nextInt(900000000);
            Optional<Accounts> optionalAccount = accountsRepository.findByAccountNumber(randomAccNumber);
            if (optionalAccount.isEmpty()) {
                break;
            }
        }
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

    /**
     * fetch account details by mobile number
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
           Customer customer= customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotValidException("Customer not found", "mobileNumber", mobileNumber));
           Accounts account= accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotValidException("Account not found", "customerId", customer.getCustomerId().toString()));
           CustomerDto customerDto= CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
           customerDto.setAccountsDto(AccountMapper.mapToAccountsDto(account,new AccountDto()));
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountDto  accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotValidException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotValidException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotValidException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

}
