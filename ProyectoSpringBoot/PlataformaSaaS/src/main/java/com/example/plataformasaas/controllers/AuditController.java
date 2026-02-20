package com.example.plataformasaas.controllers;

import com.example.plataformasaas.models.Suscripcion;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AuditController {

    private final EntityManager entityManager;

    public AuditController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GetMapping("/audit")
    @SuppressWarnings("unchecked")
    public String auditPanel(Model model) {
        AuditReader reader = AuditReaderFactory.get(entityManager);

        // Fetch all revisions of Suscripcion
        List<Object[]> history = reader.createQuery()
                .forRevisionsOfEntity(Suscripcion.class, false, true)
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();

        model.addAttribute("history", history);
        return "audit_panel";
    }
}
