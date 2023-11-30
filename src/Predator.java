import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Predator extends Animal {
    protected int damage;

    protected Predator(int hp, int maxAge, int vision, int damage) {
        super(hp, maxAge, vision);
        this.damage = damage;
    }

    @Override
    public void act(World world) {
    }

    protected void attack(Location location, int damage, World world) {
        Animal target = (Animal) world.getTile(location);
        target.takeDamage(damage);
    }

    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black);
    }
}
