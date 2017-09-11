package com.gianfranco.trabajoparcial.web.controller;

import com.gianfranco.trabajoparcial.domain.Client;
import com.gianfranco.trabajoparcial.service.CityService;
import com.gianfranco.trabajoparcial.service.ClientService;
import com.gianfranco.trabajoparcial.service.HobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ClientController {
    @Autowired
    private CityService cityService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private HobbyService hobbyService;

    @RequestMapping(path = "/")
    public String index(Model model) {
        Iterable<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "dashboard";
    }

    @RequestMapping(path = "/clients/add")
    public String formNewClient(Model model) {
        if (!model.containsAttribute("client")) {
            model.addAttribute("client", new Client());
        }
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("hobbies", hobbyService.findAll());
        model.addAttribute("action", "/clients/add");
        model.addAttribute("heading", "Add client");
        model.addAttribute("submit", "Add client");
        return "form";
    }

    @RequestMapping(value = "/clients/add", method = RequestMethod.POST)
    public String addClient(@Valid Client client, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return addClientHelper(client, result, redirectAttributes, "There was an error with the form. See below for more details");
        }
        try {
            clientService.save(client);
            redirectAttributes.addFlashAttribute("flash", "Client was successfully added");
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            return addClientHelper(client, result, redirectAttributes, "The DNI is duplicated. Please, enter another DNI");

        } catch (Exception e) {
            return addClientHelper(client, result, redirectAttributes, "There was an error with the form. See below for more details");

        }
    }

    private String addClientHelper(@Valid Client client, BindingResult result, RedirectAttributes redirectAttributes, String errorMessage) {
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.client", result);
        redirectAttributes.addFlashAttribute("client", client);
        redirectAttributes.addFlashAttribute("flash", errorMessage);
        return "redirect:/clients/add";
    }

    @RequestMapping(path = "/clients/edit/{clientId}")
    public String formEditClient(Model model, @PathVariable int clientId) {
        if (!model.containsAttribute("client")) {
            model.addAttribute("client", clientService.findById(clientId));
        }
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("hobbies", hobbyService.findAll());
        model.addAttribute("action", "/clients/edit");
        model.addAttribute("heading", "Edit client");
        model.addAttribute("submit", "Update client");
        return "form";
    }

    @RequestMapping(value = "/clients/edit", method = RequestMethod.POST)
    public String updateClient(@Valid Client client, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.client", result);
            redirectAttributes.addFlashAttribute("client", client);
            redirectAttributes.addFlashAttribute("flash", "There was an error with the form. See below for more details");
            return String.format("redirect:/clients/edit/%s", client.getId());
        }
        clientService.save(client);
        redirectAttributes.addFlashAttribute("flash", "Client was successfully updated");
        return "redirect:/";
    }

    @RequestMapping(value = "/clients/delete/{clientId}")
    public String deleteCategory(@PathVariable int clientId, RedirectAttributes redirectAttributes) {
        clientService.delete(clientId);
        redirectAttributes.addFlashAttribute("flash", "Client was successfully deleted");
        return "redirect:/";
    }
}
