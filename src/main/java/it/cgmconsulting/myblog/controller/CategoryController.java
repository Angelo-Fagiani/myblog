package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.payload.response.CategoryResponse;
import it.cgmconsulting.myblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("category")
@Validated
public class CategoryController {

    @Autowired CategoryService categoryService;

    @PutMapping("/{category}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> save(@PathVariable @NotBlank @Size(max=50,min=2) String category){
        // verificare che non esista già la categoria che andremo ad inserire
        Optional<Category> cat = categoryService.findById(category);
        if(cat.isPresent())
            return new ResponseEntity<String>("Category already present", HttpStatus.BAD_REQUEST);

        // se non esiste, persisterla sul db
        Category c = new Category(category);
        categoryService.save(c);

        return new ResponseEntity<String>("New category "+category+" added", HttpStatus.CREATED);

    }

    @PatchMapping("/{category}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> switchVisibility(@PathVariable @NotBlank @Size(max=50,min=2) String category){

        // verificare che esista  la categoria su cui modificare la visibilità
        Optional<Category> cat = categoryService.findById(category);
        if(!cat.isPresent())
            return new ResponseEntity<String>("Category not found", HttpStatus.NOT_FOUND);
        // switch visibilità
        cat.get().setVisible(!cat.get().isVisible());

       // categoryService.save(cat.get());

        return new ResponseEntity<>(null, HttpStatus.OK);

    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllCategories(){
        List<Category> categories = categoryService.findAll();
        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }

    @GetMapping("public")
    public ResponseEntity<?> getCategories(){
        List<Category> categories = categoryService.findByVisibleTrue();
        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }

    @GetMapping("public/jpql")
    public ResponseEntity<?> getCategoriesJpql(){
        List<String> categories = categoryService.getByVisibleTrue();
        return new ResponseEntity<List<String>>(categories, HttpStatus.OK);
    }

    @GetMapping("public/jpql2")
    public ResponseEntity<?> getCategoriesJpql2(){
        List<CategoryResponse> categories = categoryService.getCategoryByVisibleTrue();
        return new ResponseEntity<List<CategoryResponse>>(categories, HttpStatus.OK);
    }

    @GetMapping("public/sql")
    public ResponseEntity<?> getCategoriesSQL(){
        List<Category> categories = categoryService.getCategoryByVisibleTrueSQL();
        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }

}
