package com.music.music_inventory_api.exception;

/**
 * Exception thrown when a requested entity is not found in the database.
 */
public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String entityName, Long id) {
        super(String.format("%s not found with id: %d", entityName, id));
    }
    
    public EntityNotFoundException(String entityName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: %s", entityName, fieldName, fieldValue));
    }
}

