package com.evg.ss.lexer;

/**
 * @author 4erem6a
 */
public enum TokenType {
    //Identifiers:
    Word,

    //Literals:
    Number,
    String,
    InterpolatedString,

    //Operators:
    Lp,         //(
    Rp,         //)
    Lb,         //{
    Rb,         //}
    Lc,         //[
    Rc,         //]
    Pl,         //+
    Mn,         //-
    St,         //*
    Sl,         ///
    Eq,         //=
    Am,         //&
    Vb,         //|
    Cr,         //^
    Tl,         //~
    Ex,         //!
    Qm,         //?
    Al,         //<
    Ar,         //>
    VbVb,       //||
    AmAm,       //&&
    AlEq,       //<=
    ArEq,       //>=
    EqEq,       //==
    ExEq,       //!=
    MnAr,       //->
    Cl,         //:
    Dt,         //.
    Cm,         //,
    Sc,         //;

    //Keywords:
    True,       //true
    False,      //false
    Null,       //null
    Nan,        //nan
    Let,        //let
    Const,      //const
    If,         //if
    Else,       //else
    For,        //for
    Do,         //do
    While,      //while
    Break,      //break
    Continue,   //continue
    Block,      //block
    Import,     //import
    Function,   //function
    Return,     //return
    Foreach,    //foreach
    In,         //in
    Typeof,     //typeof
    Switch,     //switch
    Case,       //case
    Type,       //type
    Is,         //is
    Static,     //static
    Extends,    //extends

    //Utility:
    EOF
}