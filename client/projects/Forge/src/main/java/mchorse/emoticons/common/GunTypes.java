package mchorse.emoticons.common;

/**
 * Fortcraft gun types enumerate
 * 
 * This enum contains stats about every gun type available in the mod. Coming 
 * soon in 0.2.
 */
public enum GunTypes
{
    /* Pistols */
    PISTOL(23, 6.75F, 16, 1.5F), HAND_CANNON(75, 0.8F, 8, 2F), REVOLVER(54, 0.9F, 6, 2.5F),
    /* SMGs */
    SMG(17, 12, 30, 2.4F, true), COMPACT_SMG(17, 10, 40, 3.6F, true),
    /* Assault rifles */
    AR(30, 5.5F, 30, 2.3F), SCAR(35, 5.5F, 30, 2.1F),
    /* Shotguns */
    PUMP_SHOTGUN(80, 0.7F, 5, 4.8F, true), TACTICAL_SHOTGUN(67, 1.5F, 8, 6.3F, true),
    /* Sniper rifles */
    BOLT_SNIPER(105, 0.33F, 1, 3F), AUTO_SNIPER(75, 1.2F, 10, 2.5F),
    /* Explosives */
    GRENADE_LAUNCHER(100, 1, 6, 3F, true), RPG(110, 0.75F, 1, 2.8F);

    public final float damage;
    public final float firerate;
    public final int maxAmmo;
    public final int reloadSpeed;
    public final boolean automatic;

    private GunTypes(float damage, float firerate, int maxAmmo, float reloadSpeed)
    {
        this(damage, firerate, maxAmmo, reloadSpeed, false);
    }

    private GunTypes(float damage, float firerate, int maxAmmo, float reloadSpeed, boolean automatic)
    {
        this.damage = damage * 0.2F;
        this.firerate = firerate;
        this.maxAmmo = maxAmmo;
        this.reloadSpeed = (int) (reloadSpeed * 20);
        this.automatic = automatic;
    }
}