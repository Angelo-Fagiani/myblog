package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.security.CurrentUser;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.PostService;
import it.cgmconsulting.myblog.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping("rate")
@Validated
public class RatingController {

    @Autowired PostService postService;
    @Autowired RatingService ratingService;

    @PutMapping("/{postId}/{rate}")
    @PreAuthorize("hasRole('ROLE_READER')")
    public ResponseEntity vote(@PathVariable long postId, @CurrentUser UserPrincipal userPrincipal, @PathVariable @Min(1) @Max(5) byte rate){

        Optional<Post> p = postService.findByIdAndPublishedTrue(postId);
        if(p.isEmpty())
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);

        Rating r = new Rating(new RatingId(new User(userPrincipal.getId()), p.get()), rate);
        ratingService.save(r);

        return new ResponseEntity(rate, HttpStatus.OK);
    }

}
