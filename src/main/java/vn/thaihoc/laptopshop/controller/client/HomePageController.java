package vn.thaihoc.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.thaihoc.laptopshop.domain.Product;
import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.domain.dto.RegisterDTO;
import vn.thaihoc.laptopshop.service.ProductService;
import vn.thaihoc.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class HomePageController {
    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        // Pageable pageable = PageRequest.of(0, 4);
        // Page<Product> pageProducts = this.productService.getAllProducts(pageable);
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products", products);
        return "client/homepage/homepage_view";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult bindingResult) {
        // Register user
        if (bindingResult.hasErrors()) {
            return "client/auth/register";
        }
        User user = userService.registerDTOtoUser(registerDTO);
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        user.setRole(userService.getRoleByName("USER"));
        userService.handleSaveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "client/auth/login";
    }

    @GetMapping("/access-denied")
    public String getDenyPage(Model model) {
        return "client/auth/access-denied";
    }

    @GetMapping("/products")
    public String getProductPage(Model model, @RequestParam("page") Optional<String> pageOptional,
            @RequestParam("factory") Optional<String> factoryOptional,
            @RequestParam("min-price") Optional<String> minPriceOptional,
            @RequestParam("max-price") Optional<String> maxPriceOptional,
            @RequestParam("price") Optional<String> priceOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {

        }

        Pageable pageable = PageRequest.of(page - 1, 6);

        // String factory = factoryOptional.isPresent() ? factoryOptional.get() : "";
        // String price = priceOptional.isPresent() ? priceOptional.get() : "";
        List<String> price = priceOptional.isPresent() ? Arrays.asList(priceOptional.get().split(","))
                : new ArrayList<String>();
        double minPrice = minPriceOptional.isPresent() ? Double.parseDouble(minPriceOptional.get()) : 0;
        double maxPrice = maxPriceOptional.isPresent() ? Double.parseDouble(maxPriceOptional.get()) : 2000000000;

        // List<String> factory = Arrays.asList(factoryOptional.get().split(","));

        // Page<Product> pageProducts = this.productService.getAllProducts(pageable,
        // maxPrice);
        Page<Product> pageProducts = this.productService.getAllProducts(pageable, price);
        List<Product> products = pageProducts.getContent();
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        return "client/product/all_product";
    }
}
