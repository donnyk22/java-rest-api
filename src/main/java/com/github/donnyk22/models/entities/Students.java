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
@Table(name = "students")
//override delete behaviour by JPA to soft delete
@SQLDelete(sql = "UPDATE students SET deleted = true WHERE id = ?")
//automatically add "where deleted = false"
@SQLRestriction("deleted = false")
public class Students extends BaseTimestampCreateUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "class_id")
    private Integer classId;
    @Column(name = "full_name")
    private String fullName;
    private Character gender;
    private String address;
    private String photo;
    private Boolean deleted;
    // handle versioning
    @Version
    private Integer version;

    // ==== Relation ====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", insertable = false, updatable = false)
    private Classes classroom;
}
