package com.ceylin.companyorganizationSoftware.Service;


import com.ceylin.companyorganizationSoftware.Controller.UserController;
import com.ceylin.companyorganizationSoftware.Dto.Request.AddUserRequest;
import com.ceylin.companyorganizationSoftware.Dto.Request.UpdateUserRequest;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final DepartmentRepository departmentRepository;

    private final CompanyRepository companyRepository;
    private final EmailService emailService;




    public ResponseEntity<UserDto> addUser(User currentUser,AddUserRequest addUserRequest) {
        Department dep;

        if (currentUser.getUserRole().getName().equals("MANAGER")) {
            dep = currentUser.getDepartment(); // Manager can only add users to their own department
        } else if (currentUser.getUserRole().getName().equals("ADMIN")) {
            dep = departmentRepository.findByName(addUserRequest.getDepartment()); // Admin can choose any department
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Forbidden for non-admin, non-manager users
        }

        UserRole userRole = userRoleRepository.findByName(addUserRequest.getRole());
        Company company = companyRepository.findByName((addUserRequest.getCompany()));

        User newUser = User.builder()
                .userRole(userRole)
                .firstName(addUserRequest.getFirstName())
                .lastName(addUserRequest.getLastName())
                .email(addUserRequest.getEmail())
                .department(dep)
                .company(company)
                .password(null)
                .isEnabled(false)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        // Send an informative email
        String subject = "Sisteme kullanıcı olarak eklendiniz. Lütfen hesabınızı aktifleştirin.";
        String frontendUrl = "https://company-organization-software-coral.vercel.app/activateuser";
        String body = "Merhaba " + newUser.getFirstName() + ",\n\n" +
                "Company Organization Software Internship Uygulamasına kullanıcı olarak eklendiniz. Lütfen hesabınızı aktifleştirin.\n\n" +
                "Hesabınızı aktifleştirmek için lütfen aşağıdaki linke tıklayın ve e-posta adresinizi girin:\n" +
                frontendUrl + "\n\n" +
                "Teşekkürler.";

        emailService.sendEmail(newUser.getEmail(), subject, body);
        return ResponseEntity.status(HttpStatus.OK).body(getUserById(newUser.getId()).getBody());
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUserDto);
    }

    public Page<UserDto> getUsersInDepartment(User manager, Pageable pageable) {
        return userRepository.findByDepartment(manager.getDepartment(), pageable).map(this::toUserDto);
    }

    public ResponseEntity<UserDto> updateUser(User currentUser, Long userId, UpdateUserRequest updateUserRequest) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        if (currentUser.getUserRole().getName().equals("ADMIN") ||
                (currentUser.getUserRole().getName().equals("MANAGER") &&
                        existingUser.getDepartment().getName().equals(currentUser.getDepartment().getName()))) {

            UserRole userRole = userRoleRepository.findByName(updateUserRequest.getRole());
            Department department = departmentRepository.findByName(updateUserRequest.getDepartment());
            Company company = companyRepository.findByName(updateUserRequest.getCompany());

            existingUser.setUserRole(userRole);
            existingUser.setFirstName(updateUserRequest.getFirstName());
            existingUser.setLastName(updateUserRequest.getLastName());
            existingUser.setEmail(updateUserRequest.getEmail());
            existingUser.setDepartment(department);
            existingUser.setCompany(company);

            userRepository.save(existingUser);

            return ResponseEntity.status(HttpStatus.OK).body(getUserById(existingUser.getId()).getBody());
        } else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    public ResponseEntity<UserDto> deleteUser(User currentUser, Long userId) {

        // Retrieve the existing user from the database
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the current user is an admin or if the user belongs to the same department as the manager
        if (currentUser.getUserRole().getName().equals("ADMIN") ||
                (currentUser.getUserRole().getName().equals("MANAGER") &&
                        existingUser.getDepartment().getId().equals(currentUser.getDepartment().getId()))) {

            // Perform the deletion
            userRepository.delete(existingUser);

            // Return the deleted user's details in the response
            return ResponseEntity.status(HttpStatus.OK).body(getUserById(existingUser.getId()).getBody());
        } else {
            // Return a 403 Forbidden if the user is not an admin or doesn't have authority
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
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