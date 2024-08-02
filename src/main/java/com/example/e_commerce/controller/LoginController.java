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

import com.example.e_commerce.controller.dao.ChangePasswordRequest;
import com.example.e_commerce.controller.dao.LoginRequest;
import com.example.e_commerce.exception.CustomException;
import com.example.e_commerce.model.Customer;
import com.example.e_commerce.repository.CustomerRepository;
import com.example.e_commerce.security.TokenUtil;
import com.example.e_commerce.service.LoginService;
import com.example.e_commerce.service.SignUpService;
import com.example.e_commerce.service.Validator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class LoginController {

    @Autowired
    private SignUpService signUpService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private LoginService loginService;

    /***************************************************************************************************************/
    @PostMapping("/login/custom")
    public ResponseEntity<?> loginCustom(@RequestBody @Valid LoginRequest loginRequest, BindingResult result,
            HttpServletRequest request)
            throws MessagingException, SQLException, IOException {
                if (result.hasErrors()) {
                    return Validator.validate(result);
                }
        return loginService.verifyLogin(loginRequest, request);

    }

    /****************************************************************************************************************/

    @PostMapping("/forget-password")
    public ResponseEntity<?> enterEmail(@RequestBody @Valid ChangePasswordRequest changePasswordRequest,
            BindingResult result,
            HttpServletRequest request)
            throws MessagingException, SQLException, IOException {

        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        Customer customer = customerRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new CustomException("User Not Found !", HttpStatus.NOT_FOUND));
        if (!customer.getActive()) {
            return signUpService.sendRegistrationVerificationCode(changePasswordRequest.getEmail(), request,
                    tokenUtil.generateToken(changePasswordRequest.getEmail(), customer.getId(), 900));
        }
        return loginService.sendResetpasswordEmail(changePasswordRequest.getEmail(), request,
                tokenUtil.generateToken(changePasswordRequest.getEmail() + "," + changePasswordRequest.getPassword(),
                        customer.getId(), 900));
    }

    /*************************************************************************************************************/
    @SuppressWarnings("unused")
    @GetMapping("/check-token/{token}")
    public ResponseEntity<?> savePassword(@PathVariable("token") String token, HttpServletResponse response)
            throws SQLException, IOException, MessagingException {

        String email = tokenUtil.getUserName(token).split(",")[0];

        String password = tokenUtil.getUserName(token).split(",")[1];

        Customer customer = customerRepository.findByEmail(email).orElseThrow(
                () -> new CustomException("User Not Found !", HttpStatus.NOT_FOUND));

        if (tokenUtil.isTokenExpired(token)) {
            // response.sendRedirect("https://localhost:8080//reset-password/?token=invalid");
            throw new CustomException("Token is expired", HttpStatus.BAD_REQUEST);
        }
        return loginService.savePassword(email, password);
    }
}
