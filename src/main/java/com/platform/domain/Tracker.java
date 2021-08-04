package com.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Tracker.
 */
@Document(collection = "tracker")
public class Tracker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("activated")
    private Boolean activated;

    @Field("create_at")
    private LocalDate createAt;

    @Field("modified_at")
    private LocalDate modifiedAt;

    @Field("activation_begin")
    private LocalDate activationBegin;

    @Field("activation_end")
    private LocalDate activationEnd;

    @DBRef
    @Field("client")
    @JsonIgnoreProperties(value = { "exchanges", "orders", "trackers" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tracker id(String id) {
        this.id = id;
        return this;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Tracker activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public LocalDate getCreateAt() {
        return this.createAt;
    }

    public Tracker createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getModifiedAt() {
        return this.modifiedAt;
    }

    public Tracker modifiedAt(LocalDate modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public void setModifiedAt(LocalDate modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public LocalDate getActivationBegin() {
        return this.activationBegin;
    }

    public Tracker activationBegin(LocalDate activationBegin) {
        this.activationBegin = activationBegin;
        return this;
    }

    public void setActivationBegin(LocalDate activationBegin) {
        this.activationBegin = activationBegin;
    }

    public LocalDate getActivationEnd() {
        return this.activationEnd;
    }

    public Tracker activationEnd(LocalDate activationEnd) {
        this.activationEnd = activationEnd;
        return this;
    }

    public void setActivationEnd(LocalDate activationEnd) {
        this.activationEnd = activationEnd;
    }

    public Client getClient() {
        return this.client;
    }

    public Tracker client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tracker)) {
            return false;
        }
        return id != null && id.equals(((Tracker) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tracker{" +
            "id=" + getId() +
            ", activated='" + getActivated() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", activationBegin='" + getActivationBegin() + "'" +
            ", activationEnd='" + getActivationEnd() + "'" +
            "}";
    }
}
