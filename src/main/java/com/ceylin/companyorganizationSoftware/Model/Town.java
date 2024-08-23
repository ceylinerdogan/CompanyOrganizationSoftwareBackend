package com.ceylin.companyorganizationSoftware.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Town")
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "Region_ID", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "cityId", nullable = false)
    private City city;
}
