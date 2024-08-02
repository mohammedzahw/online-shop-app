
package com.example.e_commerce.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_commerce.controller.dao.SignUpRequest;
import com.example.e_commerce.model.Customer;
import com.example.e_commerce.repository.CustomerRepository;
import com.example.e_commerce.security.TokenUtil;
import com.example.e_commerce.service.SignUpService;
import com.example.e_commerce.service.Validator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("api/")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TokenUtil tokenUtil;

    /******************************************************************************************************************/

    /******************************************************************************************************************/

    @PostMapping(value = "/signup")

    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, BindingResult result,
            HttpServletRequest request) throws MessagingException, IOException, SQLException {
        // return new Response(HttpStatus.OK, "ok", signUpRequest.getEmail());
          if (result.hasErrors()) {
            return Validator.validate(result);
        }
            Customer customer = customerRepository.findByEmail(signUpRequest.getEmail()).orElse(null);
            if (customer != null) {
                return new ResponseEntity<>("Email already exists , Please login", HttpStatus.BAD_REQUEST);
            }

            signUpService.saveUser(signUpRequest);

            String token = tokenUtil.generateToken(signUpRequest.getEmail(), 1000, 1000);

            return signUpService.sendRegistrationVerificationCode(signUpRequest.getEmail(), request,
                    token);
        }
    

    /******************************************************************************************************************/

    @GetMapping("/verifyEmail/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("token") String verficationToken,
            HttpServletResponse response)
            throws SQLException, IOException {

        return signUpService.verifyEmail(verficationToken, response);
    }

}