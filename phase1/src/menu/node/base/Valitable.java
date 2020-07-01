package menu.node.base;

import menu.node.ResponseNode;

import java.util.Optional;

public interface Valitable {

    Optional<ResponseNode> validate();
}
