package kz.bmk.course_project.Features.BandCRUD.CreateBand;

public class Band {
    private long id;
    private String name;
    private long registrationNumber;
    private String genre;

    public Band(int id, String name, long registrationNumber, String genre) {
        this.id = id;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.genre = genre;
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

    public long getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(long registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
