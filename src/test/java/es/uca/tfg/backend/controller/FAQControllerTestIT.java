package es.uca.tfg.backend.integration.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.FAQ;
import es.uca.tfg.backend.entity.Operation;
import es.uca.tfg.backend.rest.FaqDTO;
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

import java.util.Optional;

@WebMvcTest
@RunWith(SpringRunner.class)
public class FAQControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }


    @Test
    public void newFAQWillCreateFAQ() throws Exception {
        //given
        Admin admin = Mockito.mock(Admin.class);
        FaqDTO faqDTO = new FaqDTO("questionTest", "answerTest");
        FAQ faq = new FAQ(faqDTO.get_sQuestion(), faqDTO.get_sAnswer());
        Mockito.when(_adminRepository.findById(1)).thenReturn(Optional.of(admin));
        Mockito.when(_faqRepository.save(any(FAQ.class))).thenReturn(faq);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/newFAQ/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(faqDTO)));
        //then
        resultActions.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(faq.get_sQuestion(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), FAQ.class).get_sQuestion());
        Assertions.assertEquals(faq.get_sAnswer(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), FAQ.class).get_sAnswer());
        Mockito.verify(_faqRepository).save(any(FAQ.class));
        Mockito.verify(_operationRepository).save(any(Operation.class));
    }

    @Test
    public void updateFAQWillUpdateExistingFAQ() throws Exception {
        //given
        FaqDTO faqDTO = new FaqDTO("updatedQuestion", "updatedAnswer");
        FAQ faq = new FAQ("questionTest", "answerTest");
        Mockito.when(_faqRepository.findById(1)).thenReturn(Optional.of(faq));
        //when
        ResultActions resultActions = _mockMvc.perform(patch("/api/updateFAQ/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(faqDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals("updatedQuestion", _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), FAQ.class).get_sQuestion());
        Assertions.assertEquals("updatedAnswer", _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), FAQ.class).get_sAnswer());
        Mockito.verify(_faqRepository).save(any(FAQ.class));
    }
}
