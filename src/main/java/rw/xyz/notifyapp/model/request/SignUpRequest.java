package rw.xyz.notifyapp.model.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String confirmPassword;
}