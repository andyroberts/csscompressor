package andyr.csscompressor;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class CssCompressorTest {

    @Test
    public void testCompressionMergesDuplicates() throws Exception {
        checkCompression(".myclassA { font-style: bold; }\n.myclassB { font-style: bold; }", ".myclassA,\n.myclassB{font-style:bold}");
    }

    @Test
    public void testCompressionCompressesDimensions() throws Exception {
        checkCompression(".myclass { border: 4px 4px 4px 4px; } ", ".myclass{border:4px}");
    }

    @Test
    public void testCompressionRemovesLastSemiColon() throws Exception {
        checkCompression(".myclass { font-style: bold; color: red; }", ".myclass{font-style:bold;color:red}");
    }

    @Test
    public void testCompressionRemovesWhitespaces() throws Exception {
        checkCompression(".myclass { font-style: bold; }", ".myclass{font-style:bold}");
    }

    @Test
    public void testCompressionStripsUnitValuesOnZeros() throws Exception {
        checkCompression(".myclass { margin-left: 0px; }", ".myclass{margin-left:0}");
    }

    @Test
    public void testCompressionCompressesColors() throws Exception {
        checkCompression(".myclass { color: rgb(51,102,153); }", ".myclass{color:#369}");
        checkCompression(".myclass { color: #AABBCC; }", ".myclass{color:#ABC}");
    }

    @Test
    public void testCompressionRemovesComments() throws Exception {
        checkCompression(".myclass { font-style: bold; /* comment */ }", ".myclass{font-style:bold}");
    }

    @Test
    public void testCompressionRemovesEmptyRules() throws Exception {
        checkCompression(".myclass { }", "");
    }

    @Test
    public void testCompressionAcceptsBlankInputs() throws Exception {
        checkCompression("", "");
        checkCompression("\n\n", "");
    }

    private void checkCompression(String cssIn, String cssOut) throws IOException {
        Reader in = new StringReader(cssIn);
        Writer out = new StringWriter();
        new CssCompressor(in).compress(out, 4);
        assertEquals(cssOut, out.toString());
    }

}
