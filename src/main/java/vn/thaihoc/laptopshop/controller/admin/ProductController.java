package vn.thaihoc.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

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
        return "/admin/product/create-product";
    }

    @PostMapping("/admin/product/create")
    public String getPostProduct(Model model, @ModelAttribute("newProduct") @Valid Product newProduct,
            BindingResult newProductBindingResult, @RequestParam("imageFile") MultipartFile file) {
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create-product";
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

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("id", id);
        model.addAttribute("product", product);
        model.addAttribute("imagePath", this.uploadService.getAbsolutePath("product", product.getImage()));
        return "/admin/product/product-inf";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("product-delete", new Product());
        return "/admin/product/delete-product";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("product-delete") Product thaihoc) {
        productService.deleteProductById(thaihoc.getId());
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product-update", product);
        System.out.println(product);
        model.addAttribute("imagePath", this.uploadService.getAbsolutePath("product", product.getImage()));
        return "/admin/product/update-product";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProductDetail(Model model, @ModelAttribute("product-update") @Valid Product thaihoc,
            BindingResult productBindingResult,
            @RequestParam("imageFile") MultipartFile file) {
        if (productBindingResult.hasErrors()) {
            return "/admin/product/update-product";
        }
        Product productUpdate = this.productService.getProductById(thaihoc.getId());
        if (!productUpdate.equals(null)) {
            productUpdate.setName(thaihoc.getName());
            productUpdate.setPrice(thaihoc.getPrice());
            productUpdate.setQuantity(thaihoc.getQuantity());
            productUpdate.setDetailDesc(thaihoc.getDetailDesc());
            productUpdate.setShortDesc(thaihoc.getShortDesc());
            productUpdate.setFactory(thaihoc.getFactory());
            productUpdate.setTarget(thaihoc.getTarget());
            if (!file.isEmpty())
                productUpdate.setImage(this.uploadService.handleSaveUploadFile(file, "product"));
        }
        this.productService.saveProduct(productUpdate);

        return "redirect:/admin/product";
    }
}
