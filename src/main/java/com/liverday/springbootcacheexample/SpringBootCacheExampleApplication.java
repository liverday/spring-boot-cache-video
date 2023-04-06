package com.liverday.springbootcacheexample;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableCaching
public class SpringBootCacheExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCacheExampleApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(final BookService bookService) {
		return args -> {
			System.out.println("-----------------");
			System.out.println("Livros chamada por id 1: " + bookService.getBookById(1L));
			System.out.println("Livros chamada por id 1: " + bookService.getBookById(1L));
//			System.out.println("Livro 1: " + bookService.getBookById(1L));
			System.out.println("Livros chamada por id 2: " + bookService.getBookById(2L));
			System.out.println("Livros chamada por id 2: " + bookService.getBookById(2L));
			System.out.println("-----------------");
		};
	}
}

record Book(Long id, String title) { }

@Service
class BookService {
	private final List<Book> books = new ArrayList<>() {{
		add(new Book(1L, "Harry Potter: A pedra filosofal"));
		add(new Book(2L, "Percy Jackson: O Ladrão de Raios"));
		add(new Book(3L, "Senhor dos Anéis: O retorno do Rei"));
	}};

	@Cacheable("books")
	public List<Book> getBooks() throws InterruptedException {
		System.out.println("Pesquisando todos os livros");
		simulateSlowService();
		return books;
	}

	@Cacheable("books")
	public Optional<Book> getBookById(final Long id) throws InterruptedException {
		System.out.println("Pesquisando o livro por id: " + id);
		simulateSlowService();
		return books.stream()
				.filter(book -> book.id().equals(id))
				.findFirst();
	}

	public void simulateSlowService() throws InterruptedException {
		Thread.sleep(5000);
	}
}
