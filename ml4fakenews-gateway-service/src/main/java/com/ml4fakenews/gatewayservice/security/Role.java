package com.ml4fakenews.gatewayservice.security;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class Role {
    private Integer id;
    private String rolename;
    private Set<User> users;
}
