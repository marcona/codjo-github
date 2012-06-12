package net.codjo.pyp.model;
/**
 *
 */
public enum Status {
    current("CURRENT", 0),
    unblocked("UNBLOCKED", 2),
    toEradicate("TO_ERADICATE", 1),
    eradicated("ERADICATED", 3);

    private String status;
    private int order;


    private Status(String status, int order) {
        this.status = status;
        this.order = order;
    }


    public String getStatus() {
        return status;
    }


    public int getOrder() {
        return order;
    }

}
