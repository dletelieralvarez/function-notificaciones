package com.function.model;

//ESTO ES MI DTO DE ENTRADA, REPRESENTA EL FORMATO ESTANDAR QUE RECIBE GRAPHQL
//GRAPHQL NO RECIBE DIRECTAMENTE LOS DATOS COMO REST, LOS REVIBE COMO UNA CONSULTA (QUERY O MUTATION)
public class GraphQLRequest {
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
