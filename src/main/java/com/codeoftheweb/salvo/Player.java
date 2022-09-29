package com.codeoftheweb.salvo;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.boot.model.source.spi.FetchCharacteristics;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String userName;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private List<GamePlayer> gamePlayers = new ArrayList<>();

    public void addGamePlayer ( GamePlayer gamePlayer ){
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    @OneToMany( mappedBy = "player",fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Score> scores = new ArrayList<>();

   /*
    public float getScores() {
        return scores;
    }

    public void setScores(float scores) {
        this.scores = scores;
    }*/

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> score) {
        this.scores = score;
    }

    public Player() { }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String Name) {
        this.userName = userName;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String toString() {
        return "You username is: " + userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

