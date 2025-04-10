package ast;

import models.Polynom;

public interface EvaluatableNode extends Node {
   abstract Polynom getValue();
}
