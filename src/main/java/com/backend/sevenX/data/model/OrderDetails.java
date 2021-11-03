package com.backend.sevenX.data.model;

import com.backend.sevenX.utills.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@Table( name = "order_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {Constant.EntityField.DELETED_AT})
                // hibernate bug of inheritance second parameter accepting as entity variable name
        })
//soft delete condition
@SQLDelete(sql =
        "UPDATE order_details SET "+ Constant.DbField.DELETED_AT +"=now() " +
                "WHERE id = ?")
@Where(clause = Constant.DbField.DELETED_AT +" IS NULL")
public class OrderDetails extends Base {

    private Integer userId;

    private String username;

    private String firstName;

    private String phoneNo;

    private String address;

    private String state;

    private String gstNumber;

    private String panNumber;

    private Double subTotal;

   // private Double gstAmount;

    private Double orderTotal;

    private Double finalOrderTotal;

    @Lob
    private String note;

    @OneToMany( cascade = CascadeType.ALL)
    private List<OrderPackages> orderPackagesList;

    //@ColumnDefault(value = Constant.Status.Pending)
    private String orderStatus = Constant.Status.Pending ;

    //@ColumnDefault(value = "0.0")
    private Double additionalOrderCost = 0.0;

    @Lob
    private String transactionId;

   // @ColumnDefault(value = Constant.Status.Pending)
    private String transactionStatus = Constant.Status.Pending;

    @Lob
    private String transactionNote;
}
