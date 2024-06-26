package vn.thaihoc.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.thaihoc.laptopshop.domain.Product;
import vn.thaihoc.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveProduct(Product newProduct) {
        this.productRepository.save(newProduct);
    }

    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product getProductById(long id) {
        return this.productRepository.findOneById(id);
    }
}
