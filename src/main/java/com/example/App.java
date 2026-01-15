package com.example;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        port(port);

        get("/hello", (req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Hello from Jenkins Docker K8s demo\"}";
        });

        get("/", (req, res) -> "App is running");
    }
}

