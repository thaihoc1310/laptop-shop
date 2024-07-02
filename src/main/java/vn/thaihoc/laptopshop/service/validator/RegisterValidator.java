package vn.thaihoc.laptopshop.service.validator;

import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.thaihoc.laptopshop.domain.dto.RegisterDTO;
import vn.thaihoc.laptopshop.service.UserService;

@Service
public class RegisterValidator implements ConstraintValidator<RegisterChecked, RegisterDTO> {
    private final UserService userService;

    public RegisterValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(RegisterDTO user, ConstraintValidatorContext context) {
        boolean valid = true;

        // Check if email is null

        // if (user.getEmail().isEmpty()) {
        // context.buildConstraintViolationWithTemplate("Email is required")
        // .addPropertyNode("email")
        // .addConstraintViolation()
        // .disableDefaultConstraintViolation();
        // valid = false;
        // }

        // Check if password is at least 6 characters
        if (user.getPassword().length() < 6) {
            context.buildConstraintViolationWithTemplate("Password must be at least 6 characters")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        // Check if first name is at least 2 character
        if (user.getFirstName().length() < 2) {
            context.buildConstraintViolationWithTemplate("First name must be at least 2 characters")
                    .addPropertyNode("firstName")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        // Check if password fields match
        if (!user.getPassword().equals(user.getConfirmPassword()) && user.getPassword().length() >= 6) {
            context.buildConstraintViolationWithTemplate("Passwords must match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        // Additional validations can be added here
        // Check email exist
        if (this.userService.checkEmailExist(user.getEmail())) {
            context.buildConstraintViolationWithTemplate("Email already exists")
                    .addPropertyNode("email")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
