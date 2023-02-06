package com.br.productapi.controller;


import com.br.productapi.config.exeception.SuccesResponse;
import com.br.productapi.dto.CategoryRequest;
import com.br.productapi.dto.CategoryResponse;
import com.br.productapi.dto.SupplierRequest;
import com.br.productapi.dto.SupplierResponse;
import com.br.productapi.repository.SupplierRepository;
import com.br.productapi.service.CategoryService;
import com.br.productapi.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public SupplierResponse save(@RequestBody SupplierRequest request){
        return supplierService.save(request);
    }

    @GetMapping
    public List<SupplierResponse> findAll(){
        return supplierService.findAll();
    }
    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable Integer id){
        return supplierService.findByIdResponse(id);
    }
    @GetMapping("name/{name}")
    public List<SupplierResponse> findByDescription(@PathVariable String name){
        return supplierService.findByName((name));
    }
    @DeleteMapping("{id}")
    public SuccesResponse delete(@PathVariable Integer id){
        return supplierService.delete(id);
    }
    @PutMapping("{id}")
    public SupplierResponse update(@RequestBody SupplierRequest request, @PathVariable Integer id){
        return supplierService.update(request, id);
    }

}
