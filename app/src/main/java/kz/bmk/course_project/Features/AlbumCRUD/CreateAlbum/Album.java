package kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum;


public class Album {
    private final double price;
    private final String relDate;
    private long id;
    private String name;

    public Album(long id, String name, double price, String relDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.relDate = relDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public String getRelDate() {
        return relDate;
    }

}