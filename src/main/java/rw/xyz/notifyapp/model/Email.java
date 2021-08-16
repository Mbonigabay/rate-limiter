package rw.xyz.notifyapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Email {
    private String to;
    private String from;
    private String subject;
    private String content;


}
