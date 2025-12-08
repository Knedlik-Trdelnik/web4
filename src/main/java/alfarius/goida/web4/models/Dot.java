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
    private double y;
    @NotNull
    @Column(name = "r")
    private double r;
    @NotNull
    @Column(name = "time")
    private int nano;

    @NotNull
    @Column(name = "hit_status")
    private boolean hitStatus;
    @NotNull
    @Column(name = "user_id")
    private Long userId;

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

    @NotNull
    public boolean isHitStatus() {
        return hitStatus;
    }


    public @NotNull Long getUserId() {
        return userId;
    }

    public void setHitStatus(@NotNull boolean hitStatus) {
        this.hitStatus = hitStatus;
    }


    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public void checkHit() {
        if (this.x == 0.0 && this.y == 0.0 && this.r == 0.0) {
            this.hitStatus = true;
            return;
        } else if (x <= 0 && y >= 0) {
            if (Math.abs(x) < r / 2d && y < r) {
                this.hitStatus = true;
                return;
            }

        } else if (x >= 0 && y >= 0) {
            double dist = Math.sqrt(x*x+y*y);
            if (dist<r/2d) {
                this.hitStatus = true;
                return;
            }
        } else if (x >= 0 && y <= 0) {
            this.hitStatus = (Math.abs(y)<=r/2d
                    && x<=r
                    && y>=x/2-r/2d);
            return;
        }
        this.hitStatus = false;

    }
}
