package com.ecommerce.sbecom.controller;

import com.ecommerce.sbecom.payload.CategoryRequestDTO;
import com.ecommerce.sbecom.payload.CategoryResponseDTO;
import com.ecommerce.sbecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CategoryResponseDTO> getAllCategories() {
        CategoryResponseDTO categoryResponseDTO = categoryService.getAllCategories();
        return new  ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    // @RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryRequestDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        CategoryRequestDTO savedCategoryDTO = categoryService.createCategory(categoryRequestDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    // @RequestMapping(value = "/admin/categories/{categoryId}", method = RequestMethod.DELETE)
    public ResponseEntity<CategoryRequestDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryRequestDTO categoryRequestDTO = categoryService.deleteCategory(categoryId);
             // return new ResponseEntity<>(categoryRequestDTO, HttpStatus.OK);
             // return ResponseEntity.ok(categoryRequestDTO);
             return ResponseEntity.status(HttpStatus.OK).body(categoryRequestDTO);
    }

    // @PutMapping("/public/categories/{categoryId}")
    @RequestMapping(value = "/public/categories/{categoryId}", method = RequestMethod.PUT)
    public ResponseEntity<CategoryRequestDTO> updateCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO
            , @PathVariable Long categoryId) {
        CategoryRequestDTO responseDTO = categoryService.updateCategory(categoryRequestDTO, categoryId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
