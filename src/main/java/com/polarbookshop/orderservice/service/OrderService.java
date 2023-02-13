package com.polarbookshop.orderservice.service;

import com.polarbookshop.orderservice.domain.Book;
import com.polarbookshop.orderservice.domain.Order;
import com.polarbookshop.orderservice.domain.OrderStatus;
import com.polarbookshop.orderservice.repository.OrderRepository;
import com.polarbookshop.orderservice.web.BookClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final BookClient bookClient;
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository, BookClient bookClient) {
        this.orderRepository = orderRepository;
        this.bookClient = bookClient;
    }

    public Flux<Order> getOrders(){
        return this.orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String isbn, int quantity) {
        return this.bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectedOrder(isbn, quantity))
                .flatMap(orderRepository::save);
    }

    public static Order buildAcceptedOrder(
            Book book, int quantity
    ) {
        return Order.of(book.isbn(), book.title() + " - " + book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(
            String bookIsbn, int quantity
    ) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
