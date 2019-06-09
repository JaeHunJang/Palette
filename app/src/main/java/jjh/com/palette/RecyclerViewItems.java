package jjh.com.palette;

public class RecyclerViewItems {
    private String name;
    private String color;
    private String date;
    private String tags;

    public RecyclerViewItems(String name, String color, String date, String tags) {
        this.name = name;
        this.color = color;
        this.date = date;
        this.tags = tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getTags() {
        return tags;
    }
}
