package com.br.productapi.service;

import com.br.productapi.config.exeception.SuccesResponse;
import com.br.productapi.config.exeception.ValidationException;
import com.br.productapi.dto.*;
import com.br.productapi.model.Product;
import com.br.productapi.repository.ProductRepository;
import com.br.productapi.sales.client.SalesClient;
import com.br.productapi.sales.dto.SalesConfirmationDTO;
import com.br.productapi.sales.enums.SalesStatus;
import com.br.productapi.sales.rabbitmq.SalesConfirmationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
public class ProductService {
    private static final Integer ZERO=0;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    @Lazy
    private SupplierService supplierService;
    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private SalesClient salesClient;

    @Autowired
    private SalesConfirmationSender salesConfirmationSender;
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
    public void updateProductStock(ProductStockDTO product){
        try{
            validateStockUpdateData(product); //Valida os dados
            updateStock(product);// Faz atualização

        }catch (Exception ex){
            log.error("Error while trying to update stock for message with error: {}", ex.getMessage(), ex);
            var rejectedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.REJECTED);
            salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);
        }
    }

    @Transactional
    private void updateStock(ProductStockDTO product){
        var productsForUpdate = new ArrayList<Product>();
        product.getProducts().forEach(salesProduct -> {
            var existingProduct = findById(salesProduct.getProductId());
            validateQuantityInStock(salesProduct, existingProduct);

            //Se o produto passar na validação salvamos
            existingProduct.updateStock(salesProduct.getQuantity());
            productsForUpdate.add(existingProduct);
        });

        if(!isEmpty(productsForUpdate)){
            productRepository.saveAll(productsForUpdate);
            var approvedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APPROVED);
            //Enviamos a mensagem de confirmação
            salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);

        }
    }


    private void validateStockUpdateData(ProductStockDTO product){
        if(isEmpty(product) || isEmpty(product.getSalesId())){
            throw new ValidationException("The product data or sales ID must be informed");
        } if(isEmpty(product.getProducts())){
            throw new ValidationException("The sales' products must be informed.");
        }
        product.getProducts()
                .forEach(salesProduct ->{
                    if(isEmpty(salesProduct.getQuantity()) || isEmpty(salesProduct.getProductId())){
                        throw new ValidationException("The productID and the quantity must be informed.");
                    }
                });
    }


    private void validateQuantityInStock(ProductQuantityDTO salesProduct, Product existingProduct){
        //Se o produto é maior do que existe, lançamos uma excessão
        if(salesProduct.getQuantity() > existingProduct.getQuantityAvailable()){
            throw new ValidationException(
                    String.format("The product %s is out of stock", existingProduct.getId()));
        }

    }

    public ProductSalesResponse findProductSales(Integer id){
        var product = findById(id);
        try{
            //Recuperamos os produtos
            var sales = salesClient.findSalesByProductId(product.getId())
                    .orElseThrow(() -> new ValidationException("The Sales was not found by this product."));
            return ProductSalesResponse.of(product, sales.getSalesIds());
        }catch (Exception ex){
            throw new ValidationException("There was error trying to get the product's sales");

        }
    }

    public SuccesResponse checkProductsStock(ProductCheckStockRequest request){
        if (isEmpty(request) || isEmpty(request.getProducts())){
            throw new ValidationException("The request data and products must be informed.");
        }
        request.getProducts().forEach(this::validateStock);
        return SuccesResponse.create("The stock is Ok");
    }
    private void validateStock(ProductQuantityDTO productQuantity){
        if(isEmpty(productQuantity.getProductId()) || isEmpty(productQuantity.getQuantity())){
            throw new ValidationException("Product ID and quantity must be informed.");
        }
        var product = findById(productQuantity.getProductId());
        if (productQuantity.getQuantity() > product.getQuantityAvailable()){
            throw new ValidationException(String.format("The product %s is out of stock", product.getId()));
        }
    }
}
