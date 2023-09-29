package io.narsha.smartpage.spring.test.controller;

import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.spring.test.model.Person;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaginatedController {

  @Autowired private AtomicReference<SmartPageQuery<Person>> reference;

  @GetMapping("/test")
  public String test(SmartPageQuery<Person> query) {
    reference.set(query);
    return "ok";
  }
}
