package alfarius.goida.web4.controller;

import alfarius.goida.web4.repository.DotsRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dots")
@PermitAll
public class DotsController {
    @Inject
    DotsRepository dR;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDots() {
        return Response
                .ok(dR.getDots())
                .build();
    }
}
