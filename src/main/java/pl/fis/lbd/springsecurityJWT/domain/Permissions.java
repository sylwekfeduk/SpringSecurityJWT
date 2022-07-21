package pl.fis.lbd.springsecurityJWT.domain;

public enum Permissions {

    USER_READ("user:read"),
    USER_EDIT("user:edit"),
    ADMIN("admin");

    private final String permission;

    Permissions(String name) {
        this.permission = name;
    }

    public String getPermission() {
        return permission;
    }
}
