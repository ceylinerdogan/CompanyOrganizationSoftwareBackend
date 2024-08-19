package com.ceylin.companyorganizationSoftware.Service;


import com.ceylin.companyorganizationSoftware.Repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;


}