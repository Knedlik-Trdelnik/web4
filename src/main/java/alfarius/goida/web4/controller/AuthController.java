package alfarius.goida.web4.controller;


import alfarius.goida.web4.exceptions.NoSuchLoginException;
import alfarius.goida.web4.models.LoginRequest;
import alfarius.goida.web4.repository.UserRepository;
import io.helidon.security.jwt.JwtUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import alfarius.goida.web4.models.User;

//ssh -L 5432:localhost:5432 s467969@helios.cs.ifmo.ru -p 2222
@Path("/auth")
@ApplicationScoped
@PermitAll
public class AuthController {
    @Inject
    UserRepository userRepository;

    @POST()
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(LoginRequest loginRequest) {
        String name = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println(name);
        System.out.println(password);
        User user = null;
        try {
            user = userRepository.findUserByLogin(name);
            if(password.equals(user.getPassword())) {
                return Response.ok(user).build();
            }

        } catch (NoSuchLoginException e) {
            System.out.println("Ебать у него нет имени хахахахха");
           return Response.status(418).build();
        }
        System.out.println("Ебать он тупой хааххахаха");
        return Response.status(418).build();
    }




}
