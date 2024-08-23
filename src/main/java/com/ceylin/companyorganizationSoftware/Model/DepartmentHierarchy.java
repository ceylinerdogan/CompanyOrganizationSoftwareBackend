package com.ceylin.companyorganizationSoftware.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Department_Hierarchy")
public class DepartmentHierarchy {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "Child_Department_ID", nullable = false)
  private Department childDepartment;

  @ManyToOne
  @JoinColumn(name = "Parent_Department_ID", nullable = false)
  private Department parentDepartment;
}
