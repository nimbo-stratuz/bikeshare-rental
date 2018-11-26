package si.nimbostratuz.bikeshare.api.v1.mappers;

import si.nimbostratuz.bikeshare.api.v1.dtos.ApiStatusDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
@ApplicationScoped
public class InternalServerErrorExceptionMapper implements ExceptionMapper<InternalServerErrorException> {

    @Override
    public Response toResponse(InternalServerErrorException e) {

        ApiStatusDTO apiStatusDTO = new ApiStatusDTO(e.getResponse().getStatus(), e.getMessage());

        return apiStatusDTO.asResponse();
    }
}
