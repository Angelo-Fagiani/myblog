package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.payload.response.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    // metodo derivato
    List<Category> findByVisibleTrue();

    // JPQL : Java Persistent Query Language
    @Query(value="SELECT c.categoryName FROM Category c WHERE visible=true ORDER BY c.categoryName")
    List<String> getByVisibleTrue();
    // JPQL
    @Query(value = "SELECT new it.cgmconsulting.myblog.payload.response.CategoryResponse(" +
            "c.categoryName, " +
            "c.visible" +
            ") FROM Category c " +
            "WHERE c.visible=true ORDER BY c.categoryName")
    List<CategoryResponse> getCategoryByVisibleTrue();

    // SQL STANDARD
    @Query(value="SELECT * FROM category WHERE visible=1 ORDER BY category_name", nativeQuery = true)
    List<Category> getCategoryByVisibleTrueSQL();

    Set<Category> findByVisibleTrueAndCategoryNameIn(Set<String> categories);
/*
    @Query(value="SELECT pc.category_name " +
            "FROM post_categories pc " +
            "LEFT JOIN category c ON c.category_name=pc.category_name " +
            "WHERE c.visible=true  " +
            "AND pc.post_id = :postId" , nativeQuery = true)
    Set<String> getCategoriesNameByPost(@Param("postId") long id);

*/
    @Query(value="SELECT cs.categoryName " +
            "FROM Post p " +
            "LEFT JOIN p.categories cs " +
            "WHERE p.id = :postId AND cs.visible=true")
    Set<String> getCategoriesNameByPost(@Param("postId") long id);




}
