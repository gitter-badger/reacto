package net.soundvibe.reacto;

import net.soundvibe.reacto.types.Event;
import org.junit.Test;
import net.soundvibe.reacto.server.CommandRegistry;
import rx.Observable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Linas on 2015.11.13.
 */
public class CommandRegistryTest {

    @Test
    public void shouldFindCommand() throws Exception {
        CommandRegistry sut = CommandRegistry.of("foo", o -> Observable.just(Event.create("foo")));

        assertTrue(sut.findCommand("foo").isPresent());
    }

    @Test
    public void shouldNotFindCommand() throws Exception {
        assertFalse(CommandRegistry.of("dfdf", o -> Observable.empty()).findCommand("foobar").isPresent());
    }
}
