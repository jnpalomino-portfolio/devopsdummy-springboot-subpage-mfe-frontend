package com.devopsdummy.springbootmfe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class frontend {
    private static final Logger log = LoggerFactory.getLogger(frontend.class);

    @Value("${servicio.saludo.url}")
    private String saludoUrl;

    @Value("${servicio.fecha.url}")
    private String fechaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("saludo", "");
        model.addAttribute("fecha", "");
        return "index";
    }

    @PostMapping("/getSaludo")
    public String getSaludo(String nombre, Model model) {
        String url;
        if (nombre == null || nombre.trim().isEmpty()) {
             url = "http://devopsdummysaludo.apps.preprodalcaldia.medellin.gov.co/saludo";
        }
        else {
            url = "http://devopsdummysaludo.apps.preprodalcaldia.medellin.gov.co/saludo?nombre=" + nombre;
        }
        log.info("Intentando conectar a: {}", url);
        String response = restTemplate.getForObject(url, String.class);
        model.addAttribute("saludo", response);
        model.addAttribute("nombre", nombre);
        return "index";
    }

    @PostMapping("/getFecha")
    public String getFecha(Model model) {
        String response = restTemplate.getForObject("http://devopsdummyfecha.apps.preprodalcaldia.medellin.gov.co/fecha", String.class);
        model.addAttribute("fecha", response);
        return "index";
    }
}
