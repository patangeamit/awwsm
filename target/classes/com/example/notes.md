NodeProg > NodeStmt[]
NodeStmt > NodeStmtLet or NodeStmtExit
NodeStmtLet > NodeIdent and NodeExpr 
NodeStmtExit > NodeExpr

NodeExpr > NodeExprTerm or NodeExprBin
NodeExprTerm > NodeExprTermInt or NodeExprTermIdnt
NodeExprBin > NodeExpr and NodeExpr