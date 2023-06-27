package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.payload.response.CategoryResponse;
import it.cgmconsulting.myblog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired CategoryRepository categoryRepository;

    public Optional<Category> findById(String category){
        return categoryRepository.findById(category);
    }

    public void save(Category cat){
        categoryRepository.save(cat);
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public List<Category> findByVisibleTrue(){
        return categoryRepository.findByVisibleTrue();
    }
    public List<String> getByVisibleTrue(){ return categoryRepository.getByVisibleTrue(); }
    public List<CategoryResponse> getCategoryByVisibleTrue(){ return categoryRepository.getCategoryByVisibleTrue(); }
    public List<Category> getCategoryByVisibleTrueSQL(){ return categoryRepository.getCategoryByVisibleTrueSQL(); }

    public Set<Category> findByVisibleTrueAndCategoryNameIn(Set<String> categories){
        return categoryRepository.findByVisibleTrueAndCategoryNameIn(categories);
    }

    public Set<String> getCategoriesNameByPost(long postId){
        return categoryRepository.getCategoriesNameByPost(postId);
    }

}
