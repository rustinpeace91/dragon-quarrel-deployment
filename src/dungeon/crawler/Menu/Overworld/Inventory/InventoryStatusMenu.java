package dungeon.crawler.Menu.Overworld.Inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Observers.StatusMenu;
import dungeon.crawler.Menu.StandardStatusMenu;
import dungeon.crawler.Utils.ItemUtils;
import dungeon.crawler.Utils.StringUtils;

public class InventoryStatusMenu extends StandardStatusMenu
    implements StatusMenu
{
    private PartyCharacter character;

    public InventoryStatusMenu (
        Skin skin,
        PartyCharacter character
    ){
        super(skin);
        this.character = character;
        displayCharacter();


    }

    public void displayCharacter(){
        String text = StringUtils.format(
            "%s's equpment. \n" +
            "Main Hand: %s \n" +
            "Off Hand: %s \n" +
            "Helment: %s \n" +
            "Armor: %s \n" +
            "Feet: %s \n\n" +
            "HP: %s\n" +
            "MP: %s\n"  +
            "Total Armor: %s \n" +
            "To Hit: %s \n" +
            "Damage: %s \n",

            character.name,
            ItemUtils.getItemName(character.equipment.getRightHand()),
            ItemUtils.getItemName(character.equipment.getLeftHand()),
            ItemUtils.getItemName(character.equipment.getHead()),
            ItemUtils.getItemName(character.equipment.getBody()),
            ItemUtils.getItemName(character.equipment.getFeet()),
            String.valueOf(character.getHp()),
            String.valueOf(character.getMp()),
            String.valueOf(character.equipment.getDefenseBonus()),
            String.valueOf(character.getToHit()),
            character.getAttackDamageString()
        );
        setText(
            text
        );
    }

    public void setCharacter(PartyCharacter characterValue){
        character = characterValue;
    }

    @Override
    public void refresh(){
        displayCharacter();
    }
    public void showCharacter(PartyCharacter character){
        setCharacter(character);
        displayCharacter();
    }
}
