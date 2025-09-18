package com.example.Project_VuDucTrung.repository;

import com.example.Project_VuDucTrung.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

@Repository
public class UserRepository {

    private static final String FILE_PATH = "src/main/resources/data/users.json";
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<User>>() {}.getType();

    public User findByUserNameAndPassWord(String username, String password) {
        try (Reader reader = new FileReader(FILE_PATH)) {
            List<User> users = gson.fromJson(reader, listType);
            if (users == null) return null;
            return users.stream()
                    .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}