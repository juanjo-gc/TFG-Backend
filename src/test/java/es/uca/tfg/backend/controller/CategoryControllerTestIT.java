package es.uca.tfg.backend.integration.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Category;
import es.uca.tfg.backend.entity.Ticket;
import es.uca.tfg.backend.rest.CategoryDTO;
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

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class CategoryControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void createCategoryWillCreateCategory() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO("categoryTest");
        Category category = new Category("categoryTest");
        Mockito.when(_categoryRepository.save(any(Category.class))).thenReturn(category);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/createCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(categoryDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals("categoryTest", _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Category.class).get_sName());
        Mockito.verify(_categoryRepository).save(any(Category.class));
    }

    @Test
    public void updateCategoryWillUpdateExistingCategory() throws Exception{
        //given
        CategoryDTO categoryDTO = new CategoryDTO("updatedCategory");
        Category category = new Category("categoryTest");
        Mockito.when(_categoryRepository.save(any(Category.class))).thenReturn(category);
        Mockito.when(_categoryRepository.findById(1)).thenReturn(Optional.of(category));
        //when
        ResultActions resultActions = _mockMvc.perform(patch("/api/updateCategory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(categoryDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals("updatedCategory", _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Category.class).get_sName());
        Mockito.verify(_categoryRepository).save(category);
    }

    @Test
    public void deleteCategoryWillDeleteCategory() throws Exception {
        //given
        List<Ticket> aTicketsWithCategory = new ArrayList<>();
        Category category = Mockito.mock(Category.class);
        Category previouslyDeleted = Mockito.mock(Category.class);
        for(int i = 0; i < 5; i++)
                aTicketsWithCategory.add(new Ticket());
        Mockito.when(_categoryRepository.findById(1)).thenReturn(Optional.of(category));
        Mockito.when(_categoryRepository.findBy_sName("Borrada previamente")).thenReturn(Optional.of(previouslyDeleted));
        Mockito.when(_ticketRepository.findBy_category(category)).thenReturn(aTicketsWithCategory);
        Mockito.when(previouslyDeleted.get_sName()).thenReturn("Borrada previamente");
        //when
        ResultActions resultActions = _mockMvc.perform(delete("/api/deleteCategory/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_ticketRepository, Mockito.times(5)).save(any(Ticket.class));
        Mockito.verify(_categoryRepository).deleteById(1);
        for (Ticket ticket: aTicketsWithCategory)
            Assertions.assertEquals("Borrada previamente", ticket.get_category().get_sName());
    }
}
