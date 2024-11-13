package com.example;

import java.util.ArrayList;
import java.util.List;

abstract class Node {

}

class NodeProg extends Node {
    List<NodeStmt> statements = new ArrayList<>();

    NodeProg() {
    }

    NodeProg(List<NodeStmt> s) {
        statements = s;
    }
}

class NodeStmt extends Node {
    Node var; // can be NodeStmtExit or NodeStmtLet

    NodeStmt(Node var) {
        this.var = var;
    }
}

class NodeStmtExit extends Node {
    // exit <expr> ;
    NodeExpr expr;

    NodeStmtExit(NodeExpr e) {
        expr = e;
    }
}

class NodeStmtLet extends Node {
    // let <idnt> = <expr> ;
    Token idnt;
    NodeExpr expr;

    NodeStmtLet(Token idnt, NodeExpr expr) {
        this.idnt = idnt;
        this.expr = expr;
    }
}

class NodeExpr extends Node {
    // <var>
    Node var; // can be NodeExprTerm or NodeExprBin

    NodeExpr(Node var) {
        this.var = var;
    }
}

class NodeExprTerm extends Node {
    Node var;// can be NodeExprTermIdent or NodeExprTermInt

    NodeExprTerm(Node var) {
        this.var = var;
    }
}

class NodeExprTermInt extends Node {
    // <tokenInt>
    Token tokenInt;

    NodeExprTermInt(Token t) {
        tokenInt = t;
    }
}

class NodeExprTermIdnt extends Node {
    // <tokenIdnt>
    Token tokenIdnt;

    NodeExprTermIdnt(Token t) {
        tokenIdnt = t;
    }

}

class NodeExprBin extends Node {
    Token tokenOp;
    Node left, right;

    NodeExprBin(Token op, Node l, Node r) {
        tokenOp = op;
        left = l;
        right = r;
    }
}
// class NodeExprBin extends Node {
// Node var; // can be NodeExprBinAdd or NodeExprBinMult

// NodeExprBin(Node var) {
// this.var = var;
// }
// }

// class NodeExprBinAdd extends Node {
// NodeExpr left, right;
// Token tokenPlus;
// Node exprBinMult; // optional

// NodeExprBinAdd(NodeExpr l, NodeExpr r) {
// left = l;
// right = r;
// }

// NodeExprBinAdd(NodeExprBinMult n) {
// exprBinMult = n;
// }
// }

// class NodeExprBinMult extends Node {
// NodeExpr left, right;
// Token tokenStar;
// }