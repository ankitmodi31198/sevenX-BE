package com.backend.sevenX.data.model;

import com.backend.sevenX.utills.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "co_founder_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {Constant.EntityField.DELETED_AT})
                // hibernate bug of inheritance second parameter accepting as entity variable name
        })
public class CoFounderDetails extends Base {

    private Integer userId;

    private String startupName;

    private String mobileNo;

    private String emailId;

    private String startupYear;

    private String stage;

    private String idea;

    private String industry;

    private String profileSkills;

    private String state;

    private String remarks;
}
