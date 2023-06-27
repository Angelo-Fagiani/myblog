package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired CommentRepository commentRepository;

    public void save(Comment c){
        commentRepository.save(c);
    }

    public Optional<Comment> findById(long id){
        return commentRepository.findById(id);
    }

    public List<Comment> getCommentsByPost(long postId){
        return commentRepository.getCommentsByPost(postId);
    }

    public List<CommentResponse> getComments(long id){
        return commentRepository.getComments(id);
    }

}
