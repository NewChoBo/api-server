package jjk.api.api_server.feature.user.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  private String loginId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String username;

  @Column
  private String email;

  @Column(updatable = false)
  private LocalDateTime createdDate;

  @Column
  private LocalDateTime updatedDate;

  @ManyToMany
  @JoinTable
  private Set<Role> roles = new HashSet<>();
}
