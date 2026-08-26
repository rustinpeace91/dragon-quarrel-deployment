package dungeon.crawler.Menu.Observers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StatusMenuObserver {
    private final ArrayList<StatusMenu> observers = new ArrayList<>();

    public void addObserver(StatusMenu observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(StatusMenu observer) {
        observers.remove(observer);
    }


    public void refreshObservers() {
        for (StatusMenu observer : observers) {
            observer.refresh();
        }
    }
}
