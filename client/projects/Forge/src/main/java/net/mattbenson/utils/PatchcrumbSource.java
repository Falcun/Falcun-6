package net.mattbenson.utils;

public enum PatchcrumbSource
{
    ENTITY(0), 
    SOUND(1), 
    SAND_STACK(2);
    
    private int id;
    
    private PatchcrumbSource(final int id) {
        this.id = id;
    }
}
