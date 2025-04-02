package ast;

import models.Polynom;

public interface EvaluatableNode {
   abstract Polynom getValue();
}
