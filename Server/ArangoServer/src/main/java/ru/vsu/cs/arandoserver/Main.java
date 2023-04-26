package ru.vsu.cs.arandoserver;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.entity.BaseDocument;
import com.arangodb.model.AqlQueryOptions;

public class Main {
    public static void main(String[] args) {
        var arangoDB = new ArangoDB.Builder()
                .host("localhost", 8529)
                .user("root")
                .password("12345")
                .build();

        //arangoDB.db("tes1").createCollection("we");

        BaseDocument doc1 = new BaseDocument();
        doc1.addAttribute("name", "Alice");

        arangoDB.db("tes1").collection("we").insertDocument(doc1);


        String query = "FOR doc IN we RETURN doc";
        AqlQueryOptions aqlOptions = new AqlQueryOptions();
        aqlOptions.batchSize(2);
        ArangoCursor<BaseDocument> cursor = arangoDB.db("tes1").query(query, BaseDocument.class);

        // Print the results of the query
        for (BaseDocument document : cursor) {
            System.out.println(document.getAttribute("name"));
        }

//        List<String> collectionNames = arangoDB.getDatabases().stream().toList();
//
//        for (String collectionName : collectionNames) {
//            ArangoCollection collection = arangoDB.db("hello").collection(collectionName);
//
//            CollectionPropertiesEntity ds = collection.getProperties();
//
//            String filename = ds.getName();
//
//            System.out.println(collectionName + ": " + filename);
//        }

    }
}
