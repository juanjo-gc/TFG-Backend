package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.PostRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.PostDTO;
import es.uca.tfg.backend.service.PostService;
import es.uca.tfg.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class PostController {


    @Autowired
    private PostRepository _postRepository;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private PostService _postService;

    @PostMapping("/newPost")
    public Post newPost(@RequestBody PostDTO dtopost) {

        Post post = new Post(dtopost.get_sText(), _userRepository.findBy_iId(dtopost.get_iUserId()));
        System.out.println(post.get_sText());
        System.out.println(post.get_user().get_sUsername());
        return _postRepository.save(post);
    }


    @GetMapping("/getUserPosts/{username}")
    public List<Post> getUserPosts(@PathVariable("username") String sUsername) {
        System.out.println("Recibido: " + sUsername);
        List<Post> posts = _postRepository.findBy_user(_userRepository.findBy_sUsername(sUsername));
        System.out.println("Username en getuserposts: " + _userRepository.findBy_sUsername(sUsername).get_sUsername());
        System.out.println("Numero de posts: " + posts.size());
        if(posts.isEmpty() || posts == null) {
            return Collections.emptyList();
        } else {
            Collections.sort(posts, new Post.PostComparator());
            return posts;
        }
    }



    @GetMapping("/getTimelinePosts/{id}")
    public List<Post> getTimelinePosts(@PathVariable("id") int iUserId) {
        User user = _userRepository.findBy_iId(iUserId);
        List<Post> orderedPosts = _postRepository.findBy_user(user);

        for(User followed: user.get_setFollowing()) {
            List<Post> followedUserPosts = _postRepository.findBy_user(followed);
            orderedPosts.addAll(followedUserPosts);
        }
        orderedPosts = orderedPosts.stream()
                .filter(p -> p.get_tCreatedAt().isAfter(LocalDateTime.now().minusDays(10)))     //Publicaciones de hace menos de 10 días
                .sorted(Comparator.comparing(Post::get_tCreatedAt).reversed())         // Ordenados por fecha (el primero el más reciente)
                .collect(Collectors.toList());


        for(Post post: orderedPosts) {
            System.out.println("Texto: " + post.get_sText() + "| Fecha: " + post.get_tCreatedAt());
        }



        return orderedPosts;
    }

    @PostMapping("/setLike/{postId}/{userId}")
    public boolean setLike(@PathVariable("postId") int iPostId, @PathVariable("userId") int iUserId) {
        boolean bPostWasLiked = false;
        Post post = _postRepository.findBy_iId(iPostId);
        User user = _userRepository.findBy_iId(iUserId);


        if(post.get_setLikes().contains(user)) {    //El usuario ya le había dado like al post
            post.get_setLikes().remove(user);
            post = _postRepository.save(post);
            System.out.println("Ya le habia dado like");
        } else {
            bPostWasLiked = true;
            System.out.println("Antes de añadir al usuario: " + post.get_setLikes().size());
            post.get_setLikes().add(user);
            System.out.println("Despues de añadir al usuario: " + post.get_setLikes().size());
            System.out.println("No le habia dado like");
        }
        post.set_iLikes(post.get_setLikes().size());
        post = _postRepository.save(post);

        return bPostWasLiked;
    }

    @GetMapping("/getPost/{postId}")
    public Post getPost(@PathVariable("postId") int iPostId) {
        System.out.println("Peticion de post con id " + iPostId);
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        return optionalPost.isPresent() ? optionalPost.get() : new Post();
    }

    @GetMapping("/getLikes/{postId}")
    public List<User> getPostLikes(@PathVariable("postId") int iPostId) {
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        return optionalPost.isPresent() ? optionalPost.get().get_setLikes().stream().toList() : Collections.emptyList();
    }

    @GetMapping("/getReplies/{postId}")
    public List<Post> getPostReplies(@PathVariable("postId") int iPostId) {
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        return optionalPost.isPresent() ? optionalPost.get().get_setReplies().stream().toList() : Collections.emptyList();
    }

    @PostMapping("/newReply/{postId}")
    public Post newReply(@RequestBody PostDTO postDTO, @PathVariable("postId") int iPostId) {
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        if(optionalPost.isPresent()) {
            Post reply = new Post(postDTO.get_sText(), _userRepository.findBy_iId(postDTO.get_iUserId()));
            reply.set_repliesTo(optionalPost.get());
            optionalPost.get().get_setReplies().add(reply);
            _postRepository.save(optionalPost.get());
            return _postRepository.save(reply);
        } else  {
            return new Post();
        }
    }






}
