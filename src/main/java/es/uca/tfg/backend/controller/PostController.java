package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.PostRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostRepository _postRepository;

    @Autowired
    private UserRepository _userRepository;

    @PostMapping("/newPost")
    public String newPost(@RequestBody PostDTO dtopost) {

        Post post = new Post(dtopost.get_sText(), _userRepository.findBy_iId(dtopost.get_iUserId()));
        System.out.println(post.get_sText());
        System.out.println(post.get_user().get_sUsername());
        post = _postRepository.save(post);

        return "Funsiona";
    }

    @GetMapping("/getPosts")
    public void getPosts() {
        User user = _userRepository.findBy_iId(1);
        int i = 1;
        List<Post> posts = new ArrayList(_userRepository.findBy_iId(1).get_setPosts());
        Collections.sort(posts, new Post.PostComparator());
        for(Post post: posts) {
            System.out.println("Iteración " + i + " Fecha y hora: " + post.get_tCreatedAt());
            i++;
        }
    }
}
