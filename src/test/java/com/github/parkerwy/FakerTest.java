package com.github.parkerwy;

import com.github.javafaker.Faker;
import com.github.javafaker.Superhero;
import org.junit.Test;

public class FakerTest {

    @Test
    public void shouldGenerateNames() throws Exception {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            Superhero superhero = faker.superhero();
            System.out.println(superhero.power());
        }
    }
}
