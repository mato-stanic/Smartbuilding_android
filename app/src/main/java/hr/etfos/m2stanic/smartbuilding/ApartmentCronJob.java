package hr.etfos.m2stanic.smartbuilding;

import java.util.List;

/**
 * Created by mato on 20.04.16..
 */

public class ApartmentCronJob {
    private Long id;
    private String time;
    private String action;
    private String room;
    private List<String> days;

    public ApartmentCronJob(Long id, String time, String action, String room, List<String> days) {
        this.id = id;
        this.time = time;
        this.action = action;
        this.room = room;
        this.days = days;
    }

    public ApartmentCronJob(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "ApartmentCronJob{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", action='" + action + '\'' +
                ", room='" + room + '\'' +
                ", days=" + days +
                '}';
    }
}
