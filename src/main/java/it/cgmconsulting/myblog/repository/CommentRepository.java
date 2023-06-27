package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post p);

    @Query(value="SELECT * FROM comment c WHERE c.post_id = :postId", nativeQuery = true) // SQL nativo
    List<Comment> getCommentsByPost(@Param("postId") long postId);

    @Query(value = "SELECT new it.cgmconsulting.myblog.payload.response.CommentResponse( " +
            "c.id," +
            //"c.comment," +
            "CASE WHEN (c.censored = true) THEN '*********' ELSE c.comment END, " +
            "c.createdAt," +
            "c.author.username) " +
            "FROM Comment c " +
            "WHERE c.post.id = :postId " +
            "ORDER BY c.createdAt DESC")
    List<CommentResponse> getComments(@Param("postId") long postId);
}
