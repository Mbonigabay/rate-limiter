package rw.xyz.notifyapp.model.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthenticationRequest implements Serializable {
    private String email;
    private String password;
}