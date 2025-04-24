package ast;

import models.Polynom;

public interface Node {
   default void execute() {
   }

   Polynom getValue();
}
