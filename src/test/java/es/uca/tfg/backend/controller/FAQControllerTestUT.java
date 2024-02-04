package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.FAQ;
import es.uca.tfg.backend.repository.AdminRepository;
import es.uca.tfg.backend.repository.FAQRepository;
import es.uca.tfg.backend.repository.OperationRepository;
import es.uca.tfg.backend.rest.FaqDTO;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class FAQControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private FAQRepository _faqRepository;
    @Mock
    private AdminRepository _adminRepository;
    @Mock
    private OperationRepository _operationRepository;

    @InjectMocks
    private FAQController _controller;

    @Mock
    private Admin _admin;

    @Mock
    private FAQ _faq;

    @Test
    public void newFaqWillCreateFaq() {
        //given
        FaqDTO dto = new FaqDTO("Question test", "Answer test");
        Mockito.when(_adminRepository.findById(1)).thenReturn(Optional.of(_admin));
        Mockito.when(_faqRepository.save(any(FAQ.class))).thenReturn(_faq);
        Mockito.when(_faq.get_iId()).thenReturn(10);
        //when
        FAQ faq = _controller.newFaq(dto, 1);
        //then
        Mockito.verify(_faqRepository).save(any(FAQ.class));
        Assertions.assertEquals(10, faq.get_iId());
    }

    @Test
    public void newFaqWillFailAsAdminDoesNotExist() {
        //given
        FaqDTO dto = new FaqDTO("Question test", "Answer test");
        Mockito.when(_faqRepository.save(any(FAQ.class))).thenReturn(_faq);
        Mockito.when(_faq.get_iId()).thenReturn(10);
        //when
        FAQ faq = _controller.newFaq(dto, 1);
        //then
        Mockito.verify(_faqRepository, Mockito.times(0)).save(any(FAQ.class));
        Assertions.assertEquals(0, faq.get_iId());
    }

    @Test
    public void updateFAQWillUpdateExistingFAQ() {
        //given
        FaqDTO dto = new FaqDTO("Question test updated", "Answer test updated");
        Mockito.when(_faqRepository.findById(1)).thenReturn(Optional.of(_faq));
        Mockito.when(_faqRepository.save(any(FAQ.class))).thenReturn(_faq);
        //when
        FAQ faq = _controller.editFaq(dto, 1);
        //then
        Mockito.verify(_faq).set_sQuestion(anyString());
        Mockito.verify(_faq).set_sAnswer(anyString());
    }

    @Test
    public void updateFAQWillFailAsFAQDoesNotExist() {
        //given
        FaqDTO dto = new FaqDTO("Question test updated", "Answer test updated");
        Mockito.when(_faqRepository.save(any(FAQ.class))).thenReturn(_faq);
        //when
        FAQ faq = _controller.editFaq(dto, 1);
        //then
        Mockito.verify(_faq, Mockito.times(0)).set_sQuestion(anyString());
        Mockito.verify(_faq, Mockito.times(0)).set_sAnswer(anyString());
        Assertions.assertEquals(0, faq.get_iId());
    }
}
