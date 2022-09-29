package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn( name = "player_id")
    private Player player;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn( name = "game_id")
    private Game game;

//    private int won;
//    private int lost;
//    private int tied;
    private float score;
    private Date finishDate;

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Score(){}

    public Score(Player player, Game game, float score, Date finishDate) {
        this.player = player;
        this.game = game;
        this.score = score;
        this.finishDate = finishDate;
        }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float total) {
        this.score = score;
    }
}
