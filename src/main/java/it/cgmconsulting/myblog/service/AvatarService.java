package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarService {

    @Autowired AvatarRepository avatarRepository;

    public void save(Avatar avatar){
        avatarRepository.save(avatar);
    }

    public void delete(Avatar avatar){
        avatarRepository.delete(avatar);
    }

    public Avatar fromMultipartFileToAvatar(MultipartFile file) throws IOException {
        Avatar avatar = new Avatar(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getBytes()
        );
        return avatar;
    }

}
