package dungeon.crawler.Menu.Church;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Utils.ItemUtils;
import dungeon.crawler.Utils.PartyUtils;

import java.util.Arrays;

public class ChurchServiceMenu extends BaseLinearMenu implements OverworldSubMenu {
    private final GameState gameState;
    private final String service;
    private final int price;


    public ChurchServiceMenu(
        Skin skin,
        GameState gameState,
        String service,
        int price
    ){
        super(skin);
        this.gameState = gameState;
        this.service = service;
        this.price = price;
        this.initializeButtons();
    }

    public BaseLinearMenu asCombatMenu(){return this;}


    protected void updateButtons(){

        this.clearChildren();
        String title = service + " costs \n" +
            price + " Gold \n";

        setTitle(title);
        this.initializeArrow();
        if(service == "Resurrection"){
            resurrectButton();
        } else {
            cureButton();
        }


        this.addButton("Back",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    returnToParentMenu();
                }
            }
        );


        if(getStage() == null) {
            Gdx.app.log("Menu Error", "refreshAndSetActive called BEFORE linear menu added to stage");
        }
        setVisible(true);

        if (parentMenu != null) {
            refreshAndSetActive();
        }

    }

    protected void resurrectButton(){
        this.addButton("Buy",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(gameState.gold >= price){
                        PartyUtils.resurrectDeadPartyMembers(gameState.party);
                        gameState.removeGold(price);
                        showPopup("Thou have risen from the dead!", 2f);

                    } else {
                        showPopup("Not enough gold!", 2f);
                    }
                    returnToParentMenu();
                }
            }
        );
    }

    protected void cureButton(){
        this.addButton("Buy",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(gameState.gold >= price){
                        PartyUtils.cureAllAilments(gameState.party);
                        gameState.removeGold(price);
                        showPopup("Thou have been cured of all ailments!", 2f);

                    } else {
                        showPopup("Not enough gold!", 2f);
                    }
                    returnToParentMenu();
                }
            }
        );
    }
    protected void initializeButtons(){

        updateButtons();

    }

    public void finishItemOption(){
        returnToParentMenu();
    }

    @Override
    protected void setStage(Stage stage) {

        super.setStage(stage);

        if(parentMenu != null){

            setSizeandPosition(GameConstants.SUBMENU_SIZE.TALL);
        }

        if (stage != null) {
            refreshAndSetActive();
        }
    }
    @Override
    public void FinishMenuComplete(){
        OverworldSubMenu inventoryMenu = (OverworldSubMenu)parentMenu;
        returnToParentMenu();
        inventoryMenu.FinishMenuComplete();
    }
}
