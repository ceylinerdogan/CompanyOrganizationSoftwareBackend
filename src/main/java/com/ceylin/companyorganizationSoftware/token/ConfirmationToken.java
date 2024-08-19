package com.ceylin.companyorganizationSoftware.token;

import com.ceylin.companyorganizationSoftware.Model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Data
@Table(name="confirmationToken")
public class ConfirmationToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="token_id")
  private Long id;
  @Column(nullable = false)
  private String token;
  @Column(nullable = false)
  private Date createDate;
  @Column(nullable = false)
  private Date expiryDate;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "id")
  private User user;

  public ConfirmationToken(User user) {
    token = UUID.randomUUID().toString();
    createDate = new Date();
    expiryDate = (new Date(System.currentTimeMillis()+15*60*1000));
    this.user=user;
  }
}
