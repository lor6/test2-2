package com.baeldung.markerinterface;

public class ShapeDao {

    public boolean delete(Object object) {
        if (!(object instanceof Deletable)) {
            return false;
        }
        // Calling the code that deletes the entity from the database

        return true;
    }

}
