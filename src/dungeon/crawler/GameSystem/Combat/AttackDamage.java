package dungeon.crawler.GameSystem.Combat;

import dungeon.crawler.GameSystem.Character.Condition;

public class AttackDamage {
    public int toHit;
    public int damage;
    public String flavorText;
    public boolean ranged;
    public Condition condition;
    public Elemental elemental;

    public AttackDamage(
        int toHit,
        int damage,
        String flavorText,
        boolean ranged
    ){
        this(toHit, damage, flavorText, ranged, null, null);
    }

    public AttackDamage(
        int toHit,
        int damage,
        String flavorText,
        boolean ranged,
        Condition condition,
        Elemental elemental
    ) {
        this.toHit = toHit;
        this.damage = damage;
        this.flavorText = flavorText;
        this.ranged = ranged;
        this.condition = condition;
        this.elemental = elemental;
    }


}
