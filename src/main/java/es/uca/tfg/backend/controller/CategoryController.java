package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Category;
import es.uca.tfg.backend.entity.Person;
import es.uca.tfg.backend.entity.Ticket;
import es.uca.tfg.backend.repository.AdminRepository;
import es.uca.tfg.backend.repository.CategoryRepository;
import es.uca.tfg.backend.repository.PersonRepository;
import es.uca.tfg.backend.repository.TicketRepository;
import es.uca.tfg.backend.rest.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryRepository _categoryRepository;
    @Autowired
    private PersonRepository _personRepository;
    @Autowired
    private TicketRepository _ticketRepository;

    @GetMapping("/getAllCategories")
    public List<Category> getAllCategories() {
        return _categoryRepository.findAll();
    }

    @PostMapping("/createCategory")
    public Category createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = _categoryRepository.save(new Category(categoryDTO.get_sName()));
        return category;
    }

    @PatchMapping("/updateCategory/{id}")
    public Category updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable("id") int iCategoryId) {
        Optional<Category> optionalCategory = _categoryRepository.findById(iCategoryId);
        if(optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.set_sName(categoryDTO.get_sName());
            return _categoryRepository.save(category);
        } else {
            return new Category();
        }
    }

    @DeleteMapping("/deleteCategory/{id}")
    public void deleteCategory(@PathVariable("id") int iCategoryId) {
        Optional<Category> checkIfShouldBeDeleted = _categoryRepository.findById(iCategoryId);
        if(checkIfShouldBeDeleted.isPresent()) {
            if(!Objects.equals(checkIfShouldBeDeleted.get().get_sName(), "Borrada previamente")) {
                Optional<Category> newCategory = _categoryRepository.findBy_sName("Borrada previamente");
                List<Ticket> aTicketsWithThisCategory = _ticketRepository.findBy_category(_categoryRepository.findById(iCategoryId).get());
                for (Ticket ticket : aTicketsWithThisCategory) {
                    ticket.set_category(newCategory.get());
                    _ticketRepository.save(ticket);
                }
            }
            _categoryRepository.deleteById(iCategoryId);
        }
    }
}
