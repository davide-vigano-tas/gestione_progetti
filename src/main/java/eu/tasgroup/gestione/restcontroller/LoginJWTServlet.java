package eu.tasgroup.gestione.restcontroller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;
import eu.tasgroup.gestione.businesscomponent.security.JwtUtil;

public class LoginJWTServlet extends HttpServlet {

    private static final long serialVersionUID = 1844901520193025238L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recupera username e password dai parametri della richiesta
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Imposta la risposta come JSON
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            // Verifica che i parametri non siano nulli
            if (username == null || password == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\": \"Username e/o Password mancanti\"}");
                return;
            }

            UserBC userBC = new UserBC();
            User user = userBC.getByUsername(username);

            // Controlla se l'utente esiste
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\": \"Username non valido\"}");
                return;
            }

            // Controlla se l'account è bloccato
            if (user.isLocked()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"error\": \"Account bloccato\"}");
                return;
            }

            // Verifica la password
            if (!Algoritmo.verificaPassword(password, user.getPassword())) {
                // Incrementa i tentativi falliti
                user.setTentativiFalliti(user.getTentativiFalliti() + 1);

                // Blocca l'account se necessario
                if (user.getTentativiFalliti() >= 5) {
                    user.setLocked(true);
                }

                // Aggiorna l'utente nel database
                userBC = new UserBC();
                userBC.createOrUpdate(user);

                // Risposta di errore
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"error\": \"Password errata\"}");
                return; // Impedisce ulteriori esecuzioni
            }

            // Reset tentativi falliti in caso di login corretto
            if (user.getTentativiFalliti() > 0) {
                user.setTentativiFalliti(0);
                userBC = new UserBC();
                userBC.createOrUpdate(user);
            }

            // Genera il token JWT
            JwtUtil jwtUtil = new JwtUtil();
            String token = jwtUtil.generateToken(username);

            // Restituisci il token come risposta JSON
            response.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"token\": \"" + token + "\"}");
        } catch (IOException e) {
            // Gestisce errori di input/output
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Errore interno del server\"}");
            e.printStackTrace();
        } catch (Exception e) {
            // Gestisce altre eccezioni generiche
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Errore imprevisto\"}");
            e.printStackTrace();
        }
    }
}
