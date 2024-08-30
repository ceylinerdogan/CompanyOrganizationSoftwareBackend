package com.ceylin.companyorganizationSoftware.Controller;

import com.ceylin.companyorganizationSoftware.Dto.Request.AddUserRequest;
import com.ceylin.companyorganizationSoftware.Dto.Request.UpdateUserRequest;
import com.ceylin.companyorganizationSoftware.Dto.UserDto;
import com.ceylin.companyorganizationSoftware.Model.User;
import com.ceylin.companyorganizationSoftware.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

  private final UserService userService;
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Add User", description = "Add a new user to the system.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User added successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid request data")
  })
  @PostMapping("/add-user")
  public ResponseEntity<UserDto> addUser(@RequestBody AddUserRequest addUserRequest) {

    return userService.addUser(addUserRequest);
  }

  // Admin - Get all users
  @Operation(summary = "Get All Users for Admin", description = "Retrieve all users. Admin access only.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
          @ApiResponse(responseCode = "403", description = "Access forbidden")
  })
  @GetMapping("/all")
  public ResponseEntity<Page<UserDto>> getAllUsers(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "firstName") String sortBy) {


    Page<UserDto> users = userService.getAllUsers(PageRequest.of(page, size, Sort.by(sortBy)));
    return ResponseEntity.ok(users);
  }

  // Manager - Get users in manager's department
  @Operation(summary = "Get Users in Department for Manager", description = "Retrieve all users in the manager's department.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
          @ApiResponse(responseCode = "403", description = "Access forbidden")
  })
  @GetMapping("/belonging-department")
  public ResponseEntity<Page<UserDto>> getUsersInDepartment(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "firstName") String sortBy) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User manager = (User) authentication.getPrincipal();

    Page<UserDto> users = userService.getUsersInDepartment(manager, PageRequest.of(page, size, Sort.by(sortBy)));
    return ResponseEntity.ok(users);
  }

  // Admin/Manager - Update user
  @Operation(summary = "Update User for both Admin and Manager", description = "Update user information. Admins can update any user, managers can update users in their department.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User updated successfully"),
          @ApiResponse(responseCode = "403", description = "Access forbidden"),
          @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PutMapping("/update/{id}")
  public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    return userService.updateUser(user, id, updateUserRequest);
  }

}
