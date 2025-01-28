package com.devopsdummy.springbootmfe.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FrontendTests {

    private frontend frontendController;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        frontendController = new frontend();
        // Reemplazar RestTemplate por mock
        try {
            Field restTemplateField = frontend.class.getDeclaredField("restTemplate");
            restTemplateField.setAccessible(true);
            restTemplateField.set(frontendController, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIndex() {
        String viewName = frontendController.index(model);

        assertEquals("index", viewName);
        verify(model).addAttribute("saludo", "");
        verify(model).addAttribute("fecha", "");
    }


    @Test
    void testGetSaludoWithEmptyName() {
        when(restTemplate.getForObject(
                "http://devopsdummysaludo.apps.preprodalcaldia.medellin.gov.co/saludo",
                String.class
        )).thenReturn("Hola!");

        String viewName = frontendController.getSaludo(null, model);

        assertEquals("index", viewName);
        verify(restTemplate).getForObject(
                "http://devopsdummysaludo.apps.preprodalcaldia.medellin.gov.co/saludo",
                String.class
        );
        verify(model).addAttribute("saludo", "Hola!");
        verify(model).addAttribute("nombre", null);
    }

    @Test
    void testGetSaludoWithName() {
        String nombre = "Juan";
        when(restTemplate.getForObject(
                "http://devopsdummysaludo.apps.preprodalcaldia.medellin.gov.co/saludo?nombre=" + nombre,
                String.class
        )).thenReturn("Hola, Juan!");

        String viewName = frontendController.getSaludo(nombre, model);

        assertEquals("index", viewName);
        verify(restTemplate).getForObject(
                "http://devopsdummysaludo.apps.preprodalcaldia.medellin.gov.co/saludo?nombre=" + nombre,
                String.class
        );
        verify(model).addAttribute("saludo", "Hola, Juan!");
        verify(model).addAttribute("nombre", nombre);
    }

    @Test
    void testGetFecha() {
        when(restTemplate.getForObject(
                "http://devopsdummyfecha.apps.preprodalcaldia.medellin.gov.co/fecha",
                String.class
        )).thenReturn("2024-01-23");

        String viewName = frontendController.getFecha(model);

        assertEquals("index", viewName);
        verify(restTemplate).getForObject(
                "http://devopsdummyfecha.apps.preprodalcaldia.medellin.gov.co/fecha",
                String.class
        );
        verify(model).addAttribute("fecha", "2024-01-23");
    }
}
