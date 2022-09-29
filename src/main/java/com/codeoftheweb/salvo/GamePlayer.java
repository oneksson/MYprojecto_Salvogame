package com.codeoftheweb.salvo;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private Date creationdate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    /*
    * Aqui tenemos una relacion N - 1 de GamePlayer con Game
    * Usamos el @JoinColumn para decir que la relacion se hace por medio de game_id que hace referencia al id de Game
    * */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    //estoy en duda con este ManyToMany ---->

    @OneToMany(mappedBy = "gamePlayer",fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    //@JoinColumn(name="ship_id")s
    private List<Ship> ships = new ArrayList<>();


    //<------DUDA

    public GamePlayer() { }

    @OneToMany(mappedBy = "gamePlayer",fetch = FetchType.EAGER)
    private List<Salvo>  salvoes =   new ArrayList<>();


    public GamePlayer(Date creationdate, Game game, Player player) {
        this.creationdate   =   creationdate;
        this.game   =   game;
        this.player =   player;
    }

    //ID
    public long getId() {return Id; }

    public void setId(long id) {
        Id = id;
    }

    //PLAYER
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    //GAME
    public Game getGame() { return game; }

    public void setGame(Game game) {
        this.game = game;
    }

    //CREATIONDATE
    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    //SHIP
    public List<Ship> getShip() {
        return ships;
    }

    public void setShip(List<Ship> ships) {
        this.ships = ships;
    }

    //SALVO
    public List<Salvo> getSalvos() {
        return salvoes;
    }

    public void setSalvos(List<Salvo> salvos) {
        this.salvoes = salvos;
    }

}


