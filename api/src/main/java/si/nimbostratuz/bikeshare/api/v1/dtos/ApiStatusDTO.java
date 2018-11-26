package si.nimbostratuz.bikeshare.api.v1.dtos;

import lombok.Data;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

@Data
public class ApiStatusDTO {

    private Integer status;
    private String message;

    public ApiStatusDTO(Response.Status status, String message) {
        this.status = status.getStatusCode();
        this.message = message;
    }

    public ApiStatusDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response asResponse() {
        JsonObject json = Json.createObjectBuilder()
                              .add("status", status)
                              .add("message", message)
                              .build();
        return Response.status(this.status).entity(json.toString()).build();
    }
}
