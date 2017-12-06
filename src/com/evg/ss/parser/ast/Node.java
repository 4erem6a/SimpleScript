package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

/**
 * @author 4erem6a
 */
public interface Node {

    void accept(Visitor visitor);

    <TResult> TResult accept(ResultVisitor<TResult> visitor);

}