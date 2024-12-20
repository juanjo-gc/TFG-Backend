package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.PostRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.PostDTO;
import es.uca.tfg.backend.service.PostService;
import es.uca.tfg.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
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
        Optional<User> optionalUser = _userRepository.findById(dtopost.get_iUserId());
        if(optionalUser.isPresent()) {
            Post post = new Post(dtopost.get_sText(), optionalUser.get());
            return _postRepository.save(post);
        } else {
            return new Post();
        }
    }

    @GetMapping("/getUserPosts/{username}")
    public List<Post> getUserPosts(@PathVariable("username") String sUsername) {
        List<Post> posts = _postRepository.findBy_user(_userRepository.findBy_sUsername(sUsername));
        if(posts.isEmpty() || posts == null) {
            return Collections.emptyList();
        } else {
            Collections.sort(posts, new Post.PostComparator());
            return posts;
        }
    }



    @GetMapping("/getTimelinePosts/{id}/{pageNumber}")
    public Page<Post> getTimelinePosts(@PathVariable("id") int iUserId, @PathVariable("pageNumber") int iPageNumber) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if(optionalUser.isPresent()) {
            return _postRepository.findTimelinePosts(optionalUser.get(), PageRequest.of(iPageNumber, 30));
        } else {
            return Page.empty();
        }
    }

    @PostMapping("/setLike/{postId}/{userId}")
    public boolean setLike(@PathVariable("postId") int iPostId, @PathVariable("userId") int iUserId) {
        boolean bPostWasLiked = false;
        Post post = _postRepository.findBy_iId(iPostId);
        User user = _userRepository.findBy_iId(iUserId);


        if(post.get_setLikes().contains(user)) {    //El usuario ya le había dado like al post
            post.get_setLikes().remove(user);
        } else {
            bPostWasLiked = true;
            post.get_setLikes().add(user);
        }
        post.set_iLikes(post.get_setLikes().size());
        post = _postRepository.save(post);

        return bPostWasLiked;
    }

    @GetMapping("/getPost/{postId}")
    public Post getPost(@PathVariable("postId") int iPostId) {
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        return optionalPost.isPresent() ? optionalPost.get() : new Post();
    }

    @GetMapping("/getLikes/{postId}")
    public List<User> getPostLikes(@PathVariable("postId") int iPostId) {
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        return optionalPost.isPresent() ? optionalPost.get().get_setLikes().stream().toList() : Collections.emptyList();
    }

    @GetMapping("/getReplies/{postId}/{pageNumber}")
    public Page<Post> getPostReplies(@PathVariable("postId") int iPostId, @PathVariable("pageNumber") int iPageNumber) {
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        return optionalPost.isPresent() ? _postRepository.findPostReplies(optionalPost.get(), PageRequest.of(iPageNumber, 5)) : Page.empty();
    }

    @PostMapping("/newReply/{postId}")
    public Post newReply(@RequestBody PostDTO postDTO, @PathVariable("postId") int iPostId) {
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        if(optionalPost.isPresent()) {
            Post reply = new Post(postDTO.get_sText(), _userRepository.findBy_iId(postDTO.get_iUserId()));
            reply.set_repliesTo(optionalPost.get());
            optionalPost.get().get_setReplies().add(reply);
            optionalPost.get().set_iReplies(optionalPost.get().get_setReplies().size());
            _postRepository.save(optionalPost.get());
            return _postRepository.save(reply);
        } else  {
            return new Post();
        }
    }

    @PatchMapping("/softDeleteOrRestorePost/{postId}")
    public boolean softDeletePost(@PathVariable("postId") int iPostId) {
        Optional<Post> optionalPost = _postRepository.findById(iPostId);
        if(optionalPost.isPresent()) {
            if(Objects.equals(optionalPost.get().get_tDeleteDate(), null))
                optionalPost.get().set_tDeleteDate(LocalDateTime.now());
            else
                optionalPost.get().set_tDeleteDate(null);

            return _postRepository.save(optionalPost.get()).get_tDeleteDate() != null;
        } else {
            return false;
        }

    }
    @GetMapping("/getLikedPosts/{userId}")
    public Set<Post> getLikedPosts(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? optionalUser.get().get_setLikedPosts() : Collections.emptySet();
    }

    @GetMapping("/countPosts")
    public long countPosts() {
        return _postRepository.count();
    }

    @GetMapping("/checkLike/{postId}/{userId}")
    public boolean checkLike(@PathVariable("postId") int iPostId, @PathVariable("userId") int iUserId) {
        Post post = _postRepository.findById(iPostId).get();
        User user = _userRepository.findById(iUserId).get();
        return post.get_setLikes().contains(user);
    }


}
