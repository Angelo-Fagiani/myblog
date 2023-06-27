package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.mail.MailService;
import it.cgmconsulting.myblog.payload.request.UpdateUserProfile;
import it.cgmconsulting.myblog.payload.response.UserMe;
import it.cgmconsulting.myblog.security.CurrentUser;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.AvatarService;
import it.cgmconsulting.myblog.service.FileService;
import it.cgmconsulting.myblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("user")
@Validated
@Slf4j
public class UserController {

    @Autowired UserService userService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired MailService mailService;
    @Autowired FileService fileService;
    @Autowired AvatarService avatarService;

    @Value("${avatar.size}")
    private long size;

    @Value("${avatar.width}")
    private int width;

    @Value("${avatar.height}")
    private int height;

    @Value("${avatar.extensions}")
    private String[] extensions;


    @PatchMapping
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid UpdateUserProfile request, @CurrentUser UserPrincipal userPrincipal){

        Optional<User> u = userService.findById(userPrincipal.getId());

        // verifico se esiste utente con username passata dalla request
        // se esiste, verifico che l'id dell'utente trovato NON corrisponda all'ide dello userPrincipla
        if(!u.get().getUsername().equals(request.getNewUsername()) && userService.existsByUsername(request.getNewUsername()))
            return new ResponseEntity("Username already in use", HttpStatus.FORBIDDEN);
        u.get().setUsername(request.getNewUsername());

        // stessa cosa per l'email
        if(!u.get().getEmail().equals(request.getNewEmail()) && userService.existsByEmail(request.getNewEmail()))
            return new ResponseEntity("Email already in use", HttpStatus.FORBIDDEN);
        u.get().setEmail(request.getNewEmail());

        return new ResponseEntity("User has been updated", HttpStatus.OK);

    }

    // cambio password:
    //      1) utente decide di cambiare password
    //      2) l'utente non ricorda la password e richiede il reset


    // 1)
    @PatchMapping("/")
    @Transactional
    public ResponseEntity<?> updatePassword(@CurrentUser UserPrincipal userPrincipal, @RequestParam @Pattern(regexp = "^[a-zA-Z0-9]{5,15}$",
            message = "Password must be of 5 to 15 length with no special characters") String newPassword){

        Optional<User> u = userService.findById(userPrincipal.getId());
        // verifico che la nuova password non Sia identica a quella presente sul db
        if(passwordEncoder.matches(newPassword, u.get().getPassword()))
            return new ResponseEntity("The new password is equal to old password", HttpStatus.BAD_REQUEST);
        u.get().setPassword(passwordEncoder.encode(newPassword));

        return new ResponseEntity("Password has been updated", HttpStatus.OK);
    }

    @PostMapping("/auth")
    @Transactional
    public ResponseEntity<?> forgotPassword(@RequestParam String username){

        String temporaryPassword = userService.generateSecureRandomPassword();
        Optional<User> u = userService.findByUsernameAndEnabledTrue(username.trim());
        if(u.isEmpty())
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        mailService.sendMail(mailService.createMail(
                u.get(),
                "Reset password request",
                "Plaese login with this temporary password:\n",
                temporaryPassword
        ));

        u.get().setPassword(passwordEncoder.encode(temporaryPassword));

        return new ResponseEntity<>("Please check your email and follow the instructions", HttpStatus.OK);

    }

    @PatchMapping(value="avatar", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> updateAvatar(@CurrentUser UserPrincipal userPrincipal, @RequestParam @NotNull MultipartFile file) throws IOException {

        if(!fileService.checkSize(file, size))
            return new ResponseEntity("File empty or size grater than "+size, HttpStatus.BAD_REQUEST);

        if(!fileService.checkDimensions(fileService.fromMutipartFileToBufferedImage(file), width, height))
            return new ResponseEntity("Wrong width or height image", HttpStatus.BAD_REQUEST);

        if(!fileService.checkExtension(file, extensions))
            return new ResponseEntity("File type not allowed", HttpStatus.BAD_REQUEST);

        Optional<User> u = userService.findById(userPrincipal.getId());
        Avatar avatar = avatarService.fromMultipartFileToAvatar(file);

        if(u.get().getAvatar() != null)
            avatar.setId(u.get().getAvatar().getId());
        avatarService.save(avatar);

        u.get().setAvatar(avatar);

        return new ResponseEntity("Your avatar has been update",  HttpStatus.OK);

    }

    @DeleteMapping("avatar")
    @Transactional
    public ResponseEntity<?> deleteAvatar(@CurrentUser UserPrincipal userPrincipal){

        Optional<User> u = userService.findById(userPrincipal.getId());
        Avatar avatar = u.get().getAvatar();
        if(avatar != null) {
            u.get().setAvatar(null);
            avatarService.delete(avatar);
            return new ResponseEntity("Your avatar has been removed",  HttpStatus.OK);
        } else {
            return new ResponseEntity("No avatar to remove",  HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("me")
    public ResponseEntity<?> getMe(@CurrentUser UserPrincipal userPrincipal){
        UserMe u = userService.getMe(userPrincipal.getId());
        //log.info(u.toString());
        return new ResponseEntity(u, HttpStatus.OK);
    }

}
