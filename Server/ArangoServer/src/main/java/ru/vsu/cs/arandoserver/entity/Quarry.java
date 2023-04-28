package ru.vsu.cs.arandoserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Quarry {

    private DataConnection connection;

    private String quarry;
}
