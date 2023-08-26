package io.narsha.smartpage.spring.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.RowMapper;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class JpaQueryExecutorTest {

    @Autowired
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test() {
        final Pair<List<Country>, Long> res = new JpaQueryExecutor(entityManager).execute(new PaginatedFilteredQuery<>(Country.class), new RowMapper(objectMapper));
        res.getKey().forEach(System.out::println);
    }

    @SpringBootApplication
    public static class Main {

    }

    public static class Country {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Country{" + "id=" + id + ", name='" + name + "'}";
        }
    }
}
