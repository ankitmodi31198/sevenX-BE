package com.backend.sevenX.data.model;

import com.backend.sevenX.utills.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
public class OrderPackages extends Base {

    private Integer orderDetailsId;

    private Integer packageId;

   // @ColumnDefault(value = "1")
    private Integer qty = 1;

    private Double packageAmount;

  //  @ColumnDefault(value = "0.0")
    private Double additionalCost = 0.0;

    private Double finalPackageAmount;

    @Lob
    private String note;
}
