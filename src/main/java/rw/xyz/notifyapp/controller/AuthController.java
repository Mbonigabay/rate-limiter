package rw.xyz.notifyapp.controller;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import rw.xyz.notifyapp.configuration.JWTConfig;
import rw.xyz.notifyapp.model.PricingPlan;
import rw.xyz.notifyapp.model.User;
import rw.xyz.notifyapp.model.request.AuthenticationRequest;
import rw.xyz.notifyapp.model.request.SignUpRequest;
import rw.xyz.notifyapp.service.AuthService;
import rw.xyz.notifyapp.service.MyUserDetailsService;
import rw.xyz.notifyapp.util.ApiResponse;

@RestController
@Slf4j
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTConfig jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    AuthService authService;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<String>> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest, Authentication authentication,
            HttpSession session) throws Exception {

        log.info("{}", "authenticationRequest");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
            log.info("{}", "Success");
        } catch (BadCredentialsException ex) {
            throw new AuthException(ex.getMessage());
        } catch (LockedException ex) {
            throw new AuthException(ex.getMessage());
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        final String message = "Users logged in successfully...";

        ApiResponse<String> body = new ApiResponse<>(HttpStatus.OK, message, jwt);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody SignUpRequest signUpRequest) throws Exception {
       
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new Exception("Password don't match");
        }

        User user = authService.signUpUser(signUpRequest);


        final String message = "User created successfully..";
        ApiResponse<User> body = new ApiResponse<>(HttpStatus.CREATED, message, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);

    }

    @GetMapping("/subscription/{pricingPlan}")
    public ResponseEntity<ApiResponse<User>> changeSubscription(@PathVariable("pricingPlan") PricingPlan pricingPlan){

        User user = authService.changeSubscription(pricingPlan);


        final String message = "User subscription changed successfully..";
        ApiResponse<User> body = new ApiResponse<>(HttpStatus.CREATED, message, user);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}