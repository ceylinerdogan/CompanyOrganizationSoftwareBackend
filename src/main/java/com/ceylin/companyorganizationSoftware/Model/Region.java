package com.ceylin.companyorganizationSoftware.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Region")
public class Region {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "City_ID", nullable = false)
  private City city;

}
