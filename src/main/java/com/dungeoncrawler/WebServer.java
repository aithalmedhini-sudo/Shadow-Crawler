package com.dungeoncrawler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Built-in lightweight HTTP server that hosts the 3D Web Edition
 * and automatically opens the user's browser.
 */
public class WebServer {
    private static final int PORT = 8080;
    private static final String WEB_DIR = "web";

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new FileHandler());
            server.setExecutor(null); // Default executor
            server.start();

            String url = "http://localhost:" + PORT + "/index.html";
            System.out.println("========================================================");
            System.out.println("  3D Dungeon Crawler Web Server Started Successfully!");
            System.out.println("  Local URL:  " + url);
            System.out.println("  Share this URL with anyone on your local network/Wi-Fi!");
            System.out.println("  Press Ctrl+C in this console to stop the server.");
            System.out.println("========================================================");

            // Open default browser automatically
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception e) {
                    System.out.println("Could not auto-open browser: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to start web server on port " + PORT + ": " + e.getMessage());
        }
    }

    private static class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String pathStr = exchange.getRequestURI().getPath();
            if (pathStr.equals("/") || pathStr.isEmpty()) {
                pathStr = "/index.html";
            }

            Path filePath = Paths.get(WEB_DIR, pathStr.substring(1));
            File file = filePath.toFile();

            if (!file.exists() || file.isDirectory()) {
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            String mimeType = Files.probeContentType(filePath);
            if (mimeType == null) {
                if (pathStr.endsWith(".js")) mimeType = "application/javascript";
                else if (pathStr.endsWith(".html")) mimeType = "text/html";
                else if (pathStr.endsWith(".css")) mimeType = "text/css";
                else if (pathStr.endsWith(".json")) mimeType = "application/json";
                else mimeType = "application/octet-stream";
            }

            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, file.length());

            OutputStream os = exchange.getResponseBody();
            Files.copy(filePath, os);
            os.close();
        }
    }
}
