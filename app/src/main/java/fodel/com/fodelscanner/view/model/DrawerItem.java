package fodel.com.fodelscanner.view.model;

public class DrawerItem {

    public static enum Type {HEADER,MENU_HEAD,MENU, DIVIDER}

    private final Type type;

    public DrawerItem(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}