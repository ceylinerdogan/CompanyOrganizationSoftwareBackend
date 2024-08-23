package com.ceylin.companyorganizationSoftware.Controller;

import com.ceylin.companyorganizationSoftware.Dto.Request.AddUserRequest;
import com.ceylin.companyorganizationSoftware.Dto.Response.Response;
import com.ceylin.companyorganizationSoftware.Dto.UserDto;
import com.ceylin.companyorganizationSoftware.Model.User;
import com.ceylin.companyorganizationSoftware.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/add-user")
  public ResponseEntity<UserDto> addUser(@RequestBody AddUserRequest addUserRequest) {

    return userService.addUser(addUserRequest);
  }

}
