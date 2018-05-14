package com.fun.yzss.resources;

import com.alibaba.fastjson.JSON;
import com.fun.yzss.exception.ValidateException;
import com.fun.yzss.model.movie.Movie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * Created by fanqq on 2016/10/9.
 */
@Component
@Path("/mv")
public class MovResource {
    @POST
    @Path("/add")
    public Response auth(@Context HttpServletRequest request,
                         @Context HttpHeaders hh,
                         String mv) throws Exception {

        Movie movie = JSON.parseObject(mv, Movie.class);
        if (movie == null) {
            throw new ValidateException("Data Illegal.");
        }


        return Response.status(200).entity("suc:").build();
    }
}
