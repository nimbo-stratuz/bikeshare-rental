package si.nimbostratuz.bikeshare.api.v1.mappers;

import si.nimbostratuz.bikeshare.api.v1.dtos.ApiStatusDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {

        ApiStatusDTO apiStatusDTO = new ApiStatusDTO(Response.Status.NOT_FOUND, e.getMessage());

        return apiStatusDTO.asResponse();
    }
}
