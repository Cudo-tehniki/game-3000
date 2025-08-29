package zombi_shooter;

public class Achievement {

    private String id;
    private String name;
    private String description;
    private int requirement;

    public Achievement(String id, String name, String description, int requirement) {
        this.id = id;
        this.name = name;
        this.requirement = requirement;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }
}
