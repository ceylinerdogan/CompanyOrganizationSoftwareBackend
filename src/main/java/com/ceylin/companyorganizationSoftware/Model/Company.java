package com.ceylin.companyorganizationSoftware.Model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Company")
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Column(name="shortName")
  private String shortName;

  @ManyToOne
  @JoinColumn(name = "companyTypeId",nullable = false)
  private CompanyType companyType;

  private String address;

  @ManyToOne
  @JoinColumn(name = "town",nullable = false)
  private Town town;
}
