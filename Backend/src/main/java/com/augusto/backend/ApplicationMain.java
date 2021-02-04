package com.augusto.backend;

import com.augusto.backend.domain.Category;
import com.augusto.backend.domain.City;
import com.augusto.backend.domain.Product;
import com.augusto.backend.domain.State;
import com.augusto.backend.repository.CategoryRepository;
import com.augusto.backend.repository.CityRepository;
import com.augusto.backend.repository.ProductRepository;
import com.augusto.backend.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class ApplicationMain implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;

    @Autowired
    public ApplicationMain(CategoryRepository categoryRepository, ProductRepository productRepository, CityRepository cityRepository, StateRepository stateRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
    }

    /*
        This allow to create and save objects to database on springboot startUp
        It will be removed later and replaced by SQL scripts
    */
    @Override
    public void run(String... args) throws Exception {

        // insertion of category and product

        Category category1 = new Category(null, "Informatics", new ArrayList<>());
        Category category2 = new Category(null, "Office", new ArrayList<>());

        Product product1 = new Product(null, "Computer", 2000.00, new ArrayList<>());
        Product product2 = new Product(null, "Printer", 800.00, new ArrayList<>());
        Product product3 = new Product(null, "Mouse", 80.00, new ArrayList<>());

        category1.getProductList().addAll(Arrays.asList(product1, product2, product3));
        category2.getProductList().add(product2);

        product1.getCategoryList().add(category1);
        product2.getCategoryList().addAll(Arrays.asList(category1, category2));
        product3.getCategoryList().add(category1);

        categoryRepository.saveAll(Arrays.asList(category1, category2));
        productRepository.saveAll(Arrays.asList(product1, product2, product3));

        // insertion of state and city

        State state1 = new State(null, "Minas Gerais", new ArrayList<>());
        State state2 = new State(null, "Sao Paulo", new ArrayList<>());

        City city1 = new City(null, "Uberlandia", state1);
        City city2 = new City(null, "Sao Paulo", state2);
        City city3 = new City(null, "Campinas", state2);

        state1.getCities().add(city1);
        state2.getCities().addAll(Arrays.asList(city2, city3));

        stateRepository.saveAll(Arrays.asList(state1, state2));
        cityRepository.saveAll(Arrays.asList(city1, city2, city3));
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class);
    }
}
