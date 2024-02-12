package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.PostRepository;
import es.uca.tfg.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository _postRepository;
    private final UserRepository _userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        _postRepository = postRepository;
        _userRepository = userRepository;
    }

    public Optional<Post> get(int id) {
        return _postRepository.findById(id);
    }

    public Post update(Post entity) {
        return _postRepository.save(entity);
    }

    public void delete(int id) {
        _postRepository.deleteById(id);
    }

    public int count() {
        return (int) _postRepository.count();
    }

}
