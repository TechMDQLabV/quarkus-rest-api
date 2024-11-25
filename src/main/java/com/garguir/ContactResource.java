package com.garguir;

import com.garguir.models.Contact;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/contact")
@Produces("application/json")
@Consumes
public class ContactResource {

    @Inject
    EntityManager entityManager;

    @POST
    @Transactional
    public Response addContact(Contact contact) {
        entityManager.persist(contact);
        return Response.status(Response.Status.CREATED).entity(contact).build();
    }

    @GET
    public List<Contact> getAllContacts() {
        return entityManager.createQuery("FROM Contact", Contact.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Response getContactById(@PathParam("id") Long id) {
        Contact contact = entityManager.find(Contact.class, id);
        if(contact == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(contact).build();
    }

    @GET
    @Path("/email/{email}")
    public Response getContactByEmail(@PathParam("email") String email) {
        Contact contact = entityManager.createQuery("SELECT c FROM Contact c WHERE c.email = :email", Contact.class)
                .setParameter("email", email)
                .getSingleResult();
        if(contact == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(contact).build();
    }

    @GET
    @Path("/search")
    public Response getContacts(@QueryParam("email") String email) {
        List<Contact> contacts = entityManager.createQuery("SELECT c FROM Contact c WHERE c.email LIKE :email", Contact.class)
                .setParameter("email", "%" + email + "%")
                .getResultList();
        return Response.ok(contacts).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateContactById(@PathParam("id") Long id, Contact contact) {
        Contact oldContact = entityManager.find(Contact.class, id);
        if(contact == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setDataProtection(contact.isDataProtection());
        entityManager.persist(oldContact);
        return Response.ok(oldContact).build();
    }

    @PUT
    @Path("/email/{email}")
    @Transactional
    public Response updateContactByEmail(@PathParam("email") String email, Contact contact) {
        Contact oldContact = entityManager.createQuery("SELECT c FROM Contact c WHERE c.email = :email", Contact.class)
                .setParameter("email", email)
                .getSingleResult();
        if(contact == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setDataProtection(contact.isDataProtection());
        entityManager.merge(oldContact);
        return Response.ok(oldContact).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteContact(@PathParam("id") Long id) {
        Contact contact = entityManager.find(Contact.class, id);
        if(contact == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entityManager.remove(contact);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


}
