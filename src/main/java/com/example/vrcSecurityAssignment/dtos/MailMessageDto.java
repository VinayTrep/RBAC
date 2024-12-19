package com.example.vrcSecurityAssignment.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailMessageDto {
    private String from;
    private String to;
    private String subject;
    private String body;
}
