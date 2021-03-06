package net.soundvibe.reacto.client.events;

import net.soundvibe.reacto.types.Command;
import net.soundvibe.reacto.types.Event;
import rx.Observable;

/**
 * @author Cipolinas on 2015.11.16.
 */
public interface EventHandler {

    Observable<Event> toObservable(Command command);

}
