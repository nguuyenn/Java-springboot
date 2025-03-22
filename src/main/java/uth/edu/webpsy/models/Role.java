package uth.edu.webpsy.models;

public enum Role {
    STUDENT("Sinh viên"),
    PARENT("Phụ huynh"),
    PSYCHOLOGIST("Chuyên gia tâm lý"),
    ADMIN("Quản trị viên");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
