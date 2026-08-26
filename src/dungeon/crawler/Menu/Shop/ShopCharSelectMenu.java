package dungeon.crawler.Menu.Shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Misc.PopUpUtils;
import dungeon.crawler.Menu.Observers.StatusMenuObserver;
import dungeon.crawler.Menu.Overworld.Inventory.InventoryBagMenu;
import dungeon.crawler.Menu.Overworld.Inventory.InventoryMenu;
import dungeon.crawler.Menu.Overworld.Inventory.InventoryStatusMenu;
import dungeon.crawler.Menu.Overworld.PartyCharacterStatusMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;
// TODO: refactor this and statusselectionmenu

public class ShopCharSelectMenu extends BaseLinearMenu implements OverworldSubMenu
{

    private final ArrayList<Item> shopInventory;
    private GameState gameState;
    private InventoryStatusMenu partyStatusMenu;
    protected boolean canSellItems;

    public StatusMenuObserver statusMenuObserver = new StatusMenuObserver();
    public BaseLinearMenu asCombatMenu(){return this;}
    public ShopCharSelectMenu(
        Skin skin,
        GameState gameState,
        ArrayList<Item> shopInventory
    ){
        super(skin);
        this.canSellItems = false;
        this.gameState = gameState;
        this.partyStatusMenu = new InventoryStatusMenu(
            skin,
            gameState.player
        );
//
        this.subStatusMenu = partyStatusMenu;
        statusMenuObserver.addObserver(this.partyStatusMenu);
        this.isToggleable = false;
        this.shopInventory = shopInventory;
    }



    @Override
    protected void setStage(Stage stage){
        // TODO: Move this logic OUTTA here. This runs when the menu closes too
        super.setStage(stage);
        if(stage == null) return;
        this.addPartyButtons();


        if(parentMenu != null){
//            this.setPosition(this.parentMenu.getOriginX() + 200, Gdx.graphics.getHeight() - this.getHeight());
            setSizeandPosition(GameConstants.SUBMENU_SIZE.MEDIUM);

        }

        float x = this.parentMenu.getWidth() + this.getWidth() + 40;
        float y = Gdx.graphics.getHeight() - getHeight() - 150;
        subStatusMenu.setPosition(
            x, y
        );
        stage.addActor(subStatusMenu);
        refreshAndSetActive();
        subStatusMenu.alignTopRight(stage);
        this.addFocusListeners();
        this.buttonList = populateButtonList();
        this.resetMenuSelection();

    }

    @Override
    public void closeMenuStack() {
        this.subStatusMenu.remove();
        super.closeMenuStack();
    }

    public void setCanSellItems(boolean value){
        canSellItems = value;
    }

    @Override
    public void refreshAndSetActive(){
        addPartyButtons();
        this.addFocusListeners();
        super.refreshAndSetActive();
    }
    public void finishItemOption(){
        returnToParentMenu();
    }

    private void addPartyButtons(){
        this.clearChildren();
        setTitle(StringUtils.format("Purchase for Who?"));
        this.initializeArrow();
        if(gameState.party != null){
            gameState.party.forEach(
                (key, character) -> {
                    addButton(
                        character.name,
                        new ChangeListener(){
                            @Override
                            public void changed(ChangeEvent event, Actor actor){
                                BaseLinearMenu nextMenu = new ShopItemCategoryMenu(
                                    skin,
                                    gameState,
                                    shopInventory,
                                    character
                                );
                                setSubMenu(nextMenu);
                                openSubMenu(nextMenu);

                                // else display a small popup

                            }
                        },
                        character
                    );
                }
            );
        }
        this.pack();
        setSizeandPosition(GameConstants.SUBMENU_SIZE.MEDIUM);
    }
    @Override
    public void addFocusListeners(){
        super.addFocusListeners();
        for (Actor actor : this.getChildren()) {
            if(actor instanceof TextButton){
                TextButton button = (TextButton) actor;
                button.addListener(new FocusListener(){
                    @Override
                    public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                        TextButton button = (TextButton) actor;
                        PartyCharacter character = (PartyCharacter)button.getUserObject();
                        if (focused) {
                            if(button.getUserObject() instanceof PartyCharacter){
//                                statusMenuObserver.refreshObservers();

                                partyStatusMenu.showCharacter(character);
                            } else {
                                partyStatusMenu.setText(StringUtils.format("This is the status text for: \n %s", button.getText()));

                            }
                        }
                    }
                });
            }
        }
    }
}
