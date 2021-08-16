package rw.xyz.notifyapp.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import rw.xyz.notifyapp.model.User;

public interface RateLimiterService {
    Bucket resolveBucket(User user);

    Bucket newBucket(User user);

    Bucket bucket(Bandwidth limit);

    User getSubscriptionByUser(String email);

    boolean countSessions(String email);

    Bucket resolveGlobalBucket();
}
