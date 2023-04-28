package bet25.live.win;

public class Exercise { int id;
    int id_musle;
    String name;
    String photo;
    int time_level ;

    public Exercise(int id, int id_musle, String name, int time_level, String photo) {
        this.id = id;
        this.id_musle = id_musle;
        this.name = name;
        this.photo = photo;
        this.time_level=time_level;
    }

    public int getId() {
        return id;
    }

    public int getId_musle() {
        return id_musle;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public int getTime_level() {
        return time_level;
    }
}