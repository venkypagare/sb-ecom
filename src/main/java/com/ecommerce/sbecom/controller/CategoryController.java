package com.ecommerce.sbecom.controller;

import com.ecommerce.sbecom.config.AppConstants;
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
    public ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
    ) {
        CategoryResponseDTO categoryResponseDTO = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new  ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/echo")
    public ResponseEntity<String> echoMessage(@RequestParam(name = "message", required = false) String message) {
        return new ResponseEntity<>("Echoed Message: "+ message, HttpStatus.OK);
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
