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
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@Entity
@Table(name = "classes")
//override delete behaviour by JPA to soft delete
@SQLDelete(sql = "UPDATE classes SET deleted = true WHERE id = ?")
//automatically add "where deleted = false"
@SQLRestriction("deleted = false")
public class Classes extends BaseTimestampCreateUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "class_name")
    private String className;
    @Column(name = "grade_level")
    private String gradeLevel;
    @Column(name = "academic_year")
    private String academicYear;
    private Boolean deleted;
    // handle versioning
    @Version
    private Integer version;

    // ==== Relation ====

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HomeroomTeachers> homeroomTeachers;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<Students> students;
}
