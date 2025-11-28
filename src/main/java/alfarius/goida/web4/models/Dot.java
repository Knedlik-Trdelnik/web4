package alfarius.goida.web4.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.naming.Name;

@Entity
@Table(name = "web_dots")
public class Dot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "x")
    @NotNull
    private double x;
    @NotNull
    @Column(name = "y")
    private  double y;
    @NotNull
    @Column(name = "r")
    private  double r;
    @NotNull
    @Column(name = "time")
    private  int nano;

    public Dot() {
    }

    public Long getId() {
        return id;
    }

    @NotNull
    public double getX() {
        return x;
    }

    @NotNull
    public double getY() {
        return y;
    }

    @NotNull
    public double getR() {
        return r;
    }

    @NotNull
    public int getNano() {
        return nano;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setX(@NotNull double x) {
        this.x = x;
    }

    public void setY(@NotNull double y) {
        this.y = y;
    }

    public void setR(@NotNull double r) {
        this.r = r;
    }

    public void setNano(@NotNull int nano) {
        this.nano = nano;
    }
}
