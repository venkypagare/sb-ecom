package com.ecommerce.sbecom.controller;

import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
public class CategoryController {
    // @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new  ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/api/admin/category")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
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
}
