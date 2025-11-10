package com.danyazero.model.ast;

import com.danyazero.utils.GenerationContext;

import java.util.List;

public class ExpressionList implements Node {

    private final List<Expression> expressionList;

    public ExpressionList(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }

    @Override
    public void produce(GenerationContext ctx) {
        expressionList.forEach(node -> node.produce(ctx));
    }

    @Override
    public void resolveTypes(GenerationContext ctx) {
        expressionList.forEach(node -> node.resolveTypes(ctx));
    }

    @Override
    public String toString() {
        return "ExpressionList{" +
                "expressionList=" + expressionList +
                '}';
    }

    public List<Expression> getExpressions() {
        return expressionList;
    }
}
