package net.neszku.patch_tracker;

public enum Emote {

    LEFT(":lewo:", "702924986247020626"),
    RIGHT(":prawo:", "702924999077527643");

    private final String name;
    private final String id;

    Emote(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String asSnowflake() {
        return name + id;
    }

    public String asEmote() {
        return "<" + asSnowflake() + ">";
    }

}