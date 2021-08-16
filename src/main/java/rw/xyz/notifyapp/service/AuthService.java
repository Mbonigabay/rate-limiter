package rw.xyz.notifyapp.service;

import rw.xyz.notifyapp.model.PricingPlan;
import rw.xyz.notifyapp.model.User;
import rw.xyz.notifyapp.model.request.SignUpRequest;

public interface AuthService {
    User getAuth();
    User signUpUser(SignUpRequest signUpRequest);
    User changeSubscription(PricingPlan pricingPlan);
}
