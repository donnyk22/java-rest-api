package com.github.donnyk22.models.entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@Entity
@Table(name = "teachers")
//override delete behaviour by JPA to soft delete
@SQLDelete(sql = "UPDATE teachers SET deleted = true WHERE id = ?")
//automatically add "where deleted = false"
@SQLRestriction("deleted = false")
public class Users extends BaseTimestampCreateUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String photo;
    private String role;
    @Column(name = "is_active")
    private Boolean isActive;
    private Boolean deleted;
    // handle versioning
    @Version
    private Integer version;

    // ==== Relation ====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Teachers teachers;
}
