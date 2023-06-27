package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.response.PostBoxResponse;
import it.cgmconsulting.myblog.payload.response.PostDetailResponse;
import it.cgmconsulting.myblog.payload.response.PostSearchResponse;
import it.cgmconsulting.myblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Value("${post.path}")
    private String imagePath;

    public void save(Post p){
        postRepository.save(p);
    }

    public boolean existsByTitle(String title){
        return postRepository.existsByTitle(title);
    }

    public Optional<Post> findById(long id){
        return postRepository.findById(id);
    }

    public Optional<Post> findByIdAndPublishedTrue(long id){
        return postRepository.findByIdAndPublishedTrue(id);
    }


    public List <PostBoxResponse> getPostBoxes(int item) {
        int i = 0;
        List<PostBoxResponse> postBoxes = postRepository.getPostBoxes();
        List<PostBoxResponse> list = new ArrayList<>();
        // il numero di elementi nella lista dei post potrebbe essere inferiore al parametro item
        // quindi per evitare un IndexOutOfBoundsException devo testare anche la dimensione della mia lista
        while (i < item && i < postBoxes.size()){
            // aggiungo al nome file dell'immagine anche il path prendendone il valore dall'application.yml
            PostBoxResponse b = new PostBoxResponse(postBoxes.get(i).getId(), postBoxes.get(i).getTitle(), imagePath+postBoxes.get(i).getImage());
            list.add(b);
            i++;
        }
        return list;
    }

    public List<PostSearchResponse> getPostSearchResponse(String keyword){
        return postRepository.getPostSearchResponse("%"+keyword+"%");
    }

    public List<PostSearchResponse> getPostSearchResponseNNQ(String keyword){
        return postRepository.getPostSearchResponseNNQ("%"+keyword+"%");
    }


    public List<PostSearchResponse> getPostSearchResponsePaged(
            int pageNumber, // numero di pagina da cui partire
            int pageSize, // numero degli elementi per pagina
            String direction, // ASC or DESC
            String sortBy, // la colonna su cui effettuare l'ordinamento
            String keyword
    ){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostSearchResponse> pageResult = postRepository.getPostSearchResponsePaged(pageable, "%"+keyword+"%");
        if(pageResult.hasContent()){
            List<PostSearchResponse> list = pageResult.getContent();
            return list;
        }else{
            return new ArrayList<PostSearchResponse>();
        }
    }

    public PostDetailResponse getPostDetailResponse(long postId){
        return postRepository.getPostDetailResponse(postId, imagePath);
    }





}
