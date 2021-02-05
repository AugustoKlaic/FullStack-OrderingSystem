package com.augusto.backend;

import com.augusto.backend.domain.*;
import com.augusto.backend.domain.enums.ClientTypeEnum;
import com.augusto.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class ApplicationMain implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final AddressRespository addressRespository;
    private final ClientRepository clientRepository;

    @Autowired
    public ApplicationMain(CategoryRepository categoryRepository, ProductRepository productRepository, CityRepository cityRepository,
                           StateRepository stateRepository, AddressRespository addressRespository, ClientRepository clientRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.addressRespository = addressRespository;
        this.clientRepository = clientRepository;
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

        // insertion of adress, client and telephone

        Client client1 = new Client(null, "Maria Silva", "maria@gmail.com", "86101153053",
                ClientTypeEnum.LEGAL_PERSON, new ArrayList<>(), Set.of("33401041", "97330690"));

        Address address1 = new Address(null, "Rua flores", "300", "Apto 202", "Jardim", "91380240", city1, client1);
        Address address2 = new Address(null, "Avenida Matos", "105", "Sala 800", "Centro", "91240380", city2, client1);

        client1.getAddresses().addAll(Arrays.asList(address1, address2));

        clientRepository.save(client1);
        addressRespository.saveAll(Arrays.asList(address1, address2));
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class);
    }
}
