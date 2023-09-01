package io.narsha.smartpage.spring.test.controller;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.spring.test.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

@RestController
public class PaginatedController {

    @Autowired
    private AtomicReference<PaginatedFilteredQuery<Person>> reference;

    @GetMapping("/test")
    public String test(PaginatedFilteredQuery<Person> query) {
        reference.set(query);
        return "ok";
    }
}
