package com.backend.sevenX.data.model;

import com.backend.sevenX.utills.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@Table( name = "cart_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {Constant.EntityField.DELETED_AT})
                // hibernate bug of inheritance second parameter accepting as entity variable name
        })
//soft delete condition
@SQLDelete(sql =
        "UPDATE cart_details SET "+ Constant.DbField.DELETED_AT +"=now() " +
                "WHERE id = ?")
@Where(clause = Constant.DbField.DELETED_AT +" IS NULL")
public class CartDetails extends Base{

    private Integer userId;

    private Double subTotal;

    private Double gstAmount;

    private Double orderTotal;

    private Double finalOrderTotal;

    @OneToMany( cascade = CascadeType.ALL)
    private List<CartPackages> cartPackagesList;
}
