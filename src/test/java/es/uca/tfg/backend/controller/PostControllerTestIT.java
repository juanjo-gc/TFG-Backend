package es.uca.tfg.backend.integration.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.rest.PostDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@RunWith(SpringRunner.class)
public class PostControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void willCreateNewPost() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findBy_iId(anyInt())).thenReturn(user);
        Mockito.when(user.get_iId()).thenReturn(1);
        PostDTO postDTO = new PostDTO("test", user.get_iId());
        //when
        ResultActions response = _mockMvc.perform(post("/api/newPost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(postDTO)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_postRepository).save(any(Post.class));
    }

    @Test
    public void getUserPostsWillReturnAtLeastOnePost() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Post post = Mockito.mock(Post.class);
        Post.PostComparator comparator = Mockito.mock(Post.PostComparator.class);
        ArrayList<Post> aPosts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            aPosts.add(post);
        }
        Mockito.when(_userRepository.findBy_sUsername(anyString())).thenReturn(user);
        Mockito.when(_postRepository.findBy_user(user)).thenReturn(aPosts);
        Mockito.when(comparator.compare(any(), any())).thenReturn((int) Math.floor(Math.random()));
        //when
        ResultActions response = _mockMvc.perform(get("/api/getUserPosts/user"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void setLikeWhenPostIsNotLiked() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Post post = Mockito.mock(Post.class);
        Set setLikes = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(anyInt())).thenReturn(user);
        Mockito.when(_postRepository.findBy_iId(anyInt())).thenReturn(post);
        Mockito.when(post.get_setLikes()).thenReturn(setLikes);
        Mockito.when(setLikes.contains(user)).thenReturn(false);
        //when
        ResultActions response = _mockMvc.perform(post("/api/setLike/1/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertTrue(_objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Boolean.class));
    }

    @Test
    public void setLikeWhenPostIsLiked() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Post post = Mockito.mock(Post.class);
        Set setLikes = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(anyInt())).thenReturn(user);
        Mockito.when(_postRepository.findBy_iId(anyInt())).thenReturn(post);
        Mockito.when(post.get_setLikes()).thenReturn(setLikes);
        Mockito.when(setLikes.contains(user)).thenReturn(true);
        Mockito.when(_postRepository.save(post)).thenReturn(post);
        //when
        ResultActions response = _mockMvc.perform(post("/api/setLike/1/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertTrue(!_objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Boolean.class));
    }

    @Test
    public void getPostWillSuccess() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        Post post = new Post("test", user);
        Optional<Post> optionalPost = Optional.of(post);
        Mockito.when(_postRepository.findById(anyInt())).thenReturn(optionalPost);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getPost/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals("test", _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Post.class).get_sText());
    }

    @Test
    public void getLikesWillReturnSomeUsers() throws Exception {
        //given
        Set<User> aUsers = new HashSet<>();
        for(int i = 0; i < 3; i++) {
            aUsers.add(new User("example" + (i+1) + "@gmail.com", "password", "username" + (i+1), "description", "user", "name", new Date(), new Province()));
        }
        Post post = Mockito.mock(Post.class);
        Optional<Post> optionalPost = Optional.of(post);
        Mockito.when(_postRepository.findById(anyInt())).thenReturn(optionalPost);
        Mockito.when(post.get_setLikes()).thenReturn(aUsers);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getLikes/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(3, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void getRepliesWillReturnSomePosts() throws Exception {
        //given
        Set<Post> aPosts = new HashSet<>();
        for(int i = 0; i < 3; i++) {
            aPosts.add(new Post("test" + (i+1),
                    new User("example" + (i+1) + "@gmail.com", "password", "username" + (i+1), "description", "user", "name", new Date(), new Province())));
        }
        Post post = Mockito.mock(Post.class);
        Optional<Post> optionalPost = Optional.of(post);
        Mockito.when(_postRepository.findById(anyInt())).thenReturn(optionalPost);
        Mockito.when(post.get_setReplies()).thenReturn(aPosts);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getLikes/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(3, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void newReplyWillCreatePost() throws Exception {
        //given
        PostDTO postDTO = new PostDTO("reply", 1);
        User user = Mockito.mock(User.class);
        Post reply = new Post(postDTO.get_sText(), user);
        Post post = Mockito.mock(Post.class);
        Optional<Post> optionalPost = Optional.of(post);
        Mockito.when(_userRepository.findBy_iId(anyInt())).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(anyInt())).thenReturn(user);
        Mockito.when(_postRepository.findById(anyInt())).thenReturn(optionalPost);
        //when
        ResultActions response = _mockMvc.perform(post("/api/newReply/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(postDTO)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_postRepository, Mockito.times(2)).save(any(Post.class));

        //System.out.println("Respuesta: " + response.andReturn().getResponse().getContentAsString());
        //Assertions.assertEquals("reply", _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Post.class).get_sText());
    }
}
