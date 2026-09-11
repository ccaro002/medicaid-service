package com.carloscaro.medicaidservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedicaidServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicaidServiceApplication.class, args);
    }

//    @Bean
//    CommandLineRunner demo(DrugService service) {
//        return args -> {
//            Drug sample = service.find("D9").orElseThrow();
//            System.out.println("found: " + sample.getId() + " " + sample.getName());
//             service.create("D10", null);
//        };
//    }
}
