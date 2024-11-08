package test;

import main.json.SimpleJson;

public class Test {
    public static void main (String[] args) throws Exception {
        final var json = "{\"data\":{\"name\":\"John\",\"age\":30,\"personOccupation\":{\"description\":\"dev pleno\",\"salary\":1000.99,\"current\":true}},\"id\":15674}";
        final var person = SimpleJson.parse(json, Person.class);
        System.out.println(person);

        final var toJson = SimpleJson.toJson(person);
        System.out.println(toJson);

        final var jsonList = "[{\"salaries\":[{\"value\":10},{\"value\":20}],\"data\":{\"name\":\"John\",\"age\":30,\"personOccupation\":{\"description\":\"dev pleno\",\"salary\":1000.99,\"current\":true}},\"id\":15674},{\"data\":{\"name\":\"John Doe\",\"age\":29,\"personOccupation\":{\"description\":\"dev jr\",\"salary\":1000.99,\"current\":false}},\"id\":3,\"numbers\":[1,2,3]}]";
        final var personList = SimpleJson.parseList(jsonList, Person.class);
        System.out.println(personList);

        final var toJsonList = SimpleJson.toJson(personList);
        System.out.println(toJsonList);
    }
}