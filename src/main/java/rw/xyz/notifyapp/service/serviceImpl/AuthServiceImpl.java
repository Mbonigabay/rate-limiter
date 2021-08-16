package rw.xyz.notifyapp.service.serviceImpl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import rw.xyz.notifyapp.configuration.SecurityConfiguration;
import rw.xyz.notifyapp.model.MyUserDetails;
import rw.xyz.notifyapp.model.PricingPlan;
import rw.xyz.notifyapp.model.User;
import rw.xyz.notifyapp.model.request.SignUpRequest;
import rw.xyz.notifyapp.repository.UserRepository;
import rw.xyz.notifyapp.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityConfiguration security;

    @Override
    public User getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return user;
    }

    @Override
    public User signUpUser(SignUpRequest signUpRequest) {
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(u -> new Exception("User email is already registered"));
                
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setActive(true);
        user.setPassword(security.getPasswordEncoder().encode(signUpRequest.getPassword()));
        user.setPricingPlan(PricingPlan.FREE);
        user.setRequestPerMonth(PricingPlan.FREE.bucketCapacity() * (int) Math.pow(10, 3));
        user.setRequest(0);

        User createdUser = userRepository.save(user);
        userRepository.save(createdUser);
        return createdUser;
    }

    @Override
    public User changeSubscription(PricingPlan pricingPlan) {
        User user = this.getAuth();
        user.setPricingPlan(pricingPlan);
        user.setRequestPerMonth(pricingPlan.bucketCapacity() * (int) Math.pow(10, 3));
        user.setRequest(0);
        user.setSubscriptionStartDate(LocalDate.now());
        user.setSubscriptionEndDate(LocalDate.now().plusDays(30));
        
        User createdUser = userRepository.save(user);
        userRepository.save(createdUser);
        return createdUser;
    }
    
}
