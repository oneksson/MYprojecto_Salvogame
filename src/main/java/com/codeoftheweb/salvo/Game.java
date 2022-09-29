package com.codeoftheweb.salvo;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private Date creationDate;

    /*Aqui tenemos una relacion 1 - N por o cual debemos asignar a mappeBy el nombre del atributo que estamos usando en GamePlayer para
    * para lograr una relacion bidireccional
    * FetchType.EAGER nos dice que obtenga las entidad relacionada osea los gamePlayers relacionados con este game
    * */
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    List<GamePlayer> gamePlayers ;

    @OneToMany( mappedBy = "game",fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Score> scores = new ArrayList<>();

    /*public float getScores() {
        return scores;
    }

    public void setScores(float scores) {
        this.scores = scores;
    }*/


    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public Game() {
    }

    public Game(Date creationDate) {

        this.creationDate   = creationDate;
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<GamePlayer> getGamePlayer() {
        return gamePlayers;
    }

    public void setGamePlayer(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }


}
