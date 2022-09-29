package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ElementCollection
    @JoinColumn( name = "location ")
    List<String> location;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

/*  public void addGamePlayer ( GamePlayer gamePlayer ){
        gamePlayer.setShip(this);
        gamePlayers.add(gamePlayer);
    }
*/
    public Ship(){}

    public Ship(List<String> location, GamePlayer gamePlayer, String type){
        this.location = location;
        this.gamePlayer = gamePlayer;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        id = id;
    }

    public void setLocation( List<String> location ){
        this.location = location;
    }
    public List<String> getLocation(){
        return location;
    }

    public String getType(){
        return type;
    }

    public void setType( String type){
        this.type = type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

}
