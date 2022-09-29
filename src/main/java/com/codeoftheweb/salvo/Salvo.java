package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int turno;

    @ElementCollection
    @JoinColumn( name = "ubication")
    List<String> ubication;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo(){}

    public Salvo( GamePlayer gamePlayer, int turno, List<String> ubication){
        this.gamePlayer = gamePlayer;
        this.turno = turno;
        this.ubication = ubication;
    }

    public void setTurno( int turno){
        this.turno = turno;
    }
    public int getTurno(){
        return turno;
    }
    public void setId( long id ){
        id = id ;
    }
    public long getId(){
        return id;
    }

    public void setUbication( List<String> ubication ){
        this.ubication = ubication;
    }
    public List<String> getUbication(){
        return ubication;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
