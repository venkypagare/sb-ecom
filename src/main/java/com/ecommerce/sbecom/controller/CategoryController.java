package com.ecommerce.sbecom.controller;

import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    // @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // @GetMapping("/public/categories")
    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new  ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/public/category")
    // @RequestMapping(value = "/public/category", method = RequestMethod.POST)
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    // @RequestMapping(value = "/admin/categories/{categoryId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
         try {
             String status = categoryService.deleteCategory(categoryId);
             // return new ResponseEntity<>(status, HttpStatus.OK);
             // return ResponseEntity.ok(status);
             return ResponseEntity.status(HttpStatus.OK).body(status);
         } catch (ResponseStatusException e) {
             return new ResponseEntity<>(e.getReason(), e.getStatusCode());
         }
    }

    // @PutMapping("/public/category/{categoryId}")
    @RequestMapping(value = "/public/category/{categoryId}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
        categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
    }
}
