package com.platform.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.platform.domain.Tracker} entity.
 */
public class TrackerDTO implements Serializable {

    private String id;

    private Boolean activated;

    private LocalDate createAt;

    private LocalDate modifiedAt;

    private LocalDate activationBegin;

    private LocalDate activationEnd;

    private ClientDTO client;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDate getActivationBegin() {
        return activationBegin;
    }

    public void setActivationBegin(LocalDate activationBegin) {
        this.activationBegin = activationBegin;
    }

    public LocalDate getActivationEnd() {
        return activationEnd;
    }

    public void setActivationEnd(LocalDate activationEnd) {
        this.activationEnd = activationEnd;
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
        if (!(o instanceof TrackerDTO)) {
            return false;
        }

        TrackerDTO trackerDTO = (TrackerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trackerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackerDTO{" +
            "id='" + getId() + "'" +
            ", activated='" + getActivated() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", activationBegin='" + getActivationBegin() + "'" +
            ", activationEnd='" + getActivationEnd() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
