package com.example.e_commerce.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.e_commerce.controller.dao.SignUpRequest;
import com.example.e_commerce.exception.CustomException;
import com.example.e_commerce.model.Customer;
import com.example.e_commerce.model.Role;
import com.example.e_commerce.repository.CustomerRepository;
import com.example.e_commerce.repository.RoleRepository;
import com.example.e_commerce.security.TokenUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SignUpService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenUtil tokenUtil;

    /******************************************************************************************************************/

    public void saveUser(SignUpRequest request) throws MessagingException, IOException, SQLException {
        try {

            Customer customer = Customer.builder().email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName()).active(false).build();
            customerRepository.save(customer);
            Role role = roleRepository.findByRole("ROLE_USER").orElse(null);
            if (role == null) {
                role = new Role();
                role.setRole("ROLE_USER");
                roleRepository.save(role);
            }
            customer.setRoles(List.of(role));
            customerRepository.save(customer);

        } catch (Exception e) {

            throw new CustomException("Error while saving user", HttpStatus.BAD_REQUEST);
        }

    }

    // /******************************************************************************************************************/

    public ResponseEntity<?> verifyEmail(String token, HttpServletResponse response) throws SQLException, IOException {
        
            if (tokenUtil.isTokenExpired(token)) {
                throw new CustomException("Token is expired", HttpStatus.BAD_REQUEST);
            }
       

        Customer customer = customerRepository.findByEmail(tokenUtil.getUserName(token)).orElseThrow(
                () -> new CustomException("User not found!", HttpStatus.NOT_FOUND));

        customer.setActive(true);
        customerRepository.save(customer);

        return ResponseEntity.ok("Account is verified, you can login now!");
    }

    /******************************************************************************************************************/
    public ResponseEntity<?> sendRegistrationVerificationCode(String email, HttpServletRequest request,
            String verficationToken) {
        try {
            String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
                    + "/api/verifyEmail/" + verficationToken;

            System.out.println("url : " + url);
            String subject = "Email Verification";
            String senderName = "User Registration Portal Service";
            String content = "<p> Hi, " + email + ", </p>" +
                    "<p>Thank you for registering with us," + "" +
                    "Please, follow the link below to complete your registration.</p>" +
                    "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                    "<p> Thank you <br> Users Registration Portal Service";
            emailService.sendEmail(email, content, subject, senderName);

            return ResponseEntity.ok("Account is not verfied ,please check your email to verify it!");
        } catch (Exception e) {
            throw new CustomException("Error while sending Email", HttpStatus.BAD_REQUEST);
        }
    }

    /**************************************************************************************************************/
    // public User registerOuth2(OAuth2UserDetails oAuth2UserDetails) throws
    // IOException, SerialException, SQLException {
    // User user = new User();

    // user.setEmail(oAuth2UserDetails.getEmail());
    // user.setFirstName(oAuth2UserDetails.getFirstName());
    // user.setLastName(oAuth2UserDetails.getLastName());
    // user.setEnabled(true);
    // Role role = roleRepository.findByRole("ROLE_USER").orElse(null);
    // user.setRoles(List.of(role));
    // user.setLastLogin(LocalDateTime.now());
    // user.setPassword(passwordEncoder.encode("password@M.reda.49"));
    // user.setRegistrationDate(LocalDateTime.now());
    // // user.setProfilePicture(downloadImage(oAuth2UserDetails.getPicture()));

    // return user;
    // }

    /******************************************************************************************************************/
    // public byte[] downloadImage(String imageUrl) throws IOException,
    // SerialException, SQLException {
    // RestTemplate restTemplate = new RestTemplate();

    // // Make a request to the image URL
    // ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl,
    // byte[].class);

    // return response.getBody();

    // }
}

/******************************************************************************************************************/
