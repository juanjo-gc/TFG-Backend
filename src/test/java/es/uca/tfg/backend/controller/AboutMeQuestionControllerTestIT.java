package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.AboutMeAnswer;
import es.uca.tfg.backend.entity.AboutMeQuestion;
import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Operation;
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

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
@RunWith(SpringRunner.class)
public class AboutMeQuestionControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void newAboutMeQuestionWillCreateQuestion() throws Exception {
        //given
        Admin admin = Mockito.mock(Admin.class);
        AboutMeQuestionAnswerDTO questionDTO = new AboutMeQuestionAnswerDTO("question", null, 1, -1, -1, -1);
        AboutMeQuestion question = new AboutMeQuestion(questionDTO.get_sQuestion());
        Mockito.when(_adminRepository.findById(1)).thenReturn(Optional.of(admin));
        Mockito.when(_aboutMeQuestionRepository.save(any(AboutMeQuestion.class))).thenReturn(question);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/newAboutMeQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(questionDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(question.get_sQuestion(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AboutMeQuestion.class).get_sQuestion());
        Mockito.verify(_operationRepository).save(any(Operation.class));
        Mockito.verify(_aboutMeQuestionRepository).save(any(AboutMeQuestion.class));
    }

    @Test
    public void updateAboutMeQuestionWillUpdateQuestion() throws Exception {
        //given
        AboutMeQuestionAnswerDTO questionDTO = new AboutMeQuestionAnswerDTO("question", null, 1, -1, 1, -1);
        Admin admin = Mockito.mock(Admin.class);
        AboutMeQuestion question = new AboutMeQuestion("test");
        Mockito.when(_adminRepository.findById(1)).thenReturn(Optional.of(admin));
        Mockito.when(_aboutMeQuestionRepository.findById(1)).thenReturn(Optional.of(question));
        Mockito.when(_aboutMeQuestionRepository.save(any())).thenReturn(question);
        //when
        ResultActions resultActions = _mockMvc.perform(patch("/api/updateAboutMeQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(questionDTO)));
        //then
        Assertions.assertEquals("question", _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AboutMeQuestion.class).get_sQuestion());
        Mockito.verify(_aboutMeQuestionRepository).save(any(AboutMeQuestion.class));

    }

    @Test
    public void deleteAboutMeQuestionWillDeleteQuestion() throws Exception {
        //given
        AboutMeQuestion question = Mockito.mock(AboutMeQuestion.class);
        List<AboutMeAnswer> answers = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            answers.add(new AboutMeAnswer());
        Mockito.when(_aboutMeQuestionRepository.findById(1)).thenReturn(Optional.of(question));
        Mockito.when(_aboutMeAnswerRepository.findBy_question(question)).thenReturn(answers);
        //when
        ResultActions resultActions = _mockMvc.perform(delete("/api/deleteAboutMeQuestion/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_aboutMeAnswerRepository, Mockito.times(5)).delete(any(AboutMeAnswer.class));
        Mockito.verify(_aboutMeQuestionRepository).deleteById(1);

    }
}
