package com.ceylin.companyorganizationSoftware.Service;


import com.ceylin.companyorganizationSoftware.Dto.Request.AddUserRequest;
import com.ceylin.companyorganizationSoftware.Dto.Response.Response;
import com.ceylin.companyorganizationSoftware.Dto.UserDto;
import com.ceylin.companyorganizationSoftware.Model.Company;
import com.ceylin.companyorganizationSoftware.Model.Department;
import com.ceylin.companyorganizationSoftware.Model.User;
import com.ceylin.companyorganizationSoftware.Model.UserRole;
import com.ceylin.companyorganizationSoftware.Repository.CompanyRepository;
import com.ceylin.companyorganizationSoftware.Repository.DepartmentRepository;
import com.ceylin.companyorganizationSoftware.Repository.UserRepository;

import com.ceylin.companyorganizationSoftware.Repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final DepartmentRepository departmentRepository;

    private final CompanyRepository companyRepository;


    public ResponseEntity<UserDto> addUser(AddUserRequest addUserRequest) {

        UserRole userRole = userRoleRepository.findByName(addUserRequest.getRole());
        Department department = departmentRepository.findByName(addUserRequest.getDepartment());
        Company company = companyRepository.findByName((addUserRequest.getCompany()));

        User newUser = User.builder()
                .userRole(userRole)
                .firstName(addUserRequest.getFirstName())
                .lastName(addUserRequest.getLastName())
                .email(addUserRequest.getEmail())
                .department(department)
                .company(company)
                .password(null)
                .isEnabled(false)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(getUserById(newUser.getId()).getBody());
    }

    public ResponseEntity<UserDto> getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserDto userDto = toUserDto(user.get());
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    private UserDto toUserDto (User user){

        return UserDto.builder()
                .id(user.getId())
                .role(user.getUserRole() != null ? user.getUserRole().getName() : null)
                .department(user.getDepartment() != null ? user.getDepartment().getName() : null)
                .company(user.getCompany() != null ? user.getCompany().getName() : null)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}