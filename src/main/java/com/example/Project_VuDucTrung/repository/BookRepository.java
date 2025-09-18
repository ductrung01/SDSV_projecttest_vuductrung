package com.example.Project_VuDucTrung.repository;

import com.example.Project_VuDucTrung.model.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository {

    private static final String FILE_PATH = "src/main/resources/data/books.json";
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<Book>>() {}.getType();

    private List<Book> loadBooks() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            List<Book> books = gson.fromJson(reader, listType);
            return books != null ? books : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void saveBooks(List<Book> books) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(books, writer);
        } catch (Exception e) {
            throw new RuntimeException("Error saving books to JSON", e);
        }
    }

    public int checkStock(long bookId) {
        return loadBooks().stream()
                .filter(b -> b.getBookId().equals(bookId))
                .map(Book::getStockQuantity)
                .findFirst()
                .orElse(0);
    }

    public Book findById(Long bookId) {
        return loadBooks().stream()
                .filter(b -> b.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public void save(Book book) {
        List<Book> books = loadBooks();
        // nếu đã tồn tại thì update stock + info
        books.removeIf(b -> b.getBookId().equals(book.getBookId()));
        books.add(book);
        saveBooks(books);
    }
}