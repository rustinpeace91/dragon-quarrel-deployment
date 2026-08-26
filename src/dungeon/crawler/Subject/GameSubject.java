package dungeon.crawler.Subject;

import com.badlogic.gdx.utils.Array;

import dungeon.crawler.Observers.PlayerPositionObserver;

public abstract class GameSubject<T> implements Subject<T> {
    protected final Array<T> observers = new Array<>();

    @Override
    public void addObserver(T observer) { observers.add(observer); }

    @Override
    public void removeObserver(T observer) { observers.removeValue(observer, true); }


}
