package rw.xyz.notifyapp.controller;

import com.google.common.util.concurrent.RateLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rw.xyz.notifyapp.exception.TooManyRequestException;
import rw.xyz.notifyapp.model.Email;
import rw.xyz.notifyapp.service.AuthService;
import rw.xyz.notifyapp.service.EmailService;
import rw.xyz.notifyapp.service.RateLimiterService;
import rw.xyz.notifyapp.util.ApiResponse;

@RestController
@Slf4j
@RequestMapping("api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    EmailService emailService;

    @Autowired
    AuthService authService;

    @Autowired
    RateLimiterService rateLimiterService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Email>> sendEmail() {
        Bucket bucket = rateLimiterService.resolveBucket(authService.getAuth());
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        log.info("{} remains in the 30 seconds frame", probe.getRemainingTokens());

        if (!probe.isConsumed()) {
            throw new TooManyRequestException("You have exceeded "
                    + authService.getAuth().getPricingPlan().bucketCapacity() + " requests on your "
                    + authService.getAuth().getPricingPlan().name() + " plan, consider upgrading your plan.");

        }

        if (!rateLimiterService.countSessions(authService.getAuth().getEmail())) {
            throw new TooManyRequestException("You have exceeded "
                    + authService.getAuth().getPricingPlan().bucketCapacity() + " requests on your "
                    + authService.getAuth().getPricingPlan().name() + " plan, consider upgrading your plan.");
        }

        Email email = emailService.sendEmail(new Email("to", "from", "subject", "content"));

        final String message = "Email sent successfully..";
        ApiResponse<Email> body = new ApiResponse<>(HttpStatus.CREATED, message, email);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

}
