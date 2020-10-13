package com.baeldung.loadtesting.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CustomerRewardsAccount {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private Integer customerId;

    public Integer getCustomerId(){
        return this.customerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    
}
