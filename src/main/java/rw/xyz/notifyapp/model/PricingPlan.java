package rw.xyz.notifyapp.model;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

public enum PricingPlan {
    FREE(3),

    BASIC(40),

    PROFESSIONAL(100);

    private int bucketCapacity;

    private PricingPlan(int bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
    }

    public Bandwidth getLimit() {
        return Bandwidth.classic(bucketCapacity, Refill.intervally(bucketCapacity, Duration.ofSeconds(30)));
    }

    public int bucketCapacity() {
        return bucketCapacity;
    }

    public static PricingPlan resolvePlan(User user) {
        return user.getPricingPlan();
    }
}
