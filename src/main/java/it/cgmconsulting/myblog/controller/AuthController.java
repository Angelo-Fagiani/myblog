package it.cgmconsulting.myblog.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import it.cgmconsulting.myblog.mail.MailService;
import it.cgmconsulting.myblog.payload.request.UpdateUserAuthority;
import it.cgmconsulting.myblog.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.payload.response.JwtAuthenticationResponse;
import it.cgmconsulting.myblog.security.JwtTokenProvider;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.AuthorityService;
import it.cgmconsulting.myblog.service.UserService;

import java.util.*;


@RestController
@RequestMapping("auth") // localhost:{port}/auth/....
@Validated
public class AuthController {

    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserService userService;
    @Autowired AuthorityService authorityService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtTokenProvider tokenProvider;
    @Autowired MailService mailService;


    @PostMapping("signin")
    @Transactional
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest request) {
        Optional<User> u = userService.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail());
        if(!u.isPresent())
            return new ResponseEntity<String>("Bad Credentials", HttpStatus.FORBIDDEN);

        // check se utente è bannato e se il suo ban è scaduto; se il ban è scaduto lo riabilito
        String s = null;
        if(!u.get().isEnabled()) {
            s = userService.checkBan(u.get(), u.get().getUpdatedAt());
            if (s != null)
                return new ResponseEntity<String>(s, HttpStatus.FORBIDDEN);
        }

        if(!passwordEncoder.matches(request.getPassword(), u.get().getPassword()))
            return new ResponseEntity<String>("Bad Credentials", HttpStatus.FORBIDDEN);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		request.getUsernameOrEmail(),
                		request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtTokenProvider.generateToken(authentication);
        JwtAuthenticationResponse currentUser = UserPrincipal.createJwtAuthenticationResponseFromUserPrincipal((UserPrincipal) authentication.getPrincipal(), jwt);

        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("signup")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    	if(userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Username already in use", HttpStatus.BAD_REQUEST);
        }

        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Email Address already in use!", HttpStatus.BAD_REQUEST);
        }

        Optional<Authority> authority = authorityService.findByAuthorityName("ROLE_GUEST");

        // Creating user's account // User user = new User();
        String confirmCode = UUID.randomUUID().toString();
        User user = new User(
    		signUpRequest.getUsername().trim(),
            signUpRequest.getEmail().toLowerCase().trim(),
            passwordEncoder.encode(signUpRequest.getPassword().trim()),
            Collections.singleton(authority.get()), // transforms object Authority into Set<Authority>
            confirmCode
        );

        userService.save(user);

        // invio confirm code
        mailService.sendMail(mailService.createMail(user, "Myblog - Confirm code", "In order to confirm your registration, please click this link http://localhost:8083/auth/confirm/"+confirmCode, ""));

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @PatchMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> updateAuthority(@RequestBody @Valid UpdateUserAuthority request, @CurrentUser UserPrincipal userPrincipal){

        // Un utente avente il ruolo di ADMIN  non può modificare i ruoli su se stesso.
        if(request.getUserId() == userPrincipal.getId())
            return new ResponseEntity<String>("You cannot update your own authority", HttpStatus.FORBIDDEN);

        Optional<User> u = userService.findByIdAndEnabledTrue(request.getUserId());
        if(u.isEmpty())
            return new ResponseEntity<String>("User not found or not enabled", HttpStatus.NOT_FOUND);

        Set<Authority> authorities = authorityService.findByAuthorityNameIn(request.getAuthorities());
        if(authorities.isEmpty())
            return new ResponseEntity<String>("Authorities not found", HttpStatus.NOT_FOUND);

        u.get().setAuthorities(authorities);

        return new ResponseEntity<String>("Authorities have been updated", HttpStatus.OK);

    }


    @PatchMapping("confirm/{confirmCode}") // esempio: localhost:8083/auth/confirm/3a831efc-b6a4-493b-8b3c-8f2af0154a34
    @Transactional
    public ResponseEntity<?> registrationConfirm(@PathVariable @NotBlank String confirmCode){

        Optional<User> u = userService.findByConfirmCode(confirmCode);
        if(u.isEmpty())
            return new ResponseEntity<String>("User not found or already confirmed", HttpStatus.NOT_FOUND);

        u.get().setEnabled(true);
        u.get().setConfirmCode(null);

        Optional<Authority> authority = authorityService.findByAuthorityName("ROLE_READER");
        if(u.isEmpty())
            return new ResponseEntity<String>("Authority not found", HttpStatus.NOT_FOUND);
        u.get().setAuthorities(Collections.singleton(authority.get()));

        return new ResponseEntity<String>("Your registration has been confirmed, please login", HttpStatus.OK);

    }



}

