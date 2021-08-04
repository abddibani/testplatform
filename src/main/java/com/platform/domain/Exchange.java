package com.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Exchange.
 */
@Document(collection = "exchange")
public class Exchange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("apikey")
    private String apikey;

    @Field("secrit")
    private String secrit;

    @Field("activated")
    private Boolean activated;

    @Field("create_at")
    private LocalDate createAt;

    @Field("modified_at")
    private LocalDate modifiedAt;

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

    public Exchange id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Exchange name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApikey() {
        return this.apikey;
    }

    public Exchange apikey(String apikey) {
        this.apikey = apikey;
        return this;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getSecrit() {
        return this.secrit;
    }

    public Exchange secrit(String secrit) {
        this.secrit = secrit;
        return this;
    }

    public void setSecrit(String secrit) {
        this.secrit = secrit;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Exchange activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public LocalDate getCreateAt() {
        return this.createAt;
    }

    public Exchange createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getModifiedAt() {
        return this.modifiedAt;
    }

    public Exchange modifiedAt(LocalDate modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public void setModifiedAt(LocalDate modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Client getClient() {
        return this.client;
    }

    public Exchange client(Client client) {
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
        if (!(o instanceof Exchange)) {
            return false;
        }
        return id != null && id.equals(((Exchange) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exchange{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", apikey='" + getApikey() + "'" +
            ", secrit='" + getSecrit() + "'" +
            ", activated='" + getActivated() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            "}";
    }
}
