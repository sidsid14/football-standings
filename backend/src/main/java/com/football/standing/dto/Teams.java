package com.football.standing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Teams {
    public String team_key;
    public String team_name;
    public String team_country;
}
