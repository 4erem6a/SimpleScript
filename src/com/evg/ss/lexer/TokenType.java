package com.evg.ss.lexer;

/**
 * @author 4erem6a
 */
public enum TokenType {
    //Identifiers:
    Word("<word>"),

    //Literals:
    Number("<number>"),
    String("<string>"),
    InterpolatedString("<`string`>"),

    //Operators:
    Lp("("),            //(
    Rp(")"),            //)
    Lb("{"),            //{
    Rb("}"),            //}
    Lc("["),            //[
    Rc("]"),            //]
    Pl("+"),            //+
    Mn("-"),            //-
    St("*"),            //*
    Sl("/"),            ///
    Pr("%"),            //%
    Eq("="),            //=
    Am("&"),            //&
    Vb("|"),            //|
    Cr("^"),            //^
    Tl("~"),            //~
    Ex("!"),            //!
    Qm("?"),            //?
    Al(">"),            //<
    Ar("?"),            //>
    At("@"),            //@
    Ns("#"),            //#
    VbVb("||"),         //||
    AmAm("&&"),         //&&
    AlEq("<="),         //<=
    ArEq(">="),         //>=
    EqEq("=="),         //==
    PlPl("++"),         //++
    MnMn("--"),         //--
    ExEq("!="),         //!=
    MnAr("->"),         //->
    ClCl("::"),         //::
    ArAr(">>"),         //>>
    AlAl("<<"),         //<<
    EqQm("=?"),         //=?
    NsNs("##"),         //##
    DtDt(".."),         //..
    ArArAr(">>>"),      //>>>
    DtDtDt("..."),      //...
    Cl(":"),            //:
    Dt("."),            //.
    Cm(","),            //,
    Sc(";"),            //;

    //Keywords:
    True("true"),           //true
    False("false"),         //false
    Null("null"),           //null
    Nan("nan"),             //nan
    Let("let"),             //let
    Const("const"),         //const
    If("if"),               //if
    Else("else"),           //else
    For("for"),             //for
    Do("do"),               //do
    While("while"),         //while
    Break("break"),         //break
    Continue("continue"),   //continue
    Block("block"),         //block
    Require("require"),     //require
    Function("function"),   //function
    Return("return"),       //return
    Foreach("foreach"),     //foreach
    In("in"),               //in
    Typeof("typeof"),       //typeof
    Switch("switch"),       //switch
    Case("case"),           //case
    Type("type"),           //type
    Is("is"),               //is
    Extends("extends"),     //extends
    Exports("exports"),     //exports
    As("as"),               //as
    Nameof("nameof"),       //nameof
    Import("import"),       //import
    New("new"),             //new
    This("this"),           //this
    Locked("locked"),       //locked
    Try("try"),             //try
    Catch("catch"),         //catch
    Finally("finally"),     //finally
    Throw("throw"),         //throw
    Class("class"),         //class
    Static("static"),       //static
    Undefined("undefined"), //undefined
    That("that"),           //that
    Super("super"),         //super

    //Utility:
    EOF("end");

    //TokenType:
    private String name;

    TokenType(String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }
}