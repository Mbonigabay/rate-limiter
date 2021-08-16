package rw.xyz.notifyapp.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 32)
    private String email;
    @Enumerated(EnumType.STRING)
    private PricingPlan pricingPlan;
    @Column(nullable = false)
    private String password;
    private Boolean active;
    private LocalDate createDate = LocalDate.now();
    private int requestPerMonth;
    private LocalDate subscriptionStartDate = LocalDate.now();
    private LocalDate subscriptionEndDate = LocalDate.now().plusDays(30);
    private int request;
}
