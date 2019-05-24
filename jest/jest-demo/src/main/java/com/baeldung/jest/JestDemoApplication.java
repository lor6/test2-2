package com.baeldung.jest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.aliases.AddAliasMapping;
import io.searchbox.indices.aliases.ModifyAliases;
import io.searchbox.indices.aliases.RemoveAliasMapping;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.indices.settings.GetSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
public class JestDemoApplication {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(JestDemoApplication.class, args);

        // Demo the JestClient
        JestClient jestClient = context.getBean(JestClient.class);

        // Check an index
        jestClient.execute(new IndicesExists.Builder("employees").build());

        // Create an index
        jestClient.execute(new CreateIndex.Builder("employees").build());

        // Create an index with options
        Map<String, Object> settings = new HashMap<>();
        settings.put("number_of_shards", 11);
        settings.put("number_of_replicas", 2);
        jestClient.execute(new CreateIndex.Builder("employees").settings(settings).build());

        // Create an alias, then remove it
        jestClient.execute(new ModifyAliases.Builder(
                new AddAliasMapping.Builder(
                        "employees",
                        "e")
                        .build())
                .build());
        jestClient.execute(new ModifyAliases.Builder(
                new RemoveAliasMapping.Builder(
                        "employees",
                        "e")
                        .build())
                .build());

        // Sample JSON for indexing

        // {
        //  "name": "Michael Pratt",
        //  "title": "Java Developer",
        //  "skills": ["java", "spring", "elasticsearch"],
        //  "years_of_service": 2
        // }

        // Index a document from String
        ObjectMapper mapper = new ObjectMapper();
        JsonNode employeeJsonNode = mapper.createObjectNode()
                .put("name", "Michael Pratt")
                .put("title", "Java Developer")
                .put("years_of_service", 2)
                .set("skills", mapper.createArrayNode()
                        .add("java")
                        .add("spring")
                        .add("elasticsearch"));
        jestClient.execute(new Index.Builder(employeeJsonNode.toString()).index("employees").build());

        // Index a document from Map
        Map<String, Object> employeeHashMap = new LinkedHashMap<>();
        employeeHashMap.put("name", "Michael Pratt");
        employeeHashMap.put("title", "Java Developer");
        employeeHashMap.put("years_of_service", 2);
        employeeHashMap.put("skills", Arrays.asList("java", "spring", "elasticsearch"));
        jestClient.execute(new Index.Builder(employeeHashMap).index("employees").build());

        // Index a document from POJO
        Employee employee = new Employee();
        employee.setName("Michael Pratt");
        employee.setTitle("Java Developer");
        employee.setYears_of_service(2);
        employee.setSkills(Arrays.asList("java", "spring", "elasticsearch"));
        jestClient.execute(new Index.Builder(employee).index("employees").build());

        // Read document by ID
        jestClient.execute(new Get.Builder("employees", "1").build());

        // Search documents
        String search = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        { \"match\": { \"name\":   \"Michael Pratt\" }}\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        jestClient.execute(new Search.Builder(search).build());

        // Update document
        employee.setYears_of_service(3);
        jestClient.execute(new Update.Builder(employee).index("employees").id("1").build());

        // Delete documents
        jestClient.execute(new Delete.Builder("2") .index("employees") .build());

        // Bulk operations
        Employee employeeObject1 = new Employee();
        employee.setName("John Smith");
        employee.setTitle("Python Developer");
        employee.setYears_of_service(10);
        employee.setSkills(Arrays.asList("python"));

        Employee employeeObject2 = new Employee();
        employee.setName("Kate Smith");
        employee.setTitle("Senior JavaScript Developer");
        employee.setYears_of_service(10);
        employee.setSkills(Arrays.asList("javascript", "angular"));

        jestClient.execute(new Bulk.Builder() .defaultIndex("employees")
                .addAction(new Index.Builder(employeeObject1).build())
                .addAction(new Index.Builder(employeeObject2).build())
                .addAction(new Delete.Builder("3").build()) .build());

        // Async operations
        Employee employeeObject3 = new Employee();
        employee.setName("Jane Doe");
        employee.setTitle("Manager");
        employee.setYears_of_service(20);
        employee.setSkills(Arrays.asList("managing"));

        jestClient.executeAsync( new Index.Builder(employeeObject3).build(), new JestResultHandler<JestResult>() {
            @Override public void completed(JestResult result) {
                // handle result
            }
            @Override public void failed(Exception ex) {
                // handle exception
            }
        });
    }

}
