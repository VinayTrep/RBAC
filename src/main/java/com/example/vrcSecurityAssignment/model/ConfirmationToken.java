package com.example.vrcSecurityAssignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ConfirmationToken extends BaseModel{

    @Column(name="confirmation_token")
    private String confirmationToken;
    private String email;

}
