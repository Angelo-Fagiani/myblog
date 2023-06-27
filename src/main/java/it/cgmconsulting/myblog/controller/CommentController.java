package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.security.CurrentUser;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.CommentService;
import it.cgmconsulting.myblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("comment")
//@Validated
public class CommentController {


    @Autowired CommentService commentService;
    @Autowired PostService postService;

    @PutMapping
    @PreAuthorize("hasRole('ROLE_READER')")
    public ResponseEntity<?> save(@RequestBody @Valid CommentRequest request, @CurrentUser UserPrincipal userPrincipal){

        Optional<Post> p = postService.findById(request.getPostId());
        if(p.isEmpty())
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);

        Comment comment = new Comment(request.getComment(), new User(userPrincipal.getId()), p.get());
        commentService.save(comment);
        return new ResponseEntity<>("New comment added to post "+request.getPostId(), HttpStatus.CREATED);

    }


}
