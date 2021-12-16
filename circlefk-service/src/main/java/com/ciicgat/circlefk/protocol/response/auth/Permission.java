package com.ciicgat.circlefk.protocol.response.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    private Set<String> menus = new HashSet<>();

    private Map<String, Set<String>> rights = new HashMap<>();
}
