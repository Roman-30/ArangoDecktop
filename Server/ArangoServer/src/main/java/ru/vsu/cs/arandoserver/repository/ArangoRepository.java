package ru.vsu.cs.arandoserver.repository;

import com.arangodb.springframework.annotation.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoRepository extends com.arangodb.springframework.repository.ArangoRepository<Object, String> {
    @Query("query")
    void insertData(String query);


}
