package me.rotemfo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TickerSymbol {
    private String name;
    private Double value;
}