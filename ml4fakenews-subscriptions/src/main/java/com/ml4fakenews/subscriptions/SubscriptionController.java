package com.ml4fakenews.subscriptions;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class SubscriptionController {
    private final List<Subscription> subscriptions = Arrays.asList(
            new Subscription(1, 1, "Subscription A"),
            new Subscription(2, 1, "Subscription B"),
            new Subscription(3, 2, "Subscription C"),
            new Subscription(4, 1, "Subscription D"),
            new Subscription(5, 2, "Subscription E"));

    @GetMapping
    public List<Subscription> getAllOrders() {
        return subscriptions;
    }

    @GetMapping("/{id}")
    public Subscription getOrderById(@PathVariable int id) {
        return subscriptions.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
