package es.uca.tfg.backend.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.PostRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.PostDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class PostControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private PostRepository _postRepository;

    @Mock
    private UserRepository _userRepository;

    @InjectMocks
    private PostController _controller;

    @Test
    public void newPostWillCreateNewPost() {
        //given
        PostDTO dto = new PostDTO("Test post", 1);
        User user = Mockito.mock(User.class);
        Post post = new Post(dto.get_sText(), user);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_postRepository.save(any(Post.class))).thenReturn(post);
        //when
        Post newPost = _controller.newPost(dto);
        //then
        Mockito.verify(_postRepository).save(any(Post.class));
        Assertions.assertEquals("Test post", newPost.get_sText());
    }

    @Test
    public void newPostWillFailAndReturnEmtpyPostAsUserDoesNotExist() {
        //given
        PostDTO dto = new PostDTO("Test post", 1);
        User user = Mockito.mock(User.class);
        Post post = new Post(dto.get_sText(), user);
        Mockito.when(_postRepository.save(any(Post.class))).thenReturn(post);
        //when
        Post newPost = _controller.newPost(dto);
        //then
        Mockito.verify(_postRepository, Mockito.times(0)).save(any(Post.class));
        Assertions.assertNull(newPost.get_sText());
        Assertions.assertEquals(0, newPost.get_iId());
    }

    @Test
    public void getUserPostsWillReturnUserPosts() {
        //given
        User user = Mockito.mock(User.class);
        List<Post> aPosts = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aPosts.add(new Post("test post " + i, user));
        Mockito.when(_userRepository.findBy_sUsername("testusername")).thenReturn(user);
        Mockito.when(_postRepository.findBy_user(user)).thenReturn(aPosts);
        //when
        List<Post> aUserPosts = _controller.getUserPosts("testusername");
        //then
        Assertions.assertEquals(5, aUserPosts.size());
    }

    @Test
    public void getUserPostsWillFailAndReturnEmptyList() {
        //given
        User user = Mockito.mock(User.class);
        List<Post> aPosts = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aPosts.add(new Post("test post " + i, user));
        Mockito.when(_postRepository.findBy_user(user)).thenReturn(aPosts);
        //when
        List<Post> aUserPosts = _controller.getUserPosts("testusername");
        //then
        Assertions.assertEquals(Collections.emptyList(), aUserPosts);
    }

    @Test
    public void getTimelinePostsWillReturnUserPostsToShow() {
        //given
        User user = Mockito.mock(User.class);
        User user1 = Mockito.mock(User.class);
        Page page = Mockito.mock(Page.class);
        Stream auxStream = Mockito.mock(Stream.class);
        List<Post> aPosts = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            aPosts.add(new Post("test post " + i, i % 2 == 0 ? user : user1));
        Mockito.when(page.get()).thenReturn(auxStream);
        Mockito.when(auxStream.toList()).thenReturn(aPosts);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_postRepository.findTimelinePosts(user, PageRequest.of(0, 30))).thenReturn(page);
        //when
        Page aTlPosts = _controller.getTimelinePosts(1, 0);
        //then
        Mockito.verify(_postRepository).findTimelinePosts(user, PageRequest.of(0, 30));
        Assertions.assertEquals(10, aTlPosts.get().toList().size());
    }

    @Test
    public void getTimelinePostsWillFailAndReturnEmptyPageAsUserDoesNotExist() {
        //given
        User user = Mockito.mock(User.class);
        User user1 = Mockito.mock(User.class);
        Page page = Mockito.mock(Page.class);
        Stream auxStream = Mockito.mock(Stream.class);
        List<Post> aPosts = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            aPosts.add(new Post("test post " + i, i % 2 == 0 ? user : user1));
        Mockito.when(page.get()).thenReturn(auxStream);
        Mockito.when(auxStream.toList()).thenReturn(aPosts);
        Mockito.when(_postRepository.findTimelinePosts(user, PageRequest.of(0, 30))).thenReturn(page);
        //when
        Page aTlPosts = _controller.getTimelinePosts(1, 0);
        //then
        Mockito.verify(_postRepository, Mockito.times(0)).findTimelinePosts(user, PageRequest.of(0, 30));
        Assertions.assertEquals(Page.empty(), aTlPosts);
    }

    @Test
    public void setLikeWillSetLikeOnRequestedPostAsUserDidNotLikeItYet() {
        //given
        User user = Mockito.mock(User.class);
        Post post = Mockito.mock(Post.class);
        Set<User> setLikes = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_postRepository.findBy_iId(1)).thenReturn(post);
        Mockito.when(post.get_setLikes()).thenReturn(setLikes);
        Mockito.when(setLikes.contains(user)).thenReturn(false);
        //when
        boolean bHasBeenLiked = _controller.setLike(1, 1);
        //then
        Mockito.verify(_postRepository).save(post);
        Mockito.verify(setLikes).contains(user);
        Mockito.verify(setLikes).add(user);
        Assertions.assertTrue(bHasBeenLiked);
    }

    @Test
    public void setLikeWillUnsetLikeOnRequestedPostAsUserHadLikedItBefore() {
        //given
        User user = Mockito.mock(User.class);
        Post post = Mockito.mock(Post.class);
        Set<User> setLikes = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_postRepository.findBy_iId(1)).thenReturn(post);
        Mockito.when(post.get_setLikes()).thenReturn(setLikes);
        Mockito.when(setLikes.contains(user)).thenReturn(true);
        //when
        boolean bHasBeenLiked = _controller.setLike(1, 1);
        //then
        Mockito.verify(_postRepository).save(post);
        Mockito.verify(setLikes).contains(user);
        Mockito.verify(setLikes).remove(user);
        Assertions.assertFalse(bHasBeenLiked);
    }

    @Test
    public void getPostWillReturnPost() {
        //given
        Post post = Mockito.mock(Post.class);
        Mockito.when(_postRepository.findById(10)).thenReturn(Optional.of(post));
        Mockito.when(post.get_iId()).thenReturn(10);
        //when
        Post returnedPost = _controller.getPost(10);
        //then
        Assertions.assertEquals(10, returnedPost.get_iId());
    }

    @Test
    public void getPostWillFailAndReturnEmptyPost() {
        //given
        Post post = Mockito.mock(Post.class);
        Mockito.when(post.get_iId()).thenReturn(10);
        //when
        Post returnedPost = _controller.getPost(10);
        //then
        Assertions.assertEquals(0, returnedPost.get_iId());
    }

    @Test
    public void getPostLikesWillReturnLikesList() {
        //given
        Post post = Mockito.mock(Post.class);
        Set<User> aLikes = new HashSet<>();
        for(int i = 0; i < 10; i++)
            aLikes.add(new User());
        Mockito.when(post.get_setLikes()).thenReturn(aLikes);
        Mockito.when(_postRepository.findById(10)).thenReturn(Optional.of(post));
        //when
        List<User> aPostLikes = _controller.getPostLikes(10);
        //then
        Assertions.assertEquals(10, aPostLikes.size());
    }

    @Test
    public void getPostLikesWillFailAndReturnEmptyListAsPostDoesNotExist() {
        //given
        Post post = Mockito.mock(Post.class);
        Set<User> aLikes = new HashSet<>();
        for(int i = 0; i < 10; i++)
            aLikes.add(new User());
        Mockito.when(post.get_setLikes()).thenReturn(aLikes);
        //when
        List<User> aPostLikes = _controller.getPostLikes(10);
        //then
        Assertions.assertEquals(Collections.emptyList(), aPostLikes);
    }

    @Test
    public void getRespliesWillReturnRepliesList() {
        //given
        Post post = Mockito.mock(Post.class);
        Page<Post> replies = Mockito.mock(Page.class);
        Stream<Post> auxStream = Mockito.mock(Stream.class);
        Set<User> aReplies = new HashSet<>();
        for(int i = 0; i < 10; i++)
            aReplies.add(new User());
        Mockito.when(_postRepository.findPostReplies(post, PageRequest.of(0, 5))).thenReturn(replies);
        Mockito.when(_postRepository.findById(10)).thenReturn(Optional.of(post));
        Mockito.when(replies.getTotalElements()).thenReturn((long) aReplies.size());
        //when
        Page<Post> aPostReplies = _controller.getPostReplies(10, 0);
        //then
        Assertions.assertEquals(10, aPostReplies.getTotalElements());
    }

    @Test
    public void getRespliesWillFailAndReturnEmptyListAsPostDoesNotExist() {
        //given
        Post post = Mockito.mock(Post.class);
        Page<Post> replies = Mockito.mock(Page.class);
        Stream<Post> auxStream = Mockito.mock(Stream.class);
        Set<User> aReplies = new HashSet<>();
        for(int i = 0; i < 10; i++)
            aReplies.add(new User());
        Mockito.when(_postRepository.findPostReplies(post, PageRequest.of(0, 5))).thenReturn(replies);
        Mockito.when(replies.getTotalElements()).thenReturn((long) aReplies.size());
        //when
        Page<Post> aPostReplies = _controller.getPostReplies(10, 0);
        //then
        Assertions.assertEquals(0, aPostReplies.getTotalElements());
    }

    @Test
    public void newReplyWillCreateNewReplyToPost() {
        //given
        User user = Mockito.mock(User.class);
        Post post = Mockito.mock(Post.class);
        Post reply = Mockito.mock(Post.class);
        PostDTO dto = new PostDTO("Test post", 1);
        Set<Post> aReplies = new HashSet<>();
        for(int i = 0; i < 5; i++)
            aReplies.add(new Post());
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_postRepository.findById(10)).thenReturn(Optional.of(post));
        Mockito.when(reply.get_sText()).thenReturn(dto.get_sText());
        Mockito.when(post.get_setReplies()).thenReturn(aReplies);
        Mockito.when(_postRepository.save(any(Post.class))).thenReturn(reply);
        //when
        Post newReply = _controller.newReply(dto, 10);
        //then
        Assertions.assertEquals("Test post", newReply.get_sText());
    }
}
