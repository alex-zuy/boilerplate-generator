package org.alex.zuy.boilerplate.beans;

public class OwnedEntity<T, S extends BaseEntity<T>> {

    private S owner;

    public T getOwnerId() {
        return owner.getId();
    }

    public S getOwner() {
        return owner;
    }

    public void setOwner(S owner) {
        this.owner = owner;
    }
}
