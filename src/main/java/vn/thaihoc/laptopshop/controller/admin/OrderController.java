package vn.thaihoc.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/admin/order")
    public String getOrder() {
        return "/admin/order/order_view";
    }
}
