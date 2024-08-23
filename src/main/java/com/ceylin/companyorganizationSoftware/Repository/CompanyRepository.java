package com.ceylin.companyorganizationSoftware.Repository;


import com.ceylin.companyorganizationSoftware.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {
  Company findByName(String name);

}
