package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {

    private String name;
    private boolean completed;
    private String date;
    private String type;
    private String description;

    private List<String> skillsUsed;
    private Map<String, String> links;

    public Project(String name, boolean completed, String date, String type, String description) {
        this.name = name;
        this.completed = completed;
        this.date = date;
        this.type = type;
        this.description = description;

        skillsUsed = new ArrayList<>();
        links = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSkillsUsed() {
        return skillsUsed;
    }

    public void setSkillsUsed(List<String> skillsUsed) {
        this.skillsUsed = skillsUsed;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
