package com.music.music_inventory_api.exception;

/**
 * Exception thrown when there is insufficient stock for an album.
 */
public class InsufficientStockException extends RuntimeException
{
    public InsufficientStockException(String message)
    {
        super(message);
    }

    public InsufficientStockException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
