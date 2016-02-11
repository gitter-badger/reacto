package reactive.fp.types;

import java.util.Optional;

/**
 * @author OZY on 2015.11.13.
 */
public class CommandNodes {

    public final String mainNode;
    public final Optional<String> fallbackNode;

    public CommandNodes(String mainNode, Optional<String> fallbackNode) {
        this.mainNode = mainNode;
        this.fallbackNode = fallbackNode;
    }

    @Override
    public String toString() {
        return "CommandNodes{" +
                "mainNode='" + mainNode + '\'' +
                ", fallbackNode=" + fallbackNode +
                '}';
    }
}