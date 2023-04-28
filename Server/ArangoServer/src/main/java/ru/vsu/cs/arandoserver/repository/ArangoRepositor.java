package ru.vsu.cs.arandoserver.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoRepositor extends ArangoRepository<Object, String> {

    @Query("query")
    void insertData(String query);


}
