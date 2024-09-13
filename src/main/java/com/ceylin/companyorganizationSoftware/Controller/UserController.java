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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

  private final UserService userService;

  @Value("${file.upload-dir}")
  private String uploadDir;


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
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();
    return userService.addUser(currentUser, addUserRequest);
  }

  // Admin - Get ALL users
  @Operation(summary = "Get All Users for Admin", description = "Retrieve all users. Admin access only.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
          @ApiResponse(responseCode = "403", description = "Access forbidden")
  })
  @GetMapping("/all")
  public ResponseEntity<Page<UserDto>> getAllUsers(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "id") String sortBy) {


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
          @RequestParam(defaultValue = "id") String sortBy) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User manager = (User) authentication.getPrincipal();

    Page<UserDto> users = userService.getUsersInDepartment(manager, PageRequest.of(page, size, Sort.by(sortBy)));
    return ResponseEntity.ok(users);
  }

  // Admin/Manager - UPDATE
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
  //DELETE
  @Operation(summary = "Delete User", description = "Delete a user by their ID. Admin access only.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User deleted successfully"),
          @ApiResponse(responseCode = "403", description = "Access forbidden"),
          @ApiResponse(responseCode = "404", description = "User not found")
  })
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();
    return userService.deleteUser(currentUser, id);
  }
  @Operation(summary = "Get User Profile", description = "Retrieve the current logged-in user's profile information.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
          @ApiResponse(responseCode = "403", description = "Access forbidden"),
          @ApiResponse(responseCode = "404", description = "User not found")
  })
  @GetMapping("/profile")
  public ResponseEntity<UserDto> getUserProfile() {
    // Get the logged-in user
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();

    // Fetch user details from service
    return userService.getUserProfile(currentUser.getId());
  }

  @Operation(summary = "Upload Profile Picture", description = "Allows the logged-in user to upload a profile picture in base64 format.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
          @ApiResponse(responseCode = "400", description = "File upload failed")
  })
  @PostMapping("/upload-profile-picture")
  public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();

    try {
      return userService.uploadProfilePicture(currentUser, file);
    } catch (IOException e) {
      return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
    }
  }
}
