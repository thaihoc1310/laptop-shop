package vn.thaihoc.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.thaihoc.laptopshop.domain.Product;
import vn.thaihoc.laptopshop.service.ProductService;
import vn.thaihoc.laptopshop.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products1", products);
        return "/admin/product/product_view";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "/admin/product/create_product";
    }

    @PostMapping("/admin/product/create")
    public String getPostProduct(Model model, @ModelAttribute("newProduct") @Valid Product newProduct,
            BindingResult newProductBindingResult, @RequestParam("imageFile") MultipartFile file) {
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create_product";
        }
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }
        String imageName = this.uploadService.handleSaveUploadFile(file, "product");
        newProduct.setImage(imageName);
        this.productService.saveProduct(newProduct);
        return "redirect:/admin/product";

    }

}
