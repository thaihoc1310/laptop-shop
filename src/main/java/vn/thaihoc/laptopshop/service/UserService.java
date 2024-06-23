package vn.thaihoc.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return this.repository.findAll();
    }

    public List<User> getAllUsersByEmail(String email) {
        return this.repository.findAllByEmail(email);
    }

    public User getUserById(long id) {
        return this.repository.findOneById(id);
    }

    public User handleSaveUser(User user) {
        return this.repository.save(user);
    }

    public void deleteUserById(long id) {
        this.repository.deleteById(id);
    }
}
