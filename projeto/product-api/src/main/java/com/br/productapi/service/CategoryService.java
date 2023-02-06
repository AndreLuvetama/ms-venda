package com.br.productapi.service;

import com.br.productapi.config.exeception.SuccesResponse;
import com.br.productapi.config.exeception.ValidationException;
import com.br.productapi.dto.CategoryRequest;
import com.br.productapi.dto.CategoryResponse;
import com.br.productapi.model.Category;
import com.br.productapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static org.springframework.util.ObjectUtils.isEmpty;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;

    public CategoryResponse findByIdResponse(Integer id){
        return CategoryResponse.of(findById(id));
    }
    public List<CategoryResponse> findAll(){
        return categoryRepository
                .findAll()
                .stream()
                .map(CategoryResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }
    public List<CategoryResponse> findByDescription(String description){
        if(isEmpty(description)){
          throw new ValidationException("The category description must be informed.");
        }
        return categoryRepository
                .findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public Category findById(Integer id){
        validateInformedId(id);
        if(isEmpty(id)){
            throw new ValidationException("The category ID was not informed");
        }
        return categoryRepository
                .findById(id).orElseThrow(()-> new ValidationException("There is no Supplier for the give ID"));
    }
    public CategoryResponse save(CategoryRequest request){
        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }
    public CategoryResponse update(CategoryRequest request, Integer id){
        validateCategoryNameInformed(request);
        validateInformedId(id);
        var category = categoryRepository.save(Category.of(request));
        category.setId(id);
        return CategoryResponse.of(category);
    }


    public void validateCategoryNameInformed(CategoryRequest request) {
        if(ObjectUtils.isEmpty(request.getDescription())){
            throw new ValidationException("The category  description was not informed");
        }
    }

    public SuccesResponse delete(Integer id){
        validateInformedId(id);
        if(productService.existsBySupplierId(id)){
            throw new ValidationException("You can not delete this category because it's already defined by a product ");
        }
        categoryRepository.deleteById(id);
        return SuccesResponse.create("The category was deleted.");

    }
    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("The category's ID must be informed");
        }
    }


}
