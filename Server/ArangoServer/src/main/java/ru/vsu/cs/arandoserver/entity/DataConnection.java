package ru.vsu.cs.arandoserver.entity;

import com.arangodb.entity.BaseDocument;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class DataConnection {

    private String dbName;
    private String host;
    private Integer port;
    private String userName;
    private String password;
    private String collection;

}
