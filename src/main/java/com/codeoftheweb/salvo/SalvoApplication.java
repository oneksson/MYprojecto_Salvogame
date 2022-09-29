
package com.codeoftheweb.salvo;

import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer{
    @Autowired
    PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
	}

    @Bean
    public CommandLineRunner initData(GameRepository    gameRepository,
                                      PlayerRepository playerRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      SalvoRepository salvoRepository,
                                      ScoreRepository scoreRepository
                                      ){
        return     (args) -> {

//--------------------------------------------------GAMES----------------------------------------------------
            Game game1 = new Game( new Date() );
            gameRepository.save ( game1 );

            Game game2 = new Game( new Date() );
            gameRepository.save ( game2 );

            Game game3 = new Game( new Date() );
            gameRepository.save ( game3 );

            Game game4 = new Game( new Date() );
            gameRepository.save ( game4 );

            Game game5 = new Game( new Date() );
            gameRepository.save ( game5 );



//------------ -------------------------------------PLAYER1------------ -------------------------------------
            Player player1 = new Player("p1@hotmail.com",passwordEncoder.encode("password"));
            playerRepository.save(player1);

            //                                   GAMEPLAYER JUGADOR 1
            GamePlayer  gamePlayer1 =   new GamePlayer(new Date(),game1,player1);
            gamePlayerRepository.save(gamePlayer1);


//                                              SHIPS OF PLAYER1
/*            Integer cont = 0 ;
            do {
                String locacion ;

                cont++;
            }while ( cont < 6   );
*/
            List<String> location1 = new ArrayList<>();
            location1.add("H1");
            location1.add("H2");
            location1.add("H3");
            location1.add("H4");
            location1.add("H5");

            List<String> location2 = new ArrayList<>();
            location2.add("E1");
            location2.add("E2");
            location2.add("E3");
            location2.add("E4");

            List<String> location3 = new ArrayList<>();
            location3.add("A1");
            location3.add("A2");
            location3.add("A3");

            List<String> location4 = new ArrayList<>();
            location4.add("G7");
            location4.add("G8");
            location4.add("G9");

            List<String> location5 = new ArrayList<>();
            location5.add("D4");
            location5.add("D5");

            Ship ship1 = new Ship( location1, gamePlayer1,"carrier");
            Ship ship2 = new Ship( location2, gamePlayer1, "battleship");
            Ship ship3 = new Ship( location3, gamePlayer1, "submarine");
            Ship ship4 = new Ship( location4, gamePlayer1, "destroyer");
            Ship ship5 = new Ship( location5, gamePlayer1, "patrolboat");

            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);
            shipRepository.save(ship5);





//                                              SALVO OF PLAYER1
            List<String> ubication = new ArrayList<>();
            ubication.add("B4");
            ubication.add("A2");
            ubication.add("A3");
            ubication.add("G3");
            ubication.add("F7");



            Salvo salvo1 = new Salvo(gamePlayer1,1,ubication);
            salvoRepository.save((salvo1));


            List<String> ubication3 = new ArrayList<>();
            ubication3.add("F1");
            ubication3.add("J10");
            ubication3.add("J9");
            ubication3.add("J8");
            ubication3.add("B3");



            Salvo salvo3 = new Salvo(gamePlayer1,2,ubication3);
            salvoRepository.save((salvo3));

//                                               SCORE

            Score score1 = new Score(player1,game1,(float)1, new Date());
            scoreRepository.save(score1);

            Score score2 = new Score(player1,game2,(float)0.5, new Date());
            scoreRepository.save(score2);

            Score score3 = new Score(player1,game3,0, new Date());
            scoreRepository.save(score3);

            Score score4 = new Score(player1,game4,0, new Date());
            scoreRepository.save(score4);

            Score score5 = new Score(player1,game5,0, new Date());
            scoreRepository.save(score5);

//----------------------------------------------PLAYER2-------------------------------------------
            Player player2 = new Player("p2@hotmail.com",passwordEncoder.encode("password2"));
            playerRepository.save(player2);
//                                            GAMEPLAYER2

            GamePlayer  gamePlayer2 =   new GamePlayer(new Date(),game1,player2);
            gamePlayerRepository.save(gamePlayer2);


//                                          SHIPS OF PLAYER2


            List<String> location6 = new ArrayList<>();
            location6.add("H1");
            location6.add("H2");
            location6.add("H3");
            location6.add("H4");
            location6.add("H5");

            List<String> location7 = new ArrayList<>();
            location7.add("E1");
            location7.add("E2");
            location7.add("E3");
            location7.add("E4");

            List<String> location8 = new ArrayList<>();
            location8.add("A1");
            location8.add("A2");
            location8.add("A3");

            List<String> location9 = new ArrayList<>();
            location9.add("B4");
            location9.add("B5");
            location9.add("B6");

            List<String> location10 = new ArrayList<>();
            location10.add("C4");
            location10.add("C5");

            Ship ship6 = new Ship( location6, gamePlayer2,"carrier");
            Ship ship7 = new Ship( location7, gamePlayer2, "battleship");
            Ship ship8 = new Ship( location8, gamePlayer2, "submarine");
            Ship ship9 = new Ship( location9, gamePlayer2, "destroyer");
            Ship ship10 = new Ship( location10, gamePlayer2, "patrolboat");


            shipRepository.save(ship6);
            shipRepository.save(ship7);
            shipRepository.save(ship8);
            shipRepository.save(ship9);
            shipRepository.save(ship10);



//                                          SALVO OF PLAYER2
            List<String> ubication1 = new ArrayList<>();
            ubication1.add("D3");
            ubication1.add("D3");
            ubication1.add("D5");

            Salvo salvo2 = new Salvo(gamePlayer2,1,ubication1);
            salvoRepository.save(salvo2);


            List<String> ubication4 = new ArrayList<>();
            ubication4.add("D3");
            ubication4.add("D4");
            ubication4.add("D5");
            ubication4.add("D6");
            ubication4.add("D7");

            Salvo salvo4 = new Salvo(gamePlayer2,2,ubication4);
            salvoRepository.save(salvo4);

//                                               SCORE



            Score score6 = new Score(player2,game1,(float)0.0, new Date());
            scoreRepository.save(score6);

            Score score7 = new Score(player2,game2,0, new Date());
            scoreRepository.save(score7);

            Score score8 = new Score(player2,game3,0, new Date());
            scoreRepository.save(score8);

            Score score9 = new Score(player2,game4,1, new Date());
            scoreRepository.save(score9);

            Score score10 = new Score(player2,game5,1, new Date());
            scoreRepository.save(score10);


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                                        //TERMINA EL JUEGO 1






//------------ -------------------------------------PLAYER3------------ -------------------------------------
            Player player3 = new Player("p3@hotmail.com",passwordEncoder.encode("password3"));
            playerRepository.save(player3);

            //                                   GAMEPLAYER3
            GamePlayer  gamePlayer33 =   new GamePlayer(new Date(),game2,player3);
            gamePlayerRepository.save(gamePlayer33);

//                                              SHIPS OF PLAYER 3

            List<String> location31 = new ArrayList<>();
            location31.add("E1");
            location31.add("E2");
            location31.add("E3");
            location31.add("E4");
            location31.add("E5");

            List<String> location32 = new ArrayList<>();
            location32.add("A1");
            location32.add("A2");
            location32.add("A3");
            location32.add("A4");

            List<String> location33 = new ArrayList<>();
            location33.add("G7");
            location33.add("G8");
            location33.add("G9");

            List<String> location34 = new ArrayList<>();
            location34.add("H1");
            location34.add("H2");
            location34.add("H3");

            List<String> location35 = new ArrayList<>();
            location35.add("C1");
            location35.add("C2");


            Ship ship31 = new Ship( location31, gamePlayer33,"carrier");
            Ship ship32 = new Ship( location32, gamePlayer33, "battleship");
            Ship ship33 = new Ship( location33, gamePlayer33, "destroyer");
            Ship ship34 = new Ship( location34, gamePlayer33, "submarine");
            Ship ship35 = new Ship( location35, gamePlayer33, "patrolboat");
            shipRepository.save(ship31);
            shipRepository.save(ship32);
            shipRepository.save(ship34);
            shipRepository.save(ship33);
            shipRepository.save(ship35);



//                                              SALVO OF PLAYER3
            List<String> ubication30 = new ArrayList<>();
            ubication30.add("B4");
            ubication30.add("A2");
            ubication30.add("A3");
            ubication30.add("G3");
            ubication30.add("F7");
            ubication30.add("D3");


            Salvo salvo33 = new Salvo(gamePlayer33,3,ubication30);
            salvoRepository.save((salvo33));

//                                               SCORE

            Score score31 = new Score(player3,game1,(float)1, new Date());
            scoreRepository.save(score31);

            Score score32 = new Score(player3,game2,(float)0.5, new Date());
            scoreRepository.save(score32);

            Score score33 = new Score(player3,game3,0, new Date());
            scoreRepository.save(score33);

            Score score34 = new Score(player3,game4,0, new Date());
            scoreRepository.save(score34);

            Score score35 = new Score(player3,game5,0, new Date());
            scoreRepository.save(score35);


//------------ -------------------------------------PLAYER4------------ -------------------------------------
            Player player4 = new Player("p4@hotmail.com",passwordEncoder.encode("password4"));
            playerRepository.save(player4);

            //                                   GAMEPLAYER4
            GamePlayer  gamePlayer44 =   new GamePlayer(new Date(),game2,player4);
            gamePlayerRepository.save(gamePlayer44);

//                                              SHIPS OF PLAYER4

            List<String> location41 = new ArrayList<>();
            location41.add("E1");
            location41.add("E2");
            location41.add("E3");
            location41.add("E4");
            location41.add("E5");

            List<String> location42 = new ArrayList<>();
            location42.add("A1");
            location42.add("A2");
            location42.add("A3");
            location42.add("A4");

            List<String> location43 = new ArrayList<>();
            location43.add("B7");
            location43.add("B8");
            location43.add("B9");

            List<String> location44 = new ArrayList<>();
            location44.add("H1");
            location44.add("H2");
            location44.add("H3");

            List<String> location45 = new ArrayList<>();
            location45.add("D7");
            location45.add("C7");


            Ship ship41 = new Ship( location41, gamePlayer44,"carrier");
            Ship ship42 = new Ship( location42, gamePlayer44, "battleship");
            Ship ship43 = new Ship( location43, gamePlayer44, "destroyer");
            Ship ship44 = new Ship( location44, gamePlayer44, "submarine");
            Ship ship45 = new Ship( location45, gamePlayer44, "patrolboat");
            shipRepository.save(ship41);
            shipRepository.save(ship42);
            shipRepository.save(ship43);
            shipRepository.save(ship44);
            shipRepository.save(ship45);



//                                              SALVO OF PLAYER4
            List<String> ubication40 = new ArrayList<>();
            ubication40.add("B4");
            ubication40.add("A2");
            ubication40.add("A3");
            ubication40.add("G3");
            ubication40.add("F7");

            Salvo salvo44 = new Salvo(gamePlayer44,3,ubication40);
            salvoRepository.save((salvo44));

//                                               SCORE

            Score score41 = new Score(player4,game1,(float)1, new Date());
            scoreRepository.save(score41);

            Score score42 = new Score(player4,game2,(float)0.5, new Date());
            scoreRepository.save(score42);

            Score score43 = new Score(player4,game3,0, new Date());
            scoreRepository.save(score43);

            Score score44 = new Score(player4,game4,0, new Date());
            scoreRepository.save(score44);

            Score score45 = new Score(player4,game5,0, new Date());
            scoreRepository.save(score45);


//------------ -------------------------------------PLAYER5------------ -------------------------------------
            Player player5 = new Player("p5@hotmail.com",passwordEncoder.encode("password5"));
            playerRepository.save(player5);


            //                                   GAMEPLAYER DEL JUGADOR 5
            GamePlayer  gamePlayer55 =   new GamePlayer(new Date(),game3,player5);
            gamePlayerRepository.save(gamePlayer55);

//------------ -------------------------------------PLAYER6------------ -------------------------------------
            Player player6 = new Player("p6@hotmail.com",passwordEncoder.encode("password6"));
            playerRepository.save(player6);


            //                                   GAMEPLAYER6
            GamePlayer  gamePlayer66 =   new GamePlayer(new Date(),game5,player6);
            gamePlayerRepository.save(gamePlayer66);




        };// FIN DEL ARCHIVO return(args); Y DE LA CARGA DE JUGADORES
    }






    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }


}//FIN DEL ARCHIVO SalvoApplication




@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {


    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputUserName -> {
            Player player = playerRepository.findByUserName(inputUserName);
            if (player != null) {
                //if (player.getUserName().equals("David")) {
                    return new User(player.getUserName(),player.getPassword(),AuthorityUtils.createAuthorityList("USER"));
                }
                else {
                    throw new UsernameNotFoundException("Unknown user: " + inputUserName);
                }
            }
            /*else {
                return new User(player.getUserName(), passwordEncoder().encode(player.getPassword()),
                        AuthorityUtils.createAuthorityList("USER"));
            }*/
        //}
        );
    }
}//FIN DEL ARCHIVO WebSecurityConfiguration




@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                //.antMatchers("/api/**").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("USER")
                .antMatchers("/web/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .and()
                .formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }


        private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }


        }



}// FIN DE WebSecurityConfig





