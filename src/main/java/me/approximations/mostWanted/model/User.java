package me.approximations.mostWanted.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class User {
    private String name;
    @Setter
    private double headPrice;
}
