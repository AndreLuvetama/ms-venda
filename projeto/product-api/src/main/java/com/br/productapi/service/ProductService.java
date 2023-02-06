package com.br.productapi.service;

import com.br.productapi.config.exeception.SuccesResponse;
import com.br.productapi.config.exeception.ValidationException;
import com.br.productapi.dto.*;
import com.br.productapi.model.Product;
import com.br.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {
    private static final Integer ZERO=0;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;

    public ProductResponse save(ProductRequest request){
        validateProductDataInformed(request);
        validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    public ProductResponse update(ProductRequest request, Integer id){
        validateProductDataInformed(request);
        validateCategoryAndSupplierInformed(request);
        validateInformedId(id);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = Product.of(request, supplier, category);
        product.setId(id);
        productRepository.save(product);
        return ProductResponse.of(product);
    }

    private void validateProductDataInformed(ProductRequest request){
        if(isEmpty(request.getName())){
            throw new ValidationException("The product's name was not informed.");
        }
        if(isEmpty(request.getQuantityAvailable())){
            throw new ValidationException("The product's quantity was not informed.");
        }
        if(request.getQuantityAvailable() <=ZERO ){
            throw new ValidationException("The quantity should not be less or equal to zero");
        }
    }

    public ProductResponse findByIdResponse(Integer id){
        return ProductResponse.of(findById(id));
    }

    public void validateCategoryAndSupplierInformed(ProductRequest request) {
        if(isEmpty(request.getCategoryId())){
            throw new ValidationException("The category ID was not informed.");
        }
        if(isEmpty(request.getSupplierId())){
            throw new ValidationException("The supplier ID was not informed.");
        }
    }


    public List<ProductResponse> findAll(){
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public Product findById(Integer id){
        validateInformedId(id);
        return productRepository
                .findById(id)
                .orElseThrow(()-> new ValidationException("There is no product for the give ID"));
    }
    public List<ProductResponse> findByName(String name){
        if(isEmpty(name)){
            throw new ValidationException("The product description must be informed.");
        }
        return productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId){
        if(isEmpty(supplierId)){
            throw new ValidationException("The product's  supplier ID must be informed.");
        }
        return productRepository
                .findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId){
        if(isEmpty(categoryId)){
            throw new ValidationException("The product's  category ID name must be informed.");
        }
        return productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public Boolean existsByCategoryId(Integer categoryId){
        return productRepository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId){
        return productRepository.existsBySupplierId(supplierId);
    }

    public SuccesResponse delete(Integer id){
        validateInformedId(id);
        productRepository.deleteById(id);
        return SuccesResponse.create("The product was deleted.");
    }
    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("The product's ID must be informed");
        }
    }
}
