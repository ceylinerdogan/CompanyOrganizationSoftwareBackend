package com.ceylin.companyorganizationSoftware.Model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "Company_ID", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "Department_Type_ID", nullable = false)
    private DepartmentType departmentType;

    private String address;

    @ManyToOne
    @JoinColumn(name = "town",nullable = false)
    private Town town;


    @ManyToOne
    @JoinColumn(name = "Manager_ID", nullable = true)
    private User manager;

    @OneToMany(mappedBy = "parentDepartment")
    private List<DepartmentHierarchy> childDepartments;

    @OneToMany(mappedBy = "childDepartment")
    private List<DepartmentHierarchy> parentDepartments;
}
