package com.yong.mongo.main.test;

import com.mongodb.client.MongoClients;
import com.yong.mongo.main.test.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
public class MongoMainTestApplication {

    public static void main(String[] args) {
        MongoOperations mongoOps = new MongoTemplate(
                MongoClients.create("mongodb://localhost:27017"),
                "springbootdemo");
        mongoOps.insert(new Person("Joe", 34));
        Criteria criteria = where("name").is("Joe").andOperator(where("age").lt(10));
        Query query = new Query(where("name").is("Joe"));
        Person person = mongoOps.findOne(query, Person.class);
        if(person != null) {
            log.info(person.toString());
        }
    }
}
