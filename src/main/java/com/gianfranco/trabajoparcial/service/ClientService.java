package com.gianfranco.trabajoparcial.service;

import com.gianfranco.trabajoparcial.domain.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();

    void save(Client client);

    Client findById(int clientId);

    void delete(int clientId);
}
