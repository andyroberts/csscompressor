/*
 * CSS Compressor
 * Author: Andy Roberts
 * Copyright (c) 2009, Andy Roberts. All rights reserved.
 *
 * This software is licensed under the BSD license.
 *
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of Andy Roberts nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission of Yahoo! Inc.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * This code is a derivative of code distributed by Yahoo! Inc. Their rights
 * are fully acknowledged below:
 *
 * YUI Compressor
 * Author: Julien Lecomte <jlecomte@yahoo-inc.com>
 * Copyright (c) 2007, Yahoo! Inc. All rights reserved.
 * Code licensed under the BSD License:
 *     http://developer.yahoo.net/yui/license.txt
 */

package andyr.csscompressor;

import jargs.gnu.CmdLineParser;

import java.io.*;
import java.nio.charset.Charset;

public class Compressor {

    public static void main(String args[]) {

        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option typeOpt = parser.addStringOption("type");
        CmdLineParser.Option verboseOpt = parser.addBooleanOption('v', "verbose");
        CmdLineParser.Option linebreakOpt = parser.addStringOption("line-break");
        CmdLineParser.Option helpOpt = parser.addBooleanOption('h', "help");
        CmdLineParser.Option charsetOpt = parser.addStringOption("charset");
        CmdLineParser.Option outputFilenameOpt = parser.addStringOption('o', "output");

        Reader in = null;
        Writer out = null;

        try {

            parser.parse(args);

            Boolean help = (Boolean) parser.getOptionValue(helpOpt);
            if (help != null && help.booleanValue()) {
                usage();
                System.exit(0);
            }

            boolean verbose = parser.getOptionValue(verboseOpt) != null;

            String charset = (String) parser.getOptionValue(charsetOpt);
            if (charset == null || !Charset.isSupported(charset)) {
                charset = System.getProperty("file.encoding");
                if (charset == null) {
                    charset = "UTF-8";
                }
                if (verbose) {
                    System.err.println("\n[INFO] Using charset " + charset);
                }
            }

            String[] fileArgs = parser.getRemainingArgs();
            String type = (String) parser.getOptionValue(typeOpt);

            if (fileArgs.length == 0) {

                if (type == null || !type.equalsIgnoreCase("js") && !type.equalsIgnoreCase("css")) {
                    usage();
                    System.exit(1);
                }

                in = new InputStreamReader(System.in, charset);

            } else {

                if (type != null && !type.equalsIgnoreCase("js") && !type.equalsIgnoreCase("css")) {
                    usage();
                    System.exit(1);
                }

                String inputFilename = fileArgs[0];

                if (type == null) {
                    int idx = inputFilename.lastIndexOf('.');
                    if (idx >= 0 && idx < inputFilename.length() - 1) {
                        type = inputFilename.substring(idx + 1);
                    }
                }

                if (type == null || !type.equalsIgnoreCase("js") && !type.equalsIgnoreCase("css")) {
                    usage();
                    System.exit(1);
                }

                in = new InputStreamReader(new FileInputStream(inputFilename), charset);
            }

            int linebreakpos = -1;
            String linebreakstr = (String) parser.getOptionValue(linebreakOpt);
            if (linebreakstr != null) {
                try {
                    linebreakpos = Integer.parseInt(linebreakstr, 10);
                } catch (NumberFormatException e) {
                    usage();
                    System.exit(1);
                }
            }

            String outputFilename = (String) parser.getOptionValue(outputFilenameOpt);

            if (type.equalsIgnoreCase("css")) {

                CssCompressor compressor = new CssCompressor(in);

                // Close the input stream first, and then open the output stream,
                // in case the output file should override the input file.
                in.close(); in = null;

                if (outputFilename == null) {
                    out = new OutputStreamWriter(System.out, charset);
                } else {
                    out = new OutputStreamWriter(new FileOutputStream(outputFilename), charset);
                }

                compressor.compress(out, linebreakpos);
            }

        } catch (CmdLineParser.OptionException e) {

            usage();
            System.exit(1);

        } catch (IOException e) {

            e.printStackTrace();
            System.exit(1);

        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void usage() {
        System.out.println(
                "\nUsage: java -jar CssCompressor.jar [options] [input file]\n\n"
                        + "Global Options\n"
                        + "  -h, --help                Displays this information\n"
                        + "  --charset <charset>       Read the input file using <charset>\n"
                        + "  --line-break <column>     Insert a line break after the specified column number\n"
                        + "  -v, --verbose             Display informational messages and warnings\n"
                        + "  -o <file>                 Place the output into <file>. Defaults to stdout.\n\n"
                        + "If no input file is specified, it defaults to stdin.");
    }
}
