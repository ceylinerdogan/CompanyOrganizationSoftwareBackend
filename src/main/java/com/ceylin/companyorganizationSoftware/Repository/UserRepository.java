package com.ceylin.companyorganizationSoftware.Repository;

import com.ceylin.companyorganizationSoftware.Model.Department;
import com.ceylin.companyorganizationSoftware.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        @Override
        Optional<User> findById(Long id);
        Optional<User> findByEmail(String email);
        List<User> findAll();
        Page<User> findByDepartment(Department department, Pageable pageable);
}

