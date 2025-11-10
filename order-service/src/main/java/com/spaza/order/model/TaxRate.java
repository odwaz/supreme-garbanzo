package com.spaza.order.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tax_rate")
public class TaxRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private String stateProvince;
    private BigDecimal rate;
    private Long taxClassId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getStateProvince() { return stateProvince; }
    public void setStateProvince(String stateProvince) { this.stateProvince = stateProvince; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }

    public Long getTaxClassId() { return taxClassId; }
    public void setTaxClassId(Long taxClassId) { this.taxClassId = taxClassId; }
}
