package com.function;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import com.function.model.NotificacionRequest;
import com.function.service.NotificacionService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.function.model.GraphQLRequest;
import com.function.model.NotificacionesResponse;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

public class NotificacionesFunction {
    @FunctionName("NotificacionesGraphQL")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS,
            route = "graphql/notificaciones"
        ) HttpRequestMessage<Optional<GraphQLRequest>> request,
        final ExecutionContext context
    ) {
        Logger log = context.getLogger();

        try {
            GraphQLRequest body = request.getBody().orElse(null);

            if (body == null || body.getQuery() == null || body.getQuery().trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                        .header("Content-Type", "application/json")
                        .body(Map.of(
                                "errors", new Object[] {
                                        Map.of("message", "El campo query es obligatorio.")
                                }))
                        .build();
            }

            NotificacionService service = new NotificacionService();

            GraphQLObjectType notificacionType = GraphQLObjectType.newObject()
                    .name("Notificacion")
                    .field(GraphQLFieldDefinition.newFieldDefinition()
                            .name("evento")
                            .type(Scalars.GraphQLString))
                    .field(GraphQLFieldDefinition.newFieldDefinition()
                            .name("canal")
                            .type(Scalars.GraphQLString))
                    .field(GraphQLFieldDefinition.newFieldDefinition()
                            .name("destino")
                            .type(Scalars.GraphQLString))
                    .field(GraphQLFieldDefinition.newFieldDefinition()
                            .name("mensaje")
                            .type(Scalars.GraphQLString))
                    .field(GraphQLFieldDefinition.newFieldDefinition()
                            .name("estado")
                            .type(Scalars.GraphQLString))
                    .build();

            GraphQLObjectType mutationType = GraphQLObjectType.newObject()
                    .name("Mutation")
                    .field(GraphQLFieldDefinition.newFieldDefinition()
                            .name("enviarNotificacion")
                            .type(notificacionType)
                            .argument(arg -> arg.name("evento").type(Scalars.GraphQLString))
                            .argument(arg -> arg.name("canal").type(Scalars.GraphQLString))
                            .argument(arg -> arg.name("destino").type(Scalars.GraphQLString))
                            .argument(arg -> arg.name("mensaje").type(Scalars.GraphQLString))
                            .dataFetcher(environment -> {
                                String evento = environment.getArgument("evento");
                                String canal = environment.getArgument("canal");
                                String destino = environment.getArgument("destino");
                                String mensaje = environment.getArgument("mensaje");

                                if (isNullOrEmpty(evento) ||
                                    isNullOrEmpty(canal) ||
                                    isNullOrEmpty(destino) ||
                                    isNullOrEmpty(mensaje)) {
                                    throw new RuntimeException("Todos los campos son obligatorios: evento, canal, destino, mensaje.");
                                }

                                log.info("=== PROTOTIPO NOTIFICACION GRAPHQL ===");
                                log.info("Evento  : " + evento);
                                log.info("Canal   : " + canal);
                                log.info("Destino : " + destino);
                                log.info("Mensaje : " + mensaje);

                                String estado = service.enviar(canal, destino, mensaje, log);

                                NotificacionesResponse response = new NotificacionesResponse();
                                response.setEvento(evento);
                                response.setCanal(canal);
                                response.setDestino(destino);
                                response.setMensaje(mensaje);
                                response.setEstado(estado);

                                return response;
                            }))
                    .build();

            GraphQLObjectType queryType = GraphQLObjectType.newObject()
                    .name("Query")
                    .field(GraphQLFieldDefinition.newFieldDefinition()
                            .name("health")
                            .type(Scalars.GraphQLString)
                            .dataFetcher(environment -> "GraphQL Notificaciones OK"))
                    .build();

            GraphQLSchema schema = GraphQLSchema.newSchema()
                    .query(queryType)
                    .mutation(mutationType)
                    .build();

            GraphQL graphQL = GraphQL.newGraphQL(schema).build();

            ExecutionResult result = graphQL.execute(body.getQuery());

            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(result.toSpecification())
                    .build();

        } catch (Exception e) {
            log.severe("Error en la función GraphQL de notificaciones: " + e.getMessage());

            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(Map.of(
                            "errors", new Object[] {
                                    Map.of("message", "Error procesando la notificación.")
                            }))
                    .build();
        }
    }

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}