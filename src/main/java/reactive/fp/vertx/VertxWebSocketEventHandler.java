package reactive.fp.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import reactive.fp.types.*;
import rx.Observable;
import rx.subjects.*;
import java.net.URI;

import static reactive.fp.mappers.Mappers.*;

/**
 * @author OZY on 2015.11.23.
 */
public class VertxWebSocketEventHandler<T> implements EventHandler<T> {

    private final URI wsUrl;
    private final Subject<Event<?>, Event<?>> subject;
    private final Vertx vertx;

    public VertxWebSocketEventHandler(URI wsUrl) {
        this.wsUrl = wsUrl;
        this.vertx = Vertx.vertx();
        this.subject = new SerializedSubject<>(ReplaySubject.create());
    }

    protected void checkForEvents(WebSocket webSocket) {
        webSocket.handler(buffer -> {
                    final byte[] bytes = buffer.getBytes();
                    final Event<?> receivedEvent = fromJsonToEvent(bytes);
                    switch (receivedEvent.eventType) {
                        case NEXT: {
                            subject.onNext(receivedEvent);
                            break;
                        }
                        case ERROR: {
                            subject.onError((Throwable) receivedEvent.payload);
                            break;
                        }
                        case COMPLETED: {
                            subject.onCompleted();
                            break;
                        }
                    }
                }
        );
    }

    public Observable<Event<?>> toObservable(String commandName, T arg) {
        final HttpClient httpClient = vertx.createHttpClient(new HttpClientOptions());
        return Observable.using(() -> httpClient.websocketStream(wsUrl.getPort() == -1 ?
                80:
                wsUrl.getPort(), wsUrl.getHost(), wsUrl.getPath()),
                webSocketStream -> {
                    webSocketStream.handler(webSocket -> {
                        startCommand(commandName, arg, webSocket);
                        checkForEvents(webSocket);
                    });
                    return subject;
                }, webSocketStream -> { webSocketStream.pause(); httpClient.close(); });
    }

    private void startCommand(String commandName, T arg, WebSocket webSocket) {
        final Command<T> command = Command.create(commandName, arg);
        final byte[] messageJson = messageToJsonBytes(command);
        webSocket.writeFinalBinaryFrame(Buffer.buffer(messageJson));
    }

}
