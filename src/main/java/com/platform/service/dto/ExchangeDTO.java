package com.platform.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.platform.domain.Exchange} entity.
 */
public class ExchangeDTO implements Serializable {

    private String id;

    private String name;

    private String apikey;

    private String secrit;

    private Boolean activated;

    private LocalDate createAt;

    private LocalDate modifiedAt;

    private ClientDTO client;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getSecrit() {
        return secrit;
    }

    public void setSecrit(String secrit) {
        this.secrit = secrit;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDate modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExchangeDTO)) {
            return false;
        }

        ExchangeDTO exchangeDTO = (ExchangeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exchangeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExchangeDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", apikey='" + getApikey() + "'" +
            ", secrit='" + getSecrit() + "'" +
            ", activated='" + getActivated() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
