package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ProductDTO;
import com.mindhub.homebanking.models.ProductType;
import com.mindhub.homebanking.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<ProductDTO> getProducts(){
        return productRepository.findAll().stream().map(ProductDTO::new).filter(p -> p.getType() != ProductType.OPERATIONAL).collect(Collectors.toList());
    }
}
