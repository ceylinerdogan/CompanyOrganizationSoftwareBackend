package com.ceylin.companyorganizationSoftware.Repository;

import com.ceylin.companyorganizationSoftware.Model.User;
import com.ceylin.companyorganizationSoftware.Model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository  <UserRole, Integer>{
  UserRole findByName(String name);


}
