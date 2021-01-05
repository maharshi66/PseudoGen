package com.maharshiappdev.pseudogen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class UsingPseudoGenActivity extends AppCompatActivity {
    private String htmlText =
            " <!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<title>\n" + "</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>Using PseudoGen</h1>\n" +
                    "<p><b>PseudoGen</b> is intended to be used by programmers to write and store pseudocode to solve programming problems in a clean and independent way</p>\n" +
                    "<h2>What is a Pseudocode?</h2>\n" +
                    "<p>In computer science, an algorithm consists of a sequence of well-defined instructions to perform a computation and solve a programming problem. Before beginning to " +
                    "implement a solution on a device, it is important to plan the sequence of instructions and a pseudocode provides an easy way to represent a proposed solution." +
                    " A pseudocode uses descriptive text written in plain English and can be understood by anyone with some or no programming background. This app allows writing pseudocode " +
                    "without being bogged down by the syntactical or implementation details of any programming language</p>\n" +
                    "<h2>Writing Pseudocode</h2>\n" +
                    "<p><b>PseudoGen</b> provides a simple and clean way to write pseudocode. Although a programmer has the freedom to write pseudocode in their preferred manner, a standard " +
                    "can be followed for better readability and comprehension. A standard for writing pseudocode is as follows: \n" +
                    "<ul>" +
                    "<li> Give a title to the pseudocode" +
                    "<li> Provide a brief description of the problem at hand" +
                    "<li> Choose input data structure" +
                    "<li> Choose output data structure" +
                    "<li> Enclose the pseudocode with <b>process...endprocess</b>" +
                    "<li> Write the set of instructions for decision control and execution mechanism to solve the problem" +
                    "</ul>" +
                    "</p>\n" +
                    "<h3>Statements</h3>\n" +
                    "<p>Each line is a specific instruction and these can be referred to as statements. They are intended to be read from top to bottom.</p>"+
                    "<h4>Keywords</h4>\n" +
                    "<p>Keywords are preloaded texts that can be used to represent commands or parameters. Look at the table at the end for more details</p> " +
                    "<h4>Operators</h4>\n" +
                    "<p>Operators form an integral part of any algorithm in order to manipulate data. Look at the table at the end for more details</p>"+
                    "<h3>Conditional Statements</h3>\n" +
                    "<p>Conditional statements help implement a decision flowchart in any programming language. When writing pseudocode" +
                    ", statements can be enclosed between <b>if...else...endif</b> and can be evaluated accordingly</p>"+
                    "<h3>Loops</h3>\n" +
                    "<p>In a lot of the cases, instructions need to be repeated to generate a set of outcomes and this is where looping (iterating) comes in handy." +
                    "</p>"+
                    "<h4>for loop</h4>\n" +
                    "<p>A <b>for</b> loop can be used when we know the number of iterations required and the precondition that remains true throughout. This can be represented by enclosing statements " +
                    "between <b>for...endloop</b></p>"+
                    "<h4>while loop</h4>\n" +
                    "<p>Just like the <b>for</b> loop, <b>while</b> loop allows executing a block of code until a condition remains true. Statements can be enclosed between <b>while...endwhile</b> to represent a while loop.</p>"+
                    "<h3>Functions</h3>\n"+
                    "<p>Functions, also known as methods, serve a very important purpose while coding. They allow reusing code and a lot of flexibility for programmers to call a chunk of code during execution. In pseudocode, we" +
                    "can enclose a block of code or statements between <b>function <i>function-name</i>...endfunction</b> and utilize them by using the <b>call</b> keyword followed by the function name</p>"+
                    "</body>\n" +
            "</html>\n";
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_using_pseudogen );
        TextView htmlTextView = findViewById ( R.id.usingPseudoGenTextView );
        htmlTextView.setText( HtmlCompat.fromHtml(htmlText, 0));
    }
}
