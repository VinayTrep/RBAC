package com.example.vrcSecurityAssignment.model;

import com.example.vrcSecurityAssignment.model.constants.RoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role extends BaseModel{
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<User> users=new HashSet<>();
}
