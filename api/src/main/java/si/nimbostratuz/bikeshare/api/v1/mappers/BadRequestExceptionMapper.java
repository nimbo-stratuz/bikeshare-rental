package si.nimbostratuz.bikeshare.api.v1.mappers;

import si.nimbostratuz.bikeshare.api.v1.dtos.ApiStatusDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException e) {

        ApiStatusDTO apiStatusDTO = new ApiStatusDTO(Response.Status.BAD_REQUEST, e.getMessage());

        return apiStatusDTO.asResponse();
    }
}
