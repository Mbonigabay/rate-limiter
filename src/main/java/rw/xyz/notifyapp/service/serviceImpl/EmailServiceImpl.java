package rw.xyz.notifyapp.service.serviceImpl;

import org.springframework.stereotype.Service;

import rw.xyz.notifyapp.model.Email;
import rw.xyz.notifyapp.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public Email sendEmail(Email email) {
        return email;
    }
    
}
