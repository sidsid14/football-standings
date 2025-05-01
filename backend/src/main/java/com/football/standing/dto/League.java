package com.football.standing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class League {
    String country_id;
    String country_name;
    String league_id;
    String league_name;
}
