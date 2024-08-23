package com.ceylin.companyorganizationSoftware.Repository;


import com.ceylin.companyorganizationSoftware.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {

  Department findByName(String name);
}
