public class Weapon {
    private int ammo;

    public Weapon(int ammo) {
        this.ammo = ammo;
    }

    public int getAmmo() {
        return ammo;
    }

    public void shoot() {
        if (ammo > 0) {
            ammo--;
        }
    }
}
