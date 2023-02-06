package com.br.productapi.service;

import com.br.productapi.config.exeception.SuccesResponse;
import com.br.productapi.config.exeception.ValidationException;
import com.br.productapi.dto.SupplierRequest;
import com.br.productapi.dto.SupplierResponse;
import com.br.productapi.model.Supplier;
import com.br.productapi.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductService productService;

   /** public Supplier findById(Integer id){
        return supplierRepository
                .findById(id).orElseThrow(()-> new ValidationException("There is no Supplier for the give ID"));
    }**/

    public List<SupplierResponse> findAll(){
        return supplierRepository
                .findAll()
                .stream()
                .map(SupplierResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public SupplierResponse findByIdResponse(Integer id){
            return SupplierResponse.of(findById(id));
    }

    public Supplier findById(Integer id){
        validateInformedId(id);
        if(isEmpty(id)){
            throw new ValidationException("The category ID was not informed");
        }
        return supplierRepository
                .findById(id)
                .orElseThrow(()-> new ValidationException("There is no Supplier for the give ID"));
    }
    public List<SupplierResponse> findByName(String name){
        if(isEmpty(name)){
            throw new ValidationException("The supplier description must be informed.");
        }
        return supplierRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                //.map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }

    public SupplierResponse save(SupplierRequest request){
        validateCategoryNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse update(SupplierRequest request, Integer id){
        validateCategoryNameInformed(request);
        validateInformedId(id);
        var supplier = supplierRepository.save(Supplier.of(request));
        supplier.setId(id);
        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    public void validateCategoryNameInformed(SupplierRequest request) {
        if(ObjectUtils.isEmpty(request.getName())){
            throw new ValidationException("The supplier name was not informed");
        }
    }

    public SuccesResponse delete(Integer id){
        validateInformedId(id);
        if(productService.existsBySupplierId(id)){
            throw new ValidationException("You can not delete this supplier because it's already defined by a product ");
        }
        supplierRepository.deleteById(id);
        return SuccesResponse.create("The supplier was deleted.");
    }
    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("The supplier's ID must be informed");
        }
    }
}
