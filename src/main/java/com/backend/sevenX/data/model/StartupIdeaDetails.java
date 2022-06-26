package com.backend.sevenX.data.model;

import com.backend.sevenX.utills.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "startup_idea_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {Constant.EntityField.DELETED_AT})
                // hibernate bug of inheritance second parameter accepting as entity variable name
        })
public class StartupIdeaDetails extends Base {

    private Integer userId;

    private String startupName;

    private String email;

    private String contactNo;

    private String stage;

    private String idea;

    private String industry;

    private String remarks;

    @OneToMany( cascade = CascadeType.ALL)
    private List<Document> documents;

}
