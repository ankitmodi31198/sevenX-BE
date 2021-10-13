package com.backend.sevenX.data.model;

import com.backend.sevenX.utills.Constant;
import lombok.Data;
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
@Table( name = "document",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"documentURL", Constant.EntityField.DELETED_AT})
		// hibernate bug of inheritance second parameter accepting as entity variable name
	})
//soft delete condition
@SQLDelete(sql =
	"UPDATE document SET "+ Constant.DbField.DELETED_AT +"=now() " +
		"WHERE id = ?")
@Where(clause = Constant.DbField.DELETED_AT +" IS NULL")
public class Document extends Base{

	private String documentURL;

	private String documentTitle;

	private String documentFor;

	private Integer userId;

	private String screenName;

}
