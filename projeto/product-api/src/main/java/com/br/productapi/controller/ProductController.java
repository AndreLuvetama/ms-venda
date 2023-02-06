package com.br.productapi.controller;


import com.br.productapi.config.exeception.SuccesResponse;
import com.br.productapi.config.exeception.ValidationException;
import com.br.productapi.dto.*;
import com.br.productapi.repository.ProductRepository;
import com.br.productapi.service.CategoryService;
import com.br.productapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest request){
        return productService.save(request);
    }

    @GetMapping
    public List<ProductResponse> findAll(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Integer id){
        return productService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByName(@PathVariable String name){
        return productService.findByName((name));
    }
    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable Integer categoryId){
        return productService.findByCategoryId((categoryId));
    }
    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findByName(@PathVariable Integer supplierId){
        return productService.findBySupplierId((supplierId));
    }

    @DeleteMapping("{id}")
    public SuccesResponse delete(@PathVariable Integer id){
        return productService.delete(id);
    }
    @PutMapping("{id}")
    public ProductResponse update(@RequestBody ProductRequest request, @PathVariable Integer id){
        return productService.update(request, id);
    }
}
