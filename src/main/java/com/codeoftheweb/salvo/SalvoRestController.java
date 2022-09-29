package com.codeoftheweb.salvo;

import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.type.MapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.expression.MapAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.awt.*;
import java.sql.SQLTransactionRollbackException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping( "/api"   )

public class SalvoRestController {

    @Autowired
    private GameRepository gameRepository;


    //    @RequestMapping("/games")
    public List<Map> getGame() {
        //List<Game> games    =   gameRepository.findAll();
        return gameRepository
                //games.stream()
                .findAll()
                .stream()
                .map(game -> GameDTO(game))
                .collect(Collectors.toList());
    }

    private Map<String, Object> GameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", gamePlayerList(game.getGamePlayer()));
        dto.put("scores", ScoreList(game.getScores()));

        return dto;
    }

    private List<Map<String, Object>> gamePlayerList(List<GamePlayer> gamePlayers) {
        return gamePlayers.stream()
                .map(gamePlayer -> gamePlayerDTO(gamePlayer))
                .collect(Collectors.toList());
    }

    private Map<String, Object> gamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", playerDTO(gamePlayer.getPlayer())); //Aqui traemos los jugadores relacionados con el juegoPlayer
        dto.put("joinDate", gamePlayer.getCreationdate());
        return dto;
    }

    private Map<String, Object> playerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        return dto;
    }


    //+++++++++++++++++++++++++++++++++++++++++++++GAME VIEW ID++++++++++++++++++++++++++++++++++++++++
    // NUEVO DTO
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

//    @RequestMapping("/game_view/{id}")
    public Map<String, Object> getGameView(@PathVariable long id) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(id).get();

        return gameViewDTO(gamePlayer);

    }

    private Map<String, Object> gameViewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", gamePlayerList(gamePlayer.getGame().getGamePlayer()));
        dto.put("ships", ShipList(gamePlayer.getShip()));
        dto.put("salvoes", getSalvoList(gamePlayer.getGame()));


        return dto;
    }
    // LISTA DE HITS
/*    private List<Map<String,Object>> SelfList(List<Self> self){

    }
*/



    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //LISTA DE SHIP
    private List<Map<String, Object>> ShipList(List<Ship> ships) {
        return ships.stream()
                .map(ship -> ShipDTO(ship))
                .collect(Collectors.toList());
    }


    private Map<String, Object> ShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocation());
        return dto;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //LISTA DE SALVO
    private List<Map<String, Object>> SalvoList(List<Salvo> salvos) {
        return salvos.stream()
                .map(salvo -> SalvoDTO(salvo))
                .collect(Collectors.toList());

    }

    private Map<String, Object> SalvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", salvo.getTurno());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", salvo.getUbication());

        return dto;
    }

    private List<Map<String, Object>> getSalvoList(Game game) {

        List<Map<String, Object>> myList = new ArrayList<>();
        game.getGamePlayer().forEach(gamePlayer -> myList.addAll(SalvoList(gamePlayer.getSalvos())));

        return myList;
    }

    //++++++++++++++++++++++++++++++++++++++++++++SCORES+++++++++++++++++++++++++++++++++++++++++++++++
    private List<Map<String, Object>> ScoreList(List<Score> score) {
        return score.stream()
                .map(scores -> ScoresDTO(scores))
                .collect(Collectors.toList());

    }

    private Map<String, Object> ScoresDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("playerID", score.getPlayer().getId());
        dto.put("score", score.getScore());
        dto.put("finishDate", score.getFinishDate());
        return dto;
    }

    @Autowired
    private PlayerRepository playerRepository;

    //++++++++++++++++++++++++++++++++++++++++LEADERBOARD++++++++++++++++++++++++++++++++++++++++++++++++++
    @RequestMapping("/leaderBoard")
    public List<Map<String, Object>> getScore() {
        return playerRepository
                .findAll()
                .stream()
                .map(player -> leaderboardDTO(player))
                .collect(Collectors.toList());

    }

    private Map<String, Object> leaderboardDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        dto.put("scores", getPlayerTotalScore(player));
        dto.put("won", getPlayerWon(player));
        dto.put("lost", getPlayerLost(player));
        dto.put("tied", getPlayerTied(player));
        return dto;
    }

    private double getPlayerTotalScore(Player player) {
        return player.getScores()
                .stream()
                .mapToDouble(s -> s.getScore()).sum();
    }

    private long getPlayerWon(Player player) {
        return player.getScores()
                .stream()
                .filter(s -> s.getScore() == 1).count();
    }

    private long getPlayerLost(Player player) {
        return player.getScores()
                .stream()
                .filter(s -> s.getScore() == 0).count();
    }

    private long getPlayerTied(Player player) {
        return player.getScores()
                .stream()
                .filter(s -> s.getScore() == 0.5).count();
    }
/*
    private Map<String,Object> unalista (List<GamePlayer> gamePlayers){
        return
                gamePlayers
                .stream()
                .map(mivariable -> )
                .collect(toList());

    }

*/
//++++++++++++++++++++++++++++++++++++++++++++PLAYERS++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(email) != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.FORBIDDEN);
        }


        Player player = playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        Game newGame = gameRepository.save(new Game());
        GamePlayer newGameplayer = gamePlayerRepository.save(new GamePlayer(new Date(),newGame, player));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    //-----------------------------------------------------------------------------------------------------------------
    @RequestMapping("/games")
    private Map<String, Object> divisordeDTOS(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (isGuest(authentication)) {
            dto.put("player", "Guest");
            dto.put("games", getGame());
            return dto;
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("player", playerDTO(player));
            dto.put("games", getGame());
            return dto;
        }
    }


    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

     @RequestMapping("/game_view/{id}")
     public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long id, Authentication authentication){

         GamePlayer gamePlayer = gamePlayerRepository.findById(id).orElse(null);
         System.out.println(gamePlayer);
         Player player = playerRepository.findByUserName(authentication.getName());
         System.out.println(player);
         if(gamePlayer == null){

             return new ResponseEntity<>(makeMap("error", "Forbidden"), HttpStatus.FORBIDDEN);
         }
         else {

             if(player.getId() != gamePlayer.getPlayer().getId()){

                 return new ResponseEntity<>(makeMap("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
             }
             else{
                 //creo una variable juego cualquiera en base al gameplayer que identifique
                 Game eljuego = gamePlayer.getGame();

                 //de ese juego obtengo la lista de juegos de ese game
                 List<GamePlayer> gamePlayerlista = eljuego.getGamePlayer();

                 //creo una variable gameplayer cualquiera
                 //hago la diferencia con un filtro
                 GamePlayer gamePlayer2 = filtro(gamePlayerlista ,player);
                 if(gamePlayer2 == null){
                     Map<String, Object> dto = new LinkedHashMap<>();
                     dto.put("id", gamePlayer.getGame().getId());
                     dto.put("gameState",getGameState(gamePlayer));
                     dto.put("created", gamePlayer.getGame().getCreationDate());
                     dto.put("gamePlayers", getPlayerList(gamePlayer.getGame().getGamePlayer()));
                     dto.put("ships", ShipList(gamePlayer.getShip()));
                     System.out.println("jojo feliz navidad");
                     dto.put("salvoes", new ArrayList<>());
                     dto.put("hits",self(gamePlayer,gamePlayer2));
                     return new ResponseEntity<>(dto, HttpStatus.OK);

                 }
                 else{
                     Map<String, Object> dto = new LinkedHashMap<>();
                     dto.put("id", gamePlayer.getGame().getId());
                     dto.put("gameState",getGameState(gamePlayer));
                     dto.put("created", gamePlayer.getGame().getCreationDate());
                     dto.put("gamePlayers", getPlayerList(gamePlayer.getGame().getGamePlayer()));
                     dto.put("ships", ShipList(gamePlayer.getShip()));
                     System.out.println(ShipList(gamePlayer.getShip()));
                     dto.put("salvoes", getSalvoList(gamePlayer.getGame()));
                     dto.put("hits",self(gamePlayer,gamePlayer2));
                     return new ResponseEntity<>(dto, HttpStatus.OK);
                 }


             }
         }


     }//FIN DEL ARCHIVO game_view/{id}
     //getCurrentPlayerByName(authentication.getName())
     //.getGamePlayers()

    private List<Map<String, Object>> getPlayerList(List<GamePlayer> gamePlayers) {
        return gamePlayers.stream()
                .map(gamePlayer -> getPlayerDTO(gamePlayer))
                .collect(Collectors.toList());
    }

    private Map<String, Object> getPlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        //quito el key "gpid"
        //dto.put("gpid", gamePlayer.getId());
        dto.put("id",gamePlayer.getPlayer().getId());
        //cambie el key "name" por el key "player"
        dto.put("player", playerDTO(gamePlayer.getPlayer())); //Aqui traemos los jugadores relacionados con el juegoPlayer
        dto.put("joinDate", gamePlayer.getCreationdate());
        return dto;
    }

    @RequestMapping(path = "/games/{id}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame (@PathVariable long id,Authentication authentication){
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "You can't Join Game"), HttpStatus.UNAUTHORIZED);
        }
        else {
            Game game = gameRepository.findById(id).orElse(null);

            if(game == null){
                return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
            }
            else{
                if(game.getGamePlayer().size() > 2){
                    return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
                }
                else {

                    Player player = playerRepository.findByUserName(authentication.getName());
                    //CREO Y GUARDO UN NUEVO gameplayer
                    GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(new Date(),game, player));
                    return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
                }
            }

        }
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You can't create a New Game if You're Not Logged In! Please Log in or Sign Up for a new player account."), HttpStatus.UNAUTHORIZED);
        } else {
            Game newGame = gameRepository.save(new Game(new Date()));
            Player player = playerRepository.findByUserName(authentication.getName());
            GamePlayer newGameplayer = gamePlayerRepository.save(new GamePlayer(new Date(),newGame, player));
            return new ResponseEntity<>(makeMap("gpid", newGameplayer.getId()), HttpStatus.CREATED);
        }
    }
//-----------------------------------------------PLACED SHIPS------------------------------------------------------------
    @Autowired
    private ShipRepository shipRepository;

    @RequestMapping(path = "/games/players/{gamePlayerID}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setShips(@PathVariable long gamePlayerID, @RequestBody List<Ship> ships , Authentication authentication) {

        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You can't place ships if You're Not Logged In! Please Log in or Sign Up for a new player account."), HttpStatus.UNAUTHORIZED);
        }
        else{
            GamePlayer gamePlayerToPlaceShips = gamePlayerRepository.getOne(gamePlayerID);
            if(gamePlayerToPlaceShips==null){
                    return new ResponseEntity<>(makeMap("error", "do not exist gameplayer"), HttpStatus.UNAUTHORIZED);
            }
            else{
                Player player = playerRepository.findByUserName(authentication.getName());

                if(player.getId() != gamePlayerToPlaceShips.getPlayer().getId()){
                       return new ResponseEntity<>(makeMap("error", "You don't belong to this game"), HttpStatus.UNAUTHORIZED);
                   }
                else {
                    if (gamePlayerToPlaceShips.getShip().size() > 5) {
                        return new ResponseEntity<>(makeMap("error", "Error: Your ships are already placed!"), HttpStatus.FORBIDDEN);
                    }
                    for (Ship item : ships) {
                        item.setGamePlayer(gamePlayerToPlaceShips);
                        shipRepository.save(item);
                    }

                }

            }

        }
        return new ResponseEntity<>(makeMap("OK", "Ship positions saved successfully! "), HttpStatus.CREATED);
    }


//-----------------------------------------------PLACED SALVOES------------------------------------------------------------
    @Autowired
    private SalvoRepository salvoRepository;
    @RequestMapping(path = "/games/players/{gamePlayerID}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setSalvoes(@PathVariable long gamePlayerID, @RequestBody Map<String,Object> salvoObject, Authentication authentication) {

        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You can't see the list of salvoes if You're Not Logged In! Please Log in or Sign Up for a new player account."), HttpStatus.CREATED);
        }
        else{
            GamePlayer gamePlayerToSalvoes = gamePlayerRepository.getOne(gamePlayerID);
            if(gamePlayerToSalvoes==null){
                return new ResponseEntity<>(makeMap("error", "do not found the gameplayer"), HttpStatus.UNAUTHORIZED);
            }
            else{
                Player player = playerRepository.findByUserName(authentication.getName());

                if(player.getId() != gamePlayerToSalvoes.getPlayer().getId()){
                    return new ResponseEntity<>(makeMap("error", "You don't belong to this game"), HttpStatus.UNAUTHORIZED);
                }
                else {
                    if (gamePlayerToSalvoes.getSalvos().size() >5) {
                        return new ResponseEntity<>(makeMap("error", "Error: Your had aready fired your salvoes!"), HttpStatus.FORBIDDEN);
                    }


                    Salvo salvo = new Salvo( gamePlayerToSalvoes, ((int)salvoObject.get("turno")) , ((List<String>)salvoObject.get("salvoLocations")));
                    salvoRepository.save(salvo);




                }

            }

        }
        return new ResponseEntity<>(makeMap("OK", "Salvoes positions saved successfully! "), HttpStatus.CREATED);
    }



//                                                   COMPARACION
private List<Map> getHits(GamePlayer gamePlayer, GamePlayer opponentGameplayer) {
    //creo una lista de mapas llamada HITS
    List<Map> hits = new ArrayList<>();

    //creo variables enteras nulas
    Integer carrierDamage = 0;
    Integer battleshipDamage = 0;
    Integer submarineDamage = 0;
    Integer destroyerDamage = 0;
    Integer patrolboatDamage = 0;

    //creación de listas
    List <String> carrierLocation = new ArrayList<>();
    List <String> battleshipLocation = new ArrayList<>();
    List <String> submarineLocation = new ArrayList<>();
    List <String> destroyerLocation = new ArrayList<>();
    List <String> patrolboatLocation = new ArrayList<>();

    //según un ship lo que hago es agregar a la listas creadas arriba la ubicación de mis propios barcos según el tipo
    gamePlayer.getShip().forEach(ship -> {
        switch (ship.getType()) {
            case "carrier":
                carrierLocation.addAll(ship.getLocation());
                break;
            case "battleship":
                battleshipLocation.addAll(ship.getLocation());
                break;
            case "submarine":
                submarineLocation.addAll(ship.getLocation());
                break;
            case "destroyer":
                destroyerLocation.addAll(ship.getLocation());
                break;
            case "patrolboat":
                patrolboatLocation.addAll(ship.getLocation());
                break;
        }
    });
    //con este FOR lo que hago es
    for (Salvo salvo : opponentGameplayer.getSalvos()) {
        //creo nuevas variables enteras de hits iniciadas cero
        Integer carrierHitsInTurn = 0;
        Integer battleshipHitsInTurn = 0;
        Integer submarineHitsInTurn = 0;
        Integer destroyerHitsInTurn = 0;
        Integer patrolboatHitsInTurn = 0;
        //creo una variable entera llamada tiros perdidos y guardo en ella el tamaño de las ubicaciones de mi salvas
        Integer missedShots = salvo.getUbication().size();

        //creo un mapa con no se què
        Map<String, Object> hitsMapPerTurn = new LinkedHashMap<>();
        Map<String, Object> damagesPerTurn = new LinkedHashMap<>();

        //creo nuevas listas
        List<String> salvoLocationsList = new ArrayList<>();
        List<String> hitCellsList = new ArrayList<>();

        //dentro de la lista que cree guardo la ubicación de mis salvas
        salvoLocationsList.addAll(salvo.getUbication());

        for (String salvoShot : salvoLocationsList) {
            if (carrierLocation.contains(salvoShot)) {
                carrierDamage++;
                carrierHitsInTurn++;
                hitCellsList.add(salvoShot);
                missedShots--;
            }
            if (battleshipLocation.contains(salvoShot)) {
                battleshipDamage++;
                battleshipHitsInTurn++;
                hitCellsList.add(salvoShot);
                missedShots--;
            }
            if (submarineLocation.contains(salvoShot)) {
                submarineDamage++;
                submarineHitsInTurn++;
                hitCellsList.add(salvoShot);
                missedShots--;
            }
            if (destroyerLocation.contains(salvoShot)) {
                destroyerDamage++;
                destroyerHitsInTurn++;
                hitCellsList.add(salvoShot);
                missedShots--;
            }
            if (patrolboatLocation.contains(salvoShot)) {
                patrolboatDamage++;
                patrolboatHitsInTurn++;
                hitCellsList.add(salvoShot);
                missedShots--;
            }
        }

        damagesPerTurn.put("carrierHits", carrierHitsInTurn);
        damagesPerTurn.put("battleshipHits", battleshipHitsInTurn);
        damagesPerTurn.put("submarineHits", submarineHitsInTurn);
        damagesPerTurn.put("destroyerHits", destroyerHitsInTurn);
        damagesPerTurn.put("patrolboatHits", patrolboatHitsInTurn);
        damagesPerTurn.put("carrier", carrierDamage);
        damagesPerTurn.put("battleship", battleshipDamage);
        damagesPerTurn.put("submarine", submarineDamage);
        damagesPerTurn.put("destroyer", destroyerDamage);
        damagesPerTurn.put("patrolboat", patrolboatDamage);
        hitsMapPerTurn.put("turn", salvo.getTurno());
        hitsMapPerTurn.put("hitLocations", hitCellsList);
        hitsMapPerTurn.put("damages", damagesPerTurn);
        hitsMapPerTurn.put("missed", missedShots);
        hits.add(hitsMapPerTurn);
    }//FIN DEL LOOPING FOR

    return hits;

}


    private Map<String, Object> self(GamePlayer gamePlayer, GamePlayer opponentGameplayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if( opponentGameplayer != null){
            dto.put("self", getHits(gamePlayer, opponentGameplayer));
            dto.put("opponent",getHits(opponentGameplayer, gamePlayer));
        }
        else{
            dto.put("self", new ArrayList<>());
            dto.put("opponent",new ArrayList<>());

        }


        return dto;
    }
//-------------------------------------------CONSEGUIR_OPONENTE-----------------------------------------------------------------------
    private GamePlayer filtro(List<GamePlayer> gamePlayerlista, Player player1){
        GamePlayer gamePlayer = gamePlayerlista
                .stream()
                .filter(s -> s.getPlayer().getId() != player1.getId())
                .findFirst().orElse(null);;

        return gamePlayer;

    }

//-------------------------------------------ESTADO DE JUEGO-----------------------------------------------------------------------
@Autowired
private ScoreRepository scoreRepository;

    private String getGameState (GamePlayer gamePlayer) {

        if (gamePlayer.getShip().size() == 0) {
            return "PLACESHIPS";
        }
        if (gamePlayer.getGame().getGamePlayer().size() == 1){
            return "WAITINGFOROPP";
        }
        if (gamePlayer.getGame().getGamePlayer().size() == 2) {

            GamePlayer opponentGp = filtro(gamePlayer.getGame().getGamePlayer(), gamePlayer.getPlayer());

            if ((gamePlayer.getSalvos().size() == opponentGp.getSalvos().size()) && (getIfAllSunk(opponentGp, gamePlayer)) && (!getIfAllSunk(gamePlayer, opponentGp))) {
                Score score = new Score(gamePlayer.getPlayer() ,gamePlayer.getGame() ,(float)1.0, new Date());
                scoreRepository.save(score);

                return "WON";
            }
            if ((gamePlayer.getSalvos().size() == opponentGp.getSalvos().size()) && (getIfAllSunk(opponentGp, gamePlayer)) && (getIfAllSunk(gamePlayer, opponentGp))) {
                Score score = new Score(gamePlayer.getPlayer() ,gamePlayer.getGame() ,(float)0.5, new Date());
                scoreRepository.save(score);

                return "TIE";
            }
            if ((gamePlayer.getSalvos().size() == opponentGp.getSalvos().size()) && (!getIfAllSunk(opponentGp, gamePlayer)) && (getIfAllSunk(gamePlayer, opponentGp))) {
                Score score = new Score(gamePlayer.getPlayer() ,gamePlayer.getGame() ,(float)0.0, new Date());
                scoreRepository.save(score);

                return "LOST";
            }

            if ((gamePlayer.getSalvos().size() == opponentGp.getSalvos().size()) && (gamePlayer.getId() < opponentGp.getId())) {
                return "PLAY";
            }
            if (gamePlayer.getSalvos().size() < opponentGp.getSalvos().size()){
                return "PLAY";
            }
            if ((gamePlayer.getSalvos().size() == opponentGp.getSalvos().size()) && (gamePlayer.getId() > opponentGp.getId())) {
                return "WAIT";
            }
            if (gamePlayer.getSalvos().size() > opponentGp.getSalvos().size()){
                return "WAIT";
            }

        }
        return "UNDEFINED";
    }

    //-------------------------------------------HUNDIDO!!!-----------------------------------------------------------------------
/*
    private boolean getIfAllSunk(GamePlayer opponentGp, GamePlayer gamePlayer){


        if((getHits(opponentGp, gamePlayer).damagesPerTurn.carrierDamage==5) && (getHits( opponentGp, gamePlayer).battleshipDamage==4)  && (getHits(opponentGp, gamePlayer).submarineDamage==3) && (getHits( opponentGp, gamePlayer).destroyerDamage==3) && (getHits(opponentGp, gamePlayer).patrolboatDamage==2)){
            return true;
        }
        getHits(opponentGp,gamePlayer).damages


        return false;
    }
*/
    private Boolean getIfAllSunk (GamePlayer gamePlayer, GamePlayer opponentGameplayer) {

        Integer carrierDamage = 0;
        Integer battleshipDamage = 0;
        Integer submarineDamage = 0;
        Integer destroyerDamage = 0;
        Integer patrolboatDamage = 0;

        List <String> carrierLocation = new ArrayList<>();
        List <String> battleshipLocation = new ArrayList<>();
        List <String> submarineLocation = new ArrayList<>();
        List <String> destroyerLocation = new ArrayList<>();
        List <String> patrolboatLocation = new ArrayList<>();

        gamePlayer.getShip().forEach(ship -> {
            switch (ship.getType()) {
                case "carrier":
                    carrierLocation.addAll(ship.getLocation());
                    break;
                case "battleship":
                    battleshipLocation.addAll(ship.getLocation());
                    break;
                case "submarine":
                    submarineLocation.addAll(ship.getLocation());
                    break;
                case "destroyer":
                    destroyerLocation.addAll(ship.getLocation());
                    break;
                case "patrolboat":
                    patrolboatLocation.addAll(ship.getLocation());
                    break;
            }
        });

        for (Salvo salvo : opponentGameplayer.getSalvos()) {


            List<String> salvoLocationsList = new ArrayList<>();
            salvoLocationsList.addAll(salvo.getUbication());
            for (String salvoShot : salvoLocationsList) {
                if (carrierLocation.contains(salvoShot)) {
                    carrierDamage++;
                }
                if (battleshipLocation.contains(salvoShot)) {
                    battleshipDamage++;
                }
                if (submarineLocation.contains(salvoShot)) {
                    submarineDamage++;
                }
                if (destroyerLocation.contains(salvoShot)) {
                    destroyerDamage++;
                }
                if (patrolboatLocation.contains(salvoShot)) {
                    patrolboatDamage++;
                }
            }
        }

        return (carrierDamage == 5) && (battleshipDamage == 4) && (submarineDamage == 3) && (destroyerDamage == 3) && (patrolboatDamage == 2);
    }


    private int getCurrentTurn(GamePlayer selfGP, GamePlayer opponentGP){
        int selfGPSalvoes = selfGP.getSalvos().size();
        int opponentGPSalvoes = opponentGP.getSalvos().size();

        int totalSalvoes = selfGPSalvoes + opponentGPSalvoes;

        if(totalSalvoes % 2 == 0)
            return totalSalvoes / 2 + 1;

        return (int) (totalSalvoes / 2.0 + 0.5);
    }



}//FIN DEL ARCHIVO


/*
    @RequestMapping(value = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Object> addSalvoes(@PathVariable long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        if (!isGuest(authentication)) {
            GamePlayer gamePlayer = gamePlayerRepo.getOne(gamePlayerId);
            Player player = playerRepo.findByUserName(authentication.getName());

            if (gamePlayer != null) {//si no es un invitado
                if (gamePlayer.getPlayer().getId() == player.getId()) {//si coinciden los players

                    salvo.setTurnNumber(gamePlayer.getGame().getPlayerTurn(player));
                    if (salvo.getTurnNumber() != (-1)) {
                        salvo.setGamePlayer(gamePlayer);
                        salvoRepo.save(salvo);
                        gamePlayer.addSalvo(salvo);
                        gamePlayerRepo.save(gamePlayer);
                        response.put("error", "salvo placed");
                        return new ResponseEntity<>(response, HttpStatus.CREATED);
                    } else {
                        response.put("error", "player already place the salvoes on this turn");
                        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
                    }
                } else {//si no coinciden los players
                    response.put("error", "player dont belong to this game");
                    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
                }

            } else {
                response.put("error", "no such gamePlayer");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } else {
            response.put("error", "no autenticado");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }*/