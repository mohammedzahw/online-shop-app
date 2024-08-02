package com.example.e_commerce.service;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.e_commerce.controller.dao.LoginRequest;
import com.example.e_commerce.exception.CustomException;
import com.example.e_commerce.model.Customer;
import com.example.e_commerce.repository.CustomerRepository;
import com.example.e_commerce.security.TokenUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class LoginService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private SignUpService signUpService;
    @Autowired
    private EmailService emailService;

    /********************************************************************************* */
    public ResponseEntity<?> verifyLogin(LoginRequest loginRequest, HttpServletRequest request)
            throws SQLException, IOException {

        Customer customer = customerRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException("User not found!", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
            throw new CustomException("Wrong Password!", HttpStatus.BAD_REQUEST);
        }
        if (!customer.getActive()) {
            String token = tokenUtil.generateToken(loginRequest.getEmail(), 1000, 1000);
            return signUpService.sendRegistrationVerificationCode(loginRequest.getEmail(),
                    request,
                    token);
        }
        String token = tokenUtil.generateToken(loginRequest.getEmail(), customer.getId(), 3000000);
        customerRepository.save(customer);
        return ResponseEntity.ok(token);

    }

    /********************************************************************************************************************/
    public ResponseEntity<?> savePassword(String email, String password)
            throws SQLException, IOException, MessagingException {
        Customer customer = customerRepository.findByEmail(email).orElseThrow(
                () -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        customer.setPassword(passwordEncoder.encode(password)); // encoded password);
        customerRepository.save(customer);
        return new ResponseEntity<>("Password Changed Successfully ! , Now you can login", HttpStatus.OK);

    }

    /********************************************************************************************************************/

    public ResponseEntity<?> sendResetpasswordEmail(String email, HttpServletRequest request, String token) {

        try {
            String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
                    + "/api/check-token/" + token;
            System.out.println("url : " + url);
            String subject = "Reset Password Verification";
            String senderName = "User Registration Portal Service";
            String content = "<p> Hi, " + email + ", </p>" +
                    "<p>Thank you for registering with us," + "" +
                    "Please, follow the link below to complete your registration.</p>" +
                    "<a href=\"" + url + "\">Reset password</a>" +
                    "<p> Thank you <br> Reset Password Portal Service";
            emailService.sendEmail(email, content, subject, senderName);

            return new ResponseEntity<>("Please, check your email to reset your password", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    /********************************************************************************************************************/

}
