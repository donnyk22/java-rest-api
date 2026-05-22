package com.github.donnyk22.models.entities;

import java.util.List;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
// creating a new table in DB for audit, also inserting the transaction
// automatically
@Audited
@Table(name = "mst_teachers")
@SoftDelete
public class MstTeachers extends BaseTimestampCreateUpdate {
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
    // handle versioning
    @Version
    private Integer version;

    // ==== Relation ====

    @OneToMany(mappedBy = "teacherData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MstHomeroomTeachers> homeroomTeachers;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MstUsers user;
}
