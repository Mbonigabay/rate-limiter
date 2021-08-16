package rw.xyz.notifyapp.service;

import rw.xyz.notifyapp.model.Email;

public interface EmailService {
    Email sendEmail(Email email);
}
