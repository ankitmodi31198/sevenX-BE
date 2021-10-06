package com.backend.sevenX.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class CartPackages extends Base {

    private Integer cartDetailsId;

    private Integer packageId;
}
