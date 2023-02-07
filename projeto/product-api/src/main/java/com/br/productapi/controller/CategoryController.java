package com.br.productapi.controller;


import com.br.productapi.config.exeception.SuccesResponse;
import com.br.productapi.dto.CategoryRequest;
import com.br.productapi.dto.CategoryResponse;
import com.br.productapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request){
        return categoryService.save(request);
    }
    @GetMapping
    public List<CategoryResponse> findAll(){
        return categoryService.findAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id){
        return categoryService.findByIdResponse(id);
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description){
        return categoryService.findByDescription((description));
    }

    @DeleteMapping("{id}")
    public SuccesResponse delete(@PathVariable Integer id){
        return categoryService.delete(id);
    }

    @PutMapping("{id}")
    public CategoryResponse update(@RequestBody CategoryRequest request, @PathVariable Integer id){
        return categoryService.update(request, id);
    }

}
