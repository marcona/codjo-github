package net.codjo.pyp.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 */
public class Brin implements Serializable {
    private String uuid;
    private String title;
    private Date creationDate;
    private Status status = Status.current;
    private String description;
    private List<Team> affectedTeams = new ArrayList<Team>();
    private UnblockingType unblockingType;
    private Date unblockingDate;
    private String unblockingDescription;
    private String rootCause;


    public Brin() {
        this("");
    }


    public Brin(String title) {
        this.title = title;
        creationDate = new Date(System.currentTimeMillis());
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public Date getCreationDate() {
        return creationDate;
    }


    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public Status getStatus() {
        return status;
    }


    public void setStatus(Status status) {
        this.status = status;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public List<Team> getAffectedTeams() {
        return affectedTeams;
    }


    public void setAffectedTeams(List<Team> affectedTeams) {
        this.affectedTeams = affectedTeams;
    }


    public UnblockingType getunblockingType() {
        return unblockingType;
    }


    public void setunblockingType(UnblockingType unblockingType) {
        this.unblockingType = unblockingType;
    }


    public Date getUnBlockingDate() {
        return unblockingDate;
    }


    public void setUnBlockingDate(Date unblockingDate) {
        this.unblockingDate = unblockingDate;
    }


    public String getUnBlockingDescription() {
        return unblockingDescription;
    }


    public void setUnBlockingDescription(String unblockingDescription) {
        this.unblockingDescription = unblockingDescription;
    }


    public String getRootCause() {
        return rootCause;
    }


    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }


    public String getUuid() {
        return uuid;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
