package ecs.components;

import com.badlogic.ashley.core.Component;

import java.util.HashSet;
import java.util.Set;

public class TeamComponent implements Component {

    public static Set<String> teams = new HashSet<>();

    public String teamName;

    public TeamComponent(String teamName) {
        teams.add(teamName);
        this.teamName = teamName;
    }
}
