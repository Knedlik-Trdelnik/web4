package alfarius.goida.web4.controller;

import alfarius.goida.web4.models.Dot;
import alfarius.goida.web4.models.DotsRequest;
import alfarius.goida.web4.models.HitRequest;
import alfarius.goida.web4.models.User;
import alfarius.goida.web4.repository.DotsRepository;
import alfarius.goida.web4.repository.UserRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/dots")
public class DotsController {
    @Inject
    DotsRepository dR;
    @Inject
    UserRepository uR;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getDots(@Context SecurityContext securityContext) {
        return Response
                .ok(dR.getDotsByUser(uR.findUserByLogin(
                        securityContext
                        .getUserPrincipal()
                        .getName()
                        )
                ))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserDot(HitRequest hitRequest, @Context SecurityContext securityContext) {
        long begin = System.nanoTime();
        String username = securityContext.getUserPrincipal().getName();
        System.out.println("Точка от " + username);
        User user = uR.findUserByLogin(username);

        Dot dot = new Dot();
        dot.setR(hitRequest.getR());
        dot.setX(hitRequest.getX());
        dot.setY(hitRequest.getY());
        dot.checkHit();
        dot.setUserId(user.getId());

        long end = System.nanoTime();
        dot.setNano((int) (end - begin));

        dR.addDot(dot);


        System.out.println("Попадание обработано!");
        return Response
                .ok(dot)
                .build();
    }
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/data")
    @POST
    public Response getDotsByPage(DotsRequest dotsRequest, @Context SecurityContext securityContext) {
        return Response
                .ok(dR.getDotsByPagesAndUser(dotsRequest.getPageNumber(),
                        uR.findUserByLogin(
                                securityContext
                                        .getUserPrincipal()
                                        .getName()
                        )
                ))
                .build();
    }
}
