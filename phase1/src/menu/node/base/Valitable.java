package menu.node.base;

import menu.node.ErrorNode;

import java.util.Optional;

public interface Valitable {

    Optional<ErrorNode> validate();
}
