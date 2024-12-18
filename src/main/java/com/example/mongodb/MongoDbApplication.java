package com.example.mongodb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MongoDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoDbApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address("England", "London", "NE9");

            String email = "namdaribrahim04@gmail.com";
            Student student = new Student(
                    "Ibrakhim",
                    "Namdar",
                    email,
                    Gender.MALE,
                    address,
                    List.of("Computer Sciencem, Maths"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(email));
            List<Student> students = mongoTemplate.find(query, Student.class);

            if(students.size() > 1) {
                throw new RuntimeException("More than one student with the same email");
            }

            if(students.isEmpty()) {
                System.out.println("Inserting student " + student);
                repository.insert(student);
            }else{
                System.out.println("Student already exists");
            }
        };
    }

}
