package vn.thaihoc.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.service.UploadService;
import vn.thaihoc.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {
    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getHomepage(Model model) {
        List<User> arrUsers = this.userService.getAllUsersByEmail("thaihoc131005@gmail.com");
        System.out.println(arrUsers);
        model.addAttribute("test", "test");
        model.addAttribute("test2", "from controller with model");
        return "hello";
    }

    @GetMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users1", users);
        return "/admin/user/user_view";
    }

    @GetMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("user", user);
        return "/admin/user/user-inf";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "/admin/user/create-user";
    }

    @PostMapping(value = "/admin/user/create")
    public String postcreateUser(Model model, @ModelAttribute("newUser") User thaihoc,
            @RequestParam("imageFile") MultipartFile file) {
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(thaihoc.getPassword());
        thaihoc.setAvatar(avatar);
        thaihoc.setPassword(hashPassword);
        thaihoc.setRole(userService.getRoleByName(thaihoc.getRole().getName()));
        userService.handleSaveUser(thaihoc);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user-update", user);
        return "/admin/user/update-user";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUserDetail(Model model, @ModelAttribute("user-update") User thaihoc) {
        User userUpdate = this.userService.getUserById(thaihoc.getId());
        if (!userUpdate.equals(null)) {
            userUpdate.setFullName(thaihoc.getFullName());
            userUpdate.setPhone(thaihoc.getPhone());
            userUpdate.setAddress(thaihoc.getAddress());
            this.userService.handleSaveUser(userUpdate);
        }
        // use way below could be loss email and id
        // this.userService.handleSaveUser(thaihoc);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("user-delete", new User());
        return "/admin/user/delete-user";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("user-delete") User thaihoc) {
        System.out.println(thaihoc);
        userService.deleteUserById(thaihoc.getId());
        return "redirect:/admin/user";
    }
}
