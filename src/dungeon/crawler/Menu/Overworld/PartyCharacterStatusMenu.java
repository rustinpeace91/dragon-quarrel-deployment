package dungeon.crawler.Menu.Overworld;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.Menu.Observers.StatusMenu;
import dungeon.crawler.Menu.StandardStatusMenu;
import dungeon.crawler.Utils.StringUtils;

public class PartyCharacterStatusMenu extends StandardStatusMenu implements StatusMenu {
    private PartyCharacter character;
    public PartyCharacterStatusMenu (
        Skin skin,
        PartyCharacter character
    ){
        super(skin);
        this.character = character;
        displayCharacter();


    }

    public void displayCharacter(){
        setText(
            StringUtils.format(
                "%s\nLevel: %s\n HP: %s\n MP: %s\n XP: %s \n\n Strength: %s\nAgility: %s\nIntelligence:%s\nPerception%s\n\n To Hit Bonus: +%s\nDamage Bonus: +0",
                character.name,
                String.valueOf(character.level),
                String.valueOf(character.hp),
                String.valueOf(character.mp),
                String.valueOf(character.xp),
                String.valueOf(character.strength),
                String.valueOf(character.agility),
                String.valueOf(character.intelligence),
                String.valueOf(character.perception),
                String.valueOf(character.getToHit()),
                String.valueOf(character.getDamageBonus())


            )
        );
//        this.setSize(200f, 300f);
    }

    @Override
    public void refresh(){
        displayCharacter();
    }

    public void setCharacter(PartyCharacter characterValue){
        character = characterValue;
    }

    public void showCharacter(PartyCharacter character){
        setCharacter(character);
        displayCharacter();
    }
}
