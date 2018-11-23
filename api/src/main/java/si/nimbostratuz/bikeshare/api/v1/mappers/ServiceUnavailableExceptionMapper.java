package si.nimbostratuz.bikeshare.api.v1.mappers;

import si.nimbostratuz.bikeshare.api.v1.dtos.ApiStatusDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ServiceUnavailableExceptionMapper implements ExceptionMapper<ServiceUnavailableException> {

    @Override
    public Response toResponse(ServiceUnavailableException e) {

        ApiStatusDTO apiStatusDTO = new ApiStatusDTO(Response.Status.SERVICE_UNAVAILABLE, e.getMessage());

        return apiStatusDTO.asResponse();
    }
}
