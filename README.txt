CSS Compressor
Version 1.0 (8th March 2009)

Copyright 2009, Andy Roberts
http://www.andy-roberts.net/software/csscompressor

Introduction
============

CSS Compressor is a simple Java library and utility for reducing the size
of Cascading Style Sheets (CSS).

This code is forked from Yahoo's YUI Compressor, an excellent open source
library for shrinking Javascript and CSS. I've made a number of enhancements to
the CSS compression algorithms. Hopefully one day in the future my changes will
be merged in to the YUI source. In the meantime, I wanted to share my code,
hence this distribution of this stand-alone CSS component.

Features
========

CSS Compressor specific


* Merges duplicate templates. E.g.
    .myclassA
    {
       font-style: bold;
    }
    .myclassB
    {
       font-style: bold;
    }

  becomes:
    .myclassA, .myclassB
    {
       font-style: bold;
    }
 * Enhanced dimension compression. E.g.,
     .myclass {
       border: 4px 4px 4px 4px;
     }

   becomes:

     .myclass {
       border: 4px;
     }

 * Additional color representation compression.
 * Strip last semi-colon from blocks. E.g.,
    .myclass
    {
       font-style: bold;
       color: red;
    }

  becomes:

    .myclass
    {
       font-style: bold;
       color: red
    }
  
Inherited features

 * Removes unneccessary whitespace. E.g.,
    .myclass
    {
       font-style: bold;
    }

  becomes:
    .myclass{font-style:bold;}

 * Strip units when value is 0; e.g.,
    .myclass
    {
       margin-left: 0px;
    }

  becomes:
    .myclass
    {
       margin-left: 0;
    }

 * Color compression; e.g.,
     rgb(51,102,153) -> #336699
     #AABBCC -> #ABC
 * Remove comments
 * Strip empty rules.
 
Running CSS Compressor
======================

CSS Compressor can be run as a command-line application.

At a command prompt navigate to the path containing CssCompressor.jar

prompt> java -jar CssCompressor.jar [options] [input file]

Usage: java -jar CssCompressor-x.y.z.jar [options] [input file]
  Global Options
   -h, --help                Displays this information
   --charset <charset>       Read the input file using <charset>
   --line-break <column>     Insert a line break after the specified column number
   -v, --verbose             Display informational messages and warnings
   -o <file>                 Place the output into <file>. Defaults to stdout.

  If no input file is specified, it defaults to stdin.

License
=======

CSS Compressor is released under the BSD license. See LICENSE.txt for more
details.

Acknowledgements
================

I'd like to thank Julien Lecomte, the original developer of the YUI Compressor
library. His original code acknowledges Isaac Schlueter's cssmin utility.
