package me.approximations.mostWanted.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class User {
    private String name;
    @Setter
    private double headPrice;

    public boolean isInContract() {
        return this.headPrice > 0;
    }
}
