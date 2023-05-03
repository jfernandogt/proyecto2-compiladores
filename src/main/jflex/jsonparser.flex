package com.jfbarrios;

import java_cup.runtime.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;


%%
%unicode

/**
* Modo compatibilidad con CUP
* En el momento que se genera el analizador léxico implemente la interfaz scanner que CUP espera
* que tenga los métodos que CUP espera y que los Tokenes que se generan a partir del analizador léxico
* por ejemplo las categorías y cadenas que hicieron match sean compatibles con las clases de CUP
*/
%cup
%line
%column
%class JSONLexer

// set de reglas de expresiones regulares
valid_set_char = [^\u0000-\u001F\"\\] // no incluye caracteres de control
valid_character = {valid_set_char}
string = \"{valid_character}*\"
sign = ["+"-]?
dot = "."
digit = [0-9]
int_part = 0|[1-9]{digit}*
dec_part = {digit}+
int_num = {sign} {int_part}
real_num = {int_num} {dot} {dec_part}
boolean = "true" | "false"
null = "null"



/* esto se copia integro dentro de la clase del analizador léxico,
* tiene dos métodos donde se genera una nueva instancia de la clase Symbol, y es la que espera CUP
* que sea el formato en el cual JFlex entregue los simbolos para realizar el análisis.
* En uno solo se recibe el identificador de tipo entero que representa el token dentro de CUP
* En otro se tiene un segundo parametro donde se puede mandar un argumento del objeto o lexema que se está construyendo
* a partir del reconocimiento
*/
%{
    private PrintWriter writer;
    StringBuffer string = new StringBuffer();
    private Symbol symbol(int type) {
        if (writer != null) {
            writer.println("<tr><td>"+type+"</td><td></td><td></td><td>"+yyline+"</td><td>"+yycolumn+"</td></tr>");
        }
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        if (writer != null) {
            writer.println("<tr><td>"+type+"</td><td>"+value.toString()+"</td><td></td><td>"+yyline+"</td><td>"+yycolumn+"</td></tr>");
        }
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol symbol(int type, Object value, String pattern) {
        if (writer != null) {
            writer.println("<tr><td>"+type+"</td><td>"+value.toString()+"</td><td>"+pattern+"</td><td>"+yyline+"</td><td>"+yycolumn+"</td></tr>");
        }
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

%init{
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    try {
    writer = new PrintWriter(String.valueOf(timestamp) + ".html");
    // writer = new PrintWriter("temporal.html");
    writer.println("<html><head><style>table {font-family: arial, sans-serif; border-collapse: collapse; width: 100%;} td, th {border: 1px solid #dddddd; text-align: left; padding: 8px; } tr:nth-child(even) {background-color: #dddddd;}</style></head><body>");
    writer.println("<table>");
    writer.println("<tr><th>Token</th><th>Lexema</th><th>Patron</th><th>Linea</th><th>Columna</th></tr>");
    } catch (FileNotFoundException e) {
    throw new RuntimeException(e);
    }
%init}


// acá se establece que pasa cuando se encuentra el final del archivo, básicamente se genera un token de tipo EOF
%eofval{
    if (writer != null) {
        writer.println("</table></body></html>");
        writer.close();
    }
    return symbol(ParserSym.EOF);
%eofval}

%%

// colección de expresiones regulares que hacen match con los tipos de caracteres que podemos encontrar en un JSON
// acá se hace uso de la clase ParserSym que no existe, se generará por CUP como resultado del análisis de la gramática
"{" { return symbol(ParserSym.LCRLBRKT, new String(yytext()), "{"); }
"}" { return symbol(ParserSym.RCRLBRKT, new String(yytext()), "}"); }

"[" { return symbol(ParserSym.LSQRBRKT, new String(yytext()), "["); }
"]" { return symbol(ParserSym.RSQRBRKT, new String(yytext()), "]"); }

"," { return symbol(ParserSym.COMMA, new String(yytext()), ","); }
":" { return symbol(ParserSym.COLON, new String(yytext()), ":"); }
//  [^\u0000-\u001F\"\\]
{string}   { return symbol(ParserSym.STRING, new String(yytext()), "{string}"); }

{int_num}  { return symbol(ParserSym.INT_NUM, new Long(yytext()), "{int_num}"); }

{real_num} { return symbol(ParserSym.REAL_NUM, new Double(yytext()), "{real_num}"); }

{boolean}  { return symbol(ParserSym.BOOLEAN, new Boolean(yytext()), "{boolean}"); }

{null}	   { return symbol(ParserSym.NULL, new String(yytext()), "{null}"); }
[ \t\r\n\f] { /* ignorar espacios */ }

[^] { throw new IllegalArgumentException("Cadena no valida: " + yytext()); }