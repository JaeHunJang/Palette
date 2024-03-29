package jjh.com.palette;

//리사이클러뷰를 구성할 아이템의 데이터를 가질 객체 클래스
public class RecyclerViewItems {
    private String num;
    private String name;
    private String color;
    private String date;
    private String tags;
    private String id;
    private String lib;

    public RecyclerViewItems(String num, String id, String lib,String name, String color, String date, String tags) {
        this.num = num;
        this.name = name;
        this.color = color;
        this.date = date;
        this.tags = tags;
        this.id = id;
        this.lib = lib;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getLib() {
        return lib;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
