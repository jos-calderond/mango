package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.utilities.Indicator;
import com.mindhub.homebanking.utilities.Indicators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api")
public class IndicatorsController {

    private Indicators response;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/indicators", method = RequestMethod.GET)
    public Indicators getIndicators(){
        String url = "https://mindicador.cl/api/";


        if(response != null){
            //si paso mas de una hora, volvemos a consultar la api externa y actualizamos copia local
            if(ChronoUnit.HOURS.between(LocalDateTime.now().plusHours(2),response.getFecha()) > 0){
                response = restTemplate.getForObject(url, Indicators.class);
            }
        }else{
            //guardamos la respuesta de la api si no la tenemos
            response = restTemplate.getForObject(url, Indicators.class);
        }
        return response;
    }
}
