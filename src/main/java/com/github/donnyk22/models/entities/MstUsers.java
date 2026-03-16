package com.github.donnyk22.models.entities;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@Entity
//creating a new table in DB for audit, also inserting the transaction automatically
@Audited
@Table(name = "mst_users")
//override delete behaviour by JPA to soft delete
@SQLDelete(sql = "UPDATE mst_users SET deleted = true WHERE id = ? AND version = ?")
//automatically add "where deleted = false"
@SQLRestriction("deleted = false")
public class MstUsers extends BaseTimestampCreateUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String email;
    private String password;
    @Column(columnDefinition = "TEXT")
    private String photo;
    private String role;
    @Column(name = "mfa_enabled")
    private Boolean mfaEnabled;
    @Column(name = "mfa_secret")
    private String mfaSecret;
    @Column(name = "is_active")
    private Boolean isActive = true;
    private Boolean deleted = false;
    // handle versioning
    @Version
    private Integer version;

    // ==== Relation ====

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private MstStudents studentData;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private MstTeachers teacherData;

    @NotAudited //disable auditing
    @OneToMany(mappedBy = "userData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LogAuditTrails> auditTrailsData;
}
