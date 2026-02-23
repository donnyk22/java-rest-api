package com.github.donnyk22.models.entities;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Teachers extends BaseTimestampCreateUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "full_name")
    private String fullName;
    private Character gender;
    private String phone;
    private String address;
    private String photo;
    private Boolean deleted;
    // handle versioning
    @Version
    private Integer version;

    // ==== Relation ====

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HomeroomTeachers> homeroomTeachers;
    
}
