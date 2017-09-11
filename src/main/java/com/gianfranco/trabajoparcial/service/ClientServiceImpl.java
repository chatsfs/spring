package com.gianfranco.trabajoparcial.service;

import com.gianfranco.trabajoparcial.dao.ClientDao;
import com.gianfranco.trabajoparcial.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientDao clientDao;

    @Override
    public List<Client> findAll() {
        return clientDao.findAll();
    }

    @Override
    public void save(Client client) {
        clientDao.save(client);
    }

    @Override
    public Client findById(int clientId) {
        return clientDao.findOne(clientId);
    }

    @Override
    public void delete(int clientId) {
        clientDao.delete(clientId);
    }
}
