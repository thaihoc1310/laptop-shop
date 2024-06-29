package vn.thaihoc.laptopshop.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.thaihoc.laptopshop.domain.Product;
import vn.thaihoc.laptopshop.service.ProductService;

@Controller
public class HomePageController {
    private final ProductService productService;

    public HomePageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "/client/homepage/homepage_view";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "/client/auth/register";
    }
}
