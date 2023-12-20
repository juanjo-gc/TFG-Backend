package es.uca.tfg.backend.integration.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.AboutMeAnswer;
import es.uca.tfg.backend.entity.AboutMeQuestion;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.rest.AboutMeQuestionAnswerDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
@RunWith(SpringRunner.class)
public class AboutMeAnswerControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void createOrModifyNewAnswerWillCreateAnswer() throws Exception {
        //given
        AboutMeQuestionAnswerDTO answerDTO = new AboutMeQuestionAnswerDTO("Question", "Answer", -1, 1, 1, -1);
        User user = Mockito.mock(User.class);
        AboutMeQuestion question = Mockito.mock(AboutMeQuestion.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_aboutMeQuestionRepository.findById(1)).thenReturn(Optional.of(question));

        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/createOrModifyAboutMeAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(answerDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(true, _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), boolean.class));
        Mockito.verify(_aboutMeAnswerRepository).save(any(AboutMeAnswer.class));
    }

    @Test
    public void createOrModifyNewAnswerWillModifyAnswer() throws Exception {
        //given
        AboutMeQuestionAnswerDTO answerDTO = new AboutMeQuestionAnswerDTO("Question", "Answer", -1, 1, 1, 1);
        User user = Mockito.mock(User.class);
        AboutMeQuestion question = Mockito.mock(AboutMeQuestion.class);
        AboutMeAnswer answer = Mockito.mock(AboutMeAnswer.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_aboutMeQuestionRepository.findById(1)).thenReturn(Optional.of(question));
        Mockito.when(_aboutMeAnswerRepository.findByUserAndQuestion(user, question)).thenReturn(Optional.of(answer));
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/createOrModifyAboutMeAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(answerDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(true, _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), boolean.class));
        Mockito.verify(answer).set_sAnswer(anyString());
        Mockito.verify(_aboutMeAnswerRepository).save(any(AboutMeAnswer.class));
    }

    @Test
    public void softDeleteAboutMeAnswerWillDeleteAnswer() throws Exception {
        //given
        AboutMeAnswer answer = Mockito.mock(AboutMeAnswer.class);
        Mockito.when(_aboutMeAnswerRepository.findById(1)).thenReturn(Optional.of(answer));
        //when
        ResultActions resultActions = _mockMvc.perform(patch("/api/softDeleteAboutMeAnswer/1"));
        //then
        Mockito.verify(answer).set_sAnswer("");
        Mockito.verify(_aboutMeAnswerRepository).save(answer);
    }

    @Test
    public void getUserAnswersWithExistingUser() throws Exception {
        //given
        List<AboutMeAnswer> answers = new ArrayList<>();
        //for(int i = 0; i < 5; i++)
        //    answers.add(Mockito.mock(AboutMeAnswer.class));
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_aboutMeAnswerRepository.findBy_user(user)).thenReturn(answers);
        //when
        ResultActions resultActions = _mockMvc.perform(get("/api/getUserAnswers/1"));
        //then
        resultActions.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(answers, _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), List.class));
        Mockito.verify(_userRepository).findById(1);
        Mockito.verify(_aboutMeAnswerRepository).findBy_user(user);
    }
}
