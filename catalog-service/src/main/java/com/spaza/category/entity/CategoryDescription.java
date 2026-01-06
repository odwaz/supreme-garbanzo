package com.spaza.category.entity;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY_DESCRIPTION")
public class CategoryDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DESCRIPTION_ID")
    private Long id;
    
    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId;
    
    @Column(name = "LANGUAGE_ID", nullable = false)
    private Long languageId;
    
    @Column(name = "NAME", length = 120)
    private String name;
    
    @Column(name = "DESCRIPTION", length = 4000)
    private String description;
    
    @Column(name = "SEO_URL", length = 255)
    private String seoUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getLanguageId() { return languageId; }
    public void setLanguageId(Long languageId) { this.languageId = languageId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSeoUrl() { return seoUrl; }
    public void setSeoUrl(String seoUrl) { this.seoUrl = seoUrl; }
}
