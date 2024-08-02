// package com.example.e_commerce.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.stereotype.Component;

// import com.example.e_commerce.customer.CustomerRepository;

// @Component
// public class CustomerUserDetailsService implements UserDetailsService {
//     @Autowired
//     private CustomerRepository customerRepository;

//     @Override
//     public UserDetails loadUserByUsername(String username) {
//         return customerRepository.findByEmail(username)
//                 .map(CustomerUserDetails::new)
//                 .orElseThrow(() -> new RuntimeException("Customer not found"));
//     }

// }
