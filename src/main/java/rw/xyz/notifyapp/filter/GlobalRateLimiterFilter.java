package rw.xyz.notifyapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.extern.slf4j.Slf4j;
import rw.xyz.notifyapp.exception.TooManyRequestException;
import rw.xyz.notifyapp.service.RateLimiterService;

@Component
@Slf4j
public class GlobalRateLimiterFilter implements Filter {

    @Autowired
    RateLimiterService rateLimiterService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Bucket bucket = rateLimiterService.resolveGlobalBucket();
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        log.info("{} remains in the global bucket per seconds frame", probe.getRemainingTokens());

        if (!probe.isConsumed()) {
            throw new TooManyRequestException("Too many request per seconds");
        }

        chain.doFilter(request, response);

    }

}
