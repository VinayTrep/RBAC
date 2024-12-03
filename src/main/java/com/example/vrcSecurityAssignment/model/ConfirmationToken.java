package com.example.vrcSecurityAssignment.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ConfirmationToken extends BaseModel{

    @Column(name="confirmation_token")
    private String confirmationToken;
    private String email;

}
