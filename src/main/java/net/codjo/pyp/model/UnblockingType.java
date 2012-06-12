package net.codjo.pyp.model;
/**
 *
 */
public enum UnblockingType {
    quick("QUICK"),
    medium("MEDIUM"),
    slow("SLOW");

    private String type;


    private UnblockingType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }
}
