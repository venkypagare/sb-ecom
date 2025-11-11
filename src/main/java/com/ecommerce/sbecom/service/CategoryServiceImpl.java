package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.exceptions.APIException;
import com.ecommerce.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.payload.CategoryRequestDTO;
import com.ecommerce.sbecom.payload.CategoryResponseDTO;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        // Case 1: No data at all in DB
        if (categoryPage.getTotalElements() == 0) {
            throw new APIException("No categories created till now.");
        }

        // Case 2: Page number out of range
        if (pageNumber >= categoryPage.getTotalPages()) {
            throw new APIException("Requested page number is out of range. Total pages available: "
                    + categoryPage.getTotalPages());
        }

        // Case 3: Empty content for current page (rare but safe)
        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new APIException("No categories found for the requested page number.");
        }
        List<CategoryRequestDTO> categoriesDTO = categories.stream()
                .map(category -> modelMapper.map(category, CategoryRequestDTO.class)).toList();
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryResponse(categoriesDTO);
        categoryResponseDTO.setPageNumber(categoryPage.getNumber());
        categoryResponseDTO.setPageSize(categoryPage.getSize());
        categoryResponseDTO.setTotalElements(categoryPage.getTotalElements());
        categoryResponseDTO.setTotalPages(categoryPage.getTotalPages());
        categoryResponseDTO.setLastPage(categoryPage.isLast());
        return categoryResponseDTO;
    }

    @Override
    public CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category categoryEntity = modelMapper.map(categoryRequestDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(categoryRequestDTO.getCategoryName());
        if (categoryFromDB != null) {
            throw new APIException("Category with name " + categoryRequestDTO.getCategoryName() + " already exists !!");
        }
        Category savedCategory = categoryRepository.save(categoryEntity);
        return modelMapper.map(savedCategory,  CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId) {
        Category updatedCategory = categoryRepository.findByCategoryName(categoryRequestDTO.getCategoryName());
        if (updatedCategory != null) {
            throw new APIException("Category with name " + categoryRequestDTO.getCategoryName() + " already exists !!");
        }
        categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        Category savedCategory = modelMapper.map(categoryRequestDTO, Category.class);
        savedCategory.setCategoryId(categoryId);
        categoryRepository.save(savedCategory);
        return modelMapper.map(savedCategory,  CategoryRequestDTO.class);
    }
}
