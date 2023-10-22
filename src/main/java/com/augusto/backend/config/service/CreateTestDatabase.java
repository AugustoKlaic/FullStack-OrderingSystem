package com.augusto.backend.config.service;

import com.augusto.backend.domain.*;
import com.augusto.backend.domain.enums.ClientTypeEnum;
import com.augusto.backend.domain.enums.PaymentStateEnum;
import com.augusto.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CreateTestDatabase {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final AddressRespository addressRespository;
    private final ClientRepository clientRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PaymentRepository paymentRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    public CreateTestDatabase(CategoryRepository categoryRepository, ProductRepository productRepository, CityRepository cityRepository,
                              StateRepository stateRepository, AddressRespository addressRespository, ClientRepository clientRepository, PurchaseOrderRepository purchaseOrderRepository, PaymentRepository paymentRepository, PurchaseOrderItemRepository purchaseOrderItemRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.addressRespository = addressRespository;
        this.clientRepository = clientRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.paymentRepository = paymentRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
    }

    public void instantiateTestDatabase() throws ParseException {
        // insertion of category and product

        Category category1 = new Category(null, "Informatics", new ArrayList<>());
        Category category2 = new Category(null, "Office", new ArrayList<>());
        Category category3 = new Category(null, "Household linen", new ArrayList<>());
        Category category4 = new Category(null, "Electronics", new ArrayList<>());
        Category category5 = new Category(null, "Gardening", new ArrayList<>());
        Category category6 = new Category(null, "Decoration", new ArrayList<>());
        Category category7 = new Category(null, "Perfumery", new ArrayList<>());

        Product product1 = new Product(null, "Computer", 2000.00, new ArrayList<>(), new HashSet<>());
        Product product2 = new Product(null, "Printer", 800.00, new ArrayList<>(), new HashSet<>());
        Product product3 = new Product(null, "Mouse", 80.00, new ArrayList<>(), new HashSet<>());
        Product product4 = new Product(null, "Office table", 300.00, new ArrayList<>(), new HashSet<>());
        Product product5 = new Product(null, "Towel", 50.00, new ArrayList<>(), new HashSet<>());
        Product product6 = new Product(null, "Quilt", 200.00, new ArrayList<>(), new HashSet<>());
        Product product7 = new Product(null, "True color TV", 1200.00, new ArrayList<>(), new HashSet<>());
        Product product8 = new Product(null, "Brush cutter", 800.00, new ArrayList<>(), new HashSet<>());
        Product product9 = new Product(null, "Lampshade", 100.00, new ArrayList<>(), new HashSet<>());
        Product product10 = new Product(null, "Pending", 180.00, new ArrayList<>(), new HashSet<>());
        Product product11 = new Product(null, "Shampoo", 90.00, new ArrayList<>(), new HashSet<>());

        category1.getProductList().addAll(List.of(product1, product2, product3));
        category2.getProductList().addAll(List.of(product2, product4));
        category3.getProductList().addAll(List.of(product5, product6));
        category4.getProductList().addAll(List.of(product1, product2, product3, product7));
        category5.getProductList().add(product8);
        category6.getProductList().addAll(List.of(product9, product10));
        category6.getProductList().add(product11);

        product1.getCategoryList().addAll(List.of(category1, category4));
        product2.getCategoryList().addAll(List.of(category1, category2, category4));
        product3.getCategoryList().addAll(List.of(category1, category4));
        product4.getCategoryList().add(category2);
        product5.getCategoryList().add(category3);
        product6.getCategoryList().add(category3);
        product7.getCategoryList().add(category4);
        product8.getCategoryList().add(category5);
        product9.getCategoryList().add(category6);
        product10.getCategoryList().add(category6);
        product11.getCategoryList().add(category7);

        categoryRepository.saveAll(Arrays.asList(category1, category2, category3, category4, category5, category6, category7));
        productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10, product11));

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

        // insertion of address, client and telephone

        Client client1 = new Client("Maria Silva", "maria@gmail.com", "86101153053",
                ClientTypeEnum.LEGAL_PERSON, Set.of("33401041", "97330690"), new ArrayList<>());

        Address address1 = new Address("Rua flores", "300", "Apto 202", "Jardim", "91380240", city1, client1);
        Address address2 = new Address("Avenida Matos", "105", "Sala 800", "Centro", "91240380", city2, client1);

        client1.setAddresses(Set.of(address1, address2));

        clientRepository.save(client1);
        addressRespository.saveAll(Arrays.asList(address1, address2));

        // insertion of purchaseOrders and payments

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        PurchaseOrder purchaseOrder1 = new PurchaseOrder(null, sdf.parse("30/09/2020 10:30"), null, client1, address1, new HashSet<>());
        PurchaseOrder purchaseOrder2 = new PurchaseOrder(null, sdf.parse("10/10/2020 19:37"), null, client1, address2, new HashSet<>());

        Payment creditCardPayment = new CreditCardPayment(null, PaymentStateEnum.SETTLED, purchaseOrder1, 6);
        Payment billetPayment = new BilletPayment(null, PaymentStateEnum.PENDING, purchaseOrder2, sdf.parse("20/10/2020 00:00"), null);

        purchaseOrder1.setPayment(creditCardPayment);
        purchaseOrder2.setPayment(billetPayment);
        client1.getPurchaseOrders().addAll(Arrays.asList(purchaseOrder1, purchaseOrder2));

        purchaseOrderRepository.saveAll(Arrays.asList(purchaseOrder1, purchaseOrder2));
        paymentRepository.saveAll(Arrays.asList(billetPayment, creditCardPayment));

        // insertion of purchaseOrderItems

        PurchaseOrderItem purchaseOrderItem1 = new PurchaseOrderItem(purchaseOrder1, product1, 0.00, 1, 2000.00);
        PurchaseOrderItem purchaseOrderItem2 = new PurchaseOrderItem(purchaseOrder1, product3, 0.00, 2, 80.00);
        PurchaseOrderItem purchaseOrderItem3 = new PurchaseOrderItem(purchaseOrder2, product2, 100.00, 1, 800.00);

        purchaseOrder1.getItems().addAll(Arrays.asList(purchaseOrderItem1, purchaseOrderItem2));
        purchaseOrder2.getItems().add(purchaseOrderItem3);

        product1.getItems().add(purchaseOrderItem1);
        product2.getItems().add(purchaseOrderItem3);
        product3.getItems().add(purchaseOrderItem2);

        purchaseOrderItemRepository.saveAll(Arrays.asList(purchaseOrderItem1, purchaseOrderItem2, purchaseOrderItem3));

    }
}
