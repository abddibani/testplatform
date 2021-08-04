package com.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Client.
 */
@Document(collection = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("email")
    private String email;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("activated")
    private Boolean activated;

    @DBRef
    @Field("exchange")
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<Exchange> exchanges = new HashSet<>();

    @DBRef
    @Field("order")
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @DBRef
    @Field("tracker")
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<Tracker> trackers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client id(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public Client username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Client password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Client activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Set<Exchange> getExchanges() {
        return this.exchanges;
    }

    public Client exchanges(Set<Exchange> exchanges) {
        this.setExchanges(exchanges);
        return this;
    }

    public Client addExchange(Exchange exchange) {
        this.exchanges.add(exchange);
        exchange.setClient(this);
        return this;
    }

    public Client removeExchange(Exchange exchange) {
        this.exchanges.remove(exchange);
        exchange.setClient(null);
        return this;
    }

    public void setExchanges(Set<Exchange> exchanges) {
        if (this.exchanges != null) {
            this.exchanges.forEach(i -> i.setClient(null));
        }
        if (exchanges != null) {
            exchanges.forEach(i -> i.setClient(this));
        }
        this.exchanges = exchanges;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public Client orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Client addOrder(Order order) {
        this.orders.add(order);
        order.setClient(this);
        return this;
    }

    public Client removeOrder(Order order) {
        this.orders.remove(order);
        order.setClient(null);
        return this;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setClient(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setClient(this));
        }
        this.orders = orders;
    }

    public Set<Tracker> getTrackers() {
        return this.trackers;
    }

    public Client trackers(Set<Tracker> trackers) {
        this.setTrackers(trackers);
        return this;
    }

    public Client addTracker(Tracker tracker) {
        this.trackers.add(tracker);
        tracker.setClient(this);
        return this;
    }

    public Client removeTracker(Tracker tracker) {
        this.trackers.remove(tracker);
        tracker.setClient(null);
        return this;
    }

    public void setTrackers(Set<Tracker> trackers) {
        if (this.trackers != null) {
            this.trackers.forEach(i -> i.setClient(null));
        }
        if (trackers != null) {
            trackers.forEach(i -> i.setClient(this));
        }
        this.trackers = trackers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", activated='" + getActivated() + "'" +
            "}";
    }
}
