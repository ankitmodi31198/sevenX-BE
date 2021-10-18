package com.backend.sevenX.data.model;

import com.backend.sevenX.utills.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@DynamicInsert
@Table( name = "cart_packages")
//soft delete condition
@SQLDelete(sql =
        "UPDATE cart_packages SET "+ Constant.DbField.DELETED_AT +"=now() " +
                "WHERE id = ?")
@Where(clause = Constant.DbField.DELETED_AT +" IS NULL")
public class CartPackages extends Base {

    private Integer cartDetailsId;

    private Integer packageId;

    //@ColumnDefault(value = "1")
    private Integer qty = 1;
}
