package com.spaza.category.service;

import com.spaza.category.model.Category;
import com.spaza.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll(int page, int count) {
        return categoryRepository.findAll(PageRequest.of(page, count)).getContent();
    }
    
    public List<Category> findByMerchantId(Long merchantId) {
        return categoryRepository.findByMerchantId(merchantId);
    }
}
