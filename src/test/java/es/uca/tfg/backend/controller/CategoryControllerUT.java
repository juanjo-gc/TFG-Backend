package es.uca.tfg.backend.controller;


import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Category;
import es.uca.tfg.backend.entity.Ticket;
import es.uca.tfg.backend.repository.CategoryRepository;
import es.uca.tfg.backend.repository.PersonRepository;
import es.uca.tfg.backend.repository.TicketRepository;
import es.uca.tfg.backend.rest.CategoryDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private CategoryRepository _categoryRepository;
    @Mock
    private PersonRepository _personRepository;
    @Mock
    private TicketRepository _ticketRepository;

    @InjectMocks
    private CategoryController _controller;

    private Category _category = new Category("Test category");

    @Test
    public void createCategoryWillCreateNewCategory() {
        //given
        CategoryDTO dto = new CategoryDTO("Test category");
        Mockito.when(_categoryRepository.save(any(Category.class))).thenReturn(new Category(dto.get_sName()));
        //when
        Category category = _controller.createCategory(dto);
        //then
        Mockito.verify(_categoryRepository).save(any(Category.class));
        Assertions.assertEquals(category.get_sName(), "Test category");
    }

    @Test
    public void updateCategoryWillUpdateExistingCategory() {
        //given
        CategoryDTO dto = new CategoryDTO("Test category updated");
        Mockito.when(_categoryRepository.findById(1)).thenReturn(Optional.of(_category));
        Mockito.when(_categoryRepository.save(_category)).thenReturn(_category);
        //when
        Category category = _controller.updateCategory(dto, 1);
        //then
        Mockito.verify(_categoryRepository).save(any(Category.class));
        Assertions.assertEquals(category.get_sName(), "Test category updated");
    }

    @Test
    public void updateCategoryWillFailAsCategoryDoesNotExist() {
        //given
        CategoryDTO dto = new CategoryDTO("Test category updated");
        Mockito.when(_categoryRepository.save(_category)).thenReturn(_category);
        //when
        Category category = _controller.updateCategory(dto, -1);
        //then
        Mockito.verify(_categoryRepository, Mockito.times(0)).save(any(Category.class));
        Assertions.assertEquals(0, category.get_iId());
    }

    @Test
    public void deleteCategoryWillDeleteExistingCategory() {
        //given
        List<Ticket> aTicketsWithCategory = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aTicketsWithCategory.add(new Ticket());
        Category previouslyDeleted = new Category("Borrada previamente");
        Mockito.when(_categoryRepository.findBy_sName("Borrada previamente")).thenReturn(Optional.of(previouslyDeleted));
        Mockito.when(_categoryRepository.findById(1)).thenReturn(Optional.of(_category));
        Mockito.when(_categoryRepository.findBy_sName("Borrada previamente")).thenReturn(Optional.of(previouslyDeleted));
        Mockito.when(_ticketRepository.findBy_category(_category)).thenReturn(aTicketsWithCategory);
        //when
        _controller.deleteCategory(1);
        //then
        Mockito.verify(_categoryRepository).deleteById(1);
        Mockito.verify(_ticketRepository, Mockito.times(5)).save(any(Ticket.class));
        for(Ticket ticket: aTicketsWithCategory) {
            Assertions.assertEquals("Borrada previamente", ticket.get_category().get_sName());
        }
    }
}
