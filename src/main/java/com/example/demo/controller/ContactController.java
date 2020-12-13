package com.example.demo.controller;

import com.example.demo.dao.ContactRepository;
import com.example.demo.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/contact")
public class ContactController {
    private ContactRepository contactRepository;

    @Autowired
    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @GetMapping("/list")
    String list(Model model,
                @RequestParam(value = "start",defaultValue = "0") Integer start,
                @RequestParam(value = "limit",defaultValue = "3") Integer limit
                ){
        start = start <0 ? 0 :start;
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(start,limit,sort);
        Page<Contact> contacts = contactRepository.findAll(pageable);
        model.addAttribute("contacts",contacts);
        return "contactlist";
    }

    @GetMapping("/add")
    String addForm(){
        return "contactadd";
    }

    @PostMapping("/add")
    String add(Contact contact){
        contactRepository.save(contact);
        return "success";
    }

    @GetMapping("/delete")
    String delete(@RequestParam Long contactId){
        contactRepository.deleteById(contactId);
        return "success";
    }

    @GetMapping("/edit")
    String editForm(Model model, @RequestParam Long contactId){
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if(contactOptional.isPresent()){
            Contact contact = contactOptional.get();
            model.addAttribute("contact",contact);
            return "contactedit";
        }else{
            return "failure";
        }
    }

    @PostMapping("/edit")
    String edit(Contact contact){
        contactRepository.save(contact);
        return "success";
    }

    @GetMapping("/search")
    String search(@RequestParam Long tel) {
        Optional<Contact> contactOptional = contactRepository.findByTel(tel);
        if (contactOptional.isPresent()) {
            return "该电话号码已存在";
        }else {
            return "";
        }
    }
}

