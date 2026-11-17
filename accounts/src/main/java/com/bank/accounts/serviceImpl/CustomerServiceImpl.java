package com.bank.accounts.serviceImpl;

import com.bank.accounts.dto.request.CustomerCreationRequestDTO;
import com.bank.accounts.dto.request.UpdateCustomerDetailsRequestDTO;
import com.bank.accounts.dto.response.AuditResponse;
import com.bank.accounts.dto.response.CustomerResponseDTO;
import com.bank.accounts.dto.response.ResponseDTO;
import com.bank.accounts.entity.Customer;
import com.bank.accounts.repository.AccountsRepository;
import com.bank.accounts.repository.CustomerRepository;
import com.bank.accounts.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    @Transactional
    public ResponseDTO createCustomer(CustomerCreationRequestDTO request) {
        try {
            if (request == null) {
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Customer request body cannot be null.", HttpStatus.BAD_REQUEST.toString());
            }

            // 1. Verify accountId exists in the accounts table
            if (!accountsRepository.existsById(request.getAccountId())) {
                return new ResponseDTO(
                        HttpStatus.NOT_FOUND.value(),
                        "Account not found with ID: " + request.getAccountId(),
                        HttpStatus.NOT_FOUND.toString()
                );
            }

            String email = request.getEmail().trim();
            String mobileNumber = request.getMobileNumber().trim();

            // 2. Prevent duplicate customer email or mobile number
            if (customerRepository.existsByEmailIgnoreCase(email)) {
                return new ResponseDTO(
                        HttpStatus.CONFLICT.value(),
                        "Customer already registered with email: " + email,
                        HttpStatus.CONFLICT.toString()
                );
            }
            if (customerRepository.existsByMobileNumber(mobileNumber)) {
                return new ResponseDTO(
                        HttpStatus.CONFLICT.value(),
                        "Customer already registered with mobile number: " + mobileNumber,
                        HttpStatus.CONFLICT.toString()
                );
            }

            // 3. Build entity and generate unique 10-digit account number
            Customer customer = new Customer();
            customer.setAccountId(request.getAccountId());
            customer.setAccountNumber(generateUniqueAccountNumber());
            customer.setName(request.getName().trim());
            customer.setEmail(email);
            customer.setMobileNumber(mobileNumber);

            // Required because Audit columns are nullable = false
            /*customer.setCreatedAt(LocalDateTime.now());
            customer.setCreatedBy("SYSTEM");*/

            Customer savedCustomer = customerRepository.save(customer);

            return new ResponseDTO(
                    HttpStatus.CREATED.value(),
                    mapToCustomerResponseDTO(savedCustomer),
                    "Customer created successfully."
            );
        } catch (Exception e) {
            return new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getCustomerDetails(Long customerId) {
        try {
            if (customerId != null) {
                Optional<Customer> customerOpt = customerRepository.findById(customerId);
                if (customerOpt.isEmpty()) {
                    return new ResponseDTO(
                            HttpStatus.NOT_FOUND.value(),
                            "Customer not found with ID: " + customerId,
                            HttpStatus.NOT_FOUND.toString()
                    );
                }
                return new ResponseDTO(
                        HttpStatus.OK.value(),
                        mapToCustomerResponseDTO(customerOpt.get()),
                        "Customer details fetched successfully."
                );
            }

            List<CustomerResponseDTO> customers = customerRepository.findAll()
                    .stream()
                    .map(this::mapToCustomerResponseDTO)
                    .collect(Collectors.toList());

            return new ResponseDTO(
                    HttpStatus.OK.value(),
                    customers,
                    "All customers fetched successfully."
            );
        } catch (Exception e) {
            return new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString()
            );
        }
    }

    @Override
    @Transactional
    public ResponseDTO updateCustomerDetails(UpdateCustomerDetailsRequestDTO request) {
        try {
            if (request == null || request.getCustomerId() == null) {
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Customer ID is required for update.", HttpStatus.BAD_REQUEST.toString());
            }

            Optional<Customer> customerOpt = customerRepository.findById(request.getCustomerId());
            if (customerOpt.isEmpty()) {
                return new ResponseDTO(
                        HttpStatus.NOT_FOUND.value(),
                        "Customer not found with ID: " + request.getCustomerId(),
                        HttpStatus.NOT_FOUND.toString()
                );
            }

            Customer existingCustomer = customerOpt.get();

            // If accountId is being updated, verify the new accountId exists in accounts table
            if (request.getAccountId() != null) {
                if (!accountsRepository.existsById(request.getAccountId())) {
                    return new ResponseDTO(
                            HttpStatus.NOT_FOUND.value(),
                            "Account not found with ID: " + request.getAccountId(),
                            HttpStatus.NOT_FOUND.toString()
                    );
                }
                existingCustomer.setAccountId(request.getAccountId());
            }

            if (request.getName() != null && !request.getName().isBlank()) {
                existingCustomer.setName(request.getName().trim());
            }

            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                String newEmail = request.getEmail().trim();
                if (!newEmail.equalsIgnoreCase(existingCustomer.getEmail())
                        && customerRepository.existsByEmailIgnoreCase(newEmail)) {
                    return new ResponseDTO(
                            HttpStatus.CONFLICT.value(),
                            "Another customer is already registered with email: " + newEmail,
                            HttpStatus.CONFLICT.toString()
                    );
                }
                existingCustomer.setEmail(newEmail);
            }

            if (request.getMobileNumber() != null && !request.getMobileNumber().isBlank()) {
                String newMobile = request.getMobileNumber().trim();
                if (!newMobile.equals(existingCustomer.getMobileNumber())
                        && customerRepository.existsByMobileNumber(newMobile)) {
                    return new ResponseDTO(
                            HttpStatus.CONFLICT.value(),
                            "Another customer is already registered with mobile number: " + newMobile,
                            HttpStatus.CONFLICT.toString()
                    );
                }
                existingCustomer.setMobileNumber(newMobile);
            }

            /*existingCustomer.setUpdatedAt(LocalDateTime.now());
            existingCustomer.setUpdatedBy("SYSTEM");*/

            Customer updatedCustomer = customerRepository.save(existingCustomer);

            return new ResponseDTO(
                    HttpStatus.OK.value(),
                    mapToCustomerResponseDTO(updatedCustomer),
                    "Customer details updated successfully."
            );
        } catch (Exception e) {
            return new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString()
            );
        }
    }

    @Override
    @Transactional
    public ResponseDTO deleteCustomer(Long customerId) {
        try {
            if (!customerRepository.existsById(customerId)) {
                return new ResponseDTO(
                        HttpStatus.NOT_FOUND.value(),
                        "Customer not found with ID: " + customerId,
                        HttpStatus.NOT_FOUND.toString()
                );
            }

            customerRepository.deleteById(customerId);
            return new ResponseDTO(
                    HttpStatus.OK.value(),
                    "Customer deleted successfully.",
                    HttpStatus.OK.toString()
            );
        } catch (Exception e) {
            return new ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString()
            );
        }
    }

    // Helper method: generates a unique 10-digit account number
    private Long generateUniqueAccountNumber() {
        long accountNumber;
        do {
            accountNumber = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        } while (customerRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    // Helper method: maps Customer entity to CustomerResponseDTO
    private CustomerResponseDTO mapToCustomerResponseDTO(Customer customer) {
        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setCreatedAt(customer.getCreatedAt());
        auditResponse.setCreatedBy(customer.getCreatedBy());
        auditResponse.setUpdatedAt(customer.getUpdatedAt());
        auditResponse.setUpdatedBy(customer.getUpdatedBy());

        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setAccountId(customer.getAccountId());
        dto.setAccountNumber(customer.getAccountNumber());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setMobileNumber(customer.getMobileNumber());
        dto.setAuditResponse(auditResponse);

        return dto;
    }
}