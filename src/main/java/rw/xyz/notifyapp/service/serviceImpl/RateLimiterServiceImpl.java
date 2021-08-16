package rw.xyz.notifyapp.service.serviceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import rw.xyz.notifyapp.model.PricingPlan;
import rw.xyz.notifyapp.model.User;
import rw.xyz.notifyapp.repository.UserRepository;
import rw.xyz.notifyapp.service.RateLimiterService;

@Service
@Slf4j
public class RateLimiterServiceImpl implements RateLimiterService {

    @Autowired
    private UserRepository userRepository;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private final double MAX_SYSTEM_REQUEST = Math.pow(10, 3);

    @Override
    public Bucket resolveBucket(User user) {
        return cache.computeIfAbsent(user.getEmail(), k -> this.newBucket(user));
    }

    @Override
    public Bucket newBucket(User user) {
        PricingPlan pricingPlan = PricingPlan.resolvePlan(user);
        return bucket(pricingPlan.getLimit());
    }

    @Override
    public Bucket bucket(Bandwidth limit) {
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public User getSubscriptionByUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    @Override
    public boolean countSessions(String email) {
        User user = getSubscriptionByUser(email);
        if (user.getSubscriptionEndDate().compareTo(LocalDate.now()) > 0) {
            if (user.getRequest() >= user.getRequestPerMonth()) {
                return false;
            } else {
                user.setRequest(user.getRequest() + 1);
                userRepository.save(user);
                log.info("{} remains in the month frame", user.getRequestPerMonth() - user.getRequest());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public Bucket resolveGlobalBucket() {
        Bandwidth limit = Bandwidth.classic((long) MAX_SYSTEM_REQUEST, Refill.intervally((long) MAX_SYSTEM_REQUEST, Duration.ofSeconds(30)));
        return cache.computeIfAbsent("system", k -> this.bucket(limit));
    }

}
