package mat.cql;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static mat.cql.CqlUtils.nextQuotedString;
import static mat.cql.CqlUtils.nextTickedString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class TestCqlCqlUtils {

    @Test
    public void testNextQuotedStringErrors() {
        String myString = "no quoted string here";
        ParseResult p = nextQuotedString(myString,0);
        assertEquals(null,p.getString());
        assertEquals(-1,p.getEndIndex());

        myString = "no quoted string \"here";
        p = nextQuotedString(myString,0);
        assertEquals(null,p.getString());
        assertEquals(-1,p.getEndIndex());

        myString = "no quoted string \"here\\\"";
        p = nextQuotedString(myString,0);
        assertEquals(null,p.getString());
        assertEquals(-1,p.getEndIndex());
    }

    @Test
    public void testNextQuotedStringNoEscape() {
        String myString = "kjshdfrkjshdf \"This is the string\" 23847293847234\nksjdfhsdf";
        ParseResult p = nextQuotedString(myString,0);
        assertEquals("This is the string",p.getString());
        assertEquals(33,p.getEndIndex());
        assertEquals('"',myString.charAt(p.getEndIndex()));

        myString = "\"This is the string\" 23847293847234\nksjdfhsdf";
        p = nextQuotedString(myString,0);
        assertEquals("This is the string",p.getString());
        assertEquals(19,p.getEndIndex());
        assertEquals('"',myString.charAt(p.getEndIndex()));

        myString = "\"This is the string\"";
        p = nextQuotedString(myString,0);
        assertEquals("This is the string",p.getString());
        assertEquals(19,p.getEndIndex());
        assertEquals('"',myString.charAt(p.getEndIndex()));

        myString = "\"This is the string\n\n,\t\"";
        p = nextQuotedString(myString,0);
        assertEquals("This is the string\n\n,\t",p.getString());
        assertEquals(23,p.getEndIndex());
        assertEquals('"',myString.charAt(p.getEndIndex()));
    }

    @Test
    public void textNextQuotedStringInnerQuotes() {
        String myString = "kjshdfrkjshdf \"This is the \\\"INNER\\\" string\" 23847293847234\nksjdfhsdf";
        ParseResult p = nextQuotedString(myString,0);
        assertEquals("This is the \\\"INNER\\\" string",p.getString());
        assertEquals(43,p.getEndIndex());
        assertEquals('"',myString.charAt(p.getEndIndex()));

        myString = "\"\\\"\\\"\"";
        p = nextQuotedString(myString,0);
        assertEquals("\\\"\\\"",p.getString());
        assertEquals(5,p.getEndIndex());
        assertEquals('"',myString.charAt(p.getEndIndex()));
    }

    @Test
    public void testNextTickedStringErrors() {
        String myString = "no quoted string here";
        ParseResult p = nextTickedString(myString,0);
        assertEquals(null,p.getString());
        assertEquals(-1,p.getEndIndex());

        myString = "no quoted string \"here";
        p = nextTickedString(myString,0);
        assertEquals(null,p.getString());
        assertEquals(-1,p.getEndIndex());

        myString = "no quoted string \'here";
        p = nextTickedString(myString,0);
        assertEquals(null,p.getString());
        assertEquals(-1,p.getEndIndex());

    }

    @Test
    public void testNextTickedStringNoEscape() {
        String myString = "kjshdfrkjshdf 'This is the string' 23847293847234\nksjdfhsdf";
        ParseResult p = nextTickedString(myString,0);
        assertEquals("This is the string",p.getString());
        assertEquals(33,p.getEndIndex());
        assertEquals('\'',myString.charAt(p.getEndIndex()));

        myString = "'This is the string' 23847293847234\nksjdfhsdf";
        p = nextTickedString(myString,0);
        assertEquals("This is the string",p.getString());
        assertEquals(19,p.getEndIndex());
        assertEquals('\'',myString.charAt(p.getEndIndex()));

        myString = "'This is the string'";
        p = nextTickedString(myString,0);
        assertEquals("This is the string",p.getString());
        assertEquals(19,p.getEndIndex());
        assertEquals('\'',myString.charAt(p.getEndIndex()));

        myString = "'This is the string\n\n,\t'";
        p = nextTickedString(myString,0);
        assertEquals("This is the string\n\n,\t",p.getString());
        assertEquals(23,p.getEndIndex());
        assertEquals('\'',myString.charAt(p.getEndIndex()));
    }

    @Test
    public void textNextTickedStringInnerQuotes() {
        String myString = "kjshdfrkjshdf 'This is the \\'INNER\\' string' 23847293847234\nksjdfhsdf";
        ParseResult p = nextTickedString(myString,0);
        assertEquals("This is the \\'INNER\\' string",p.getString());
        assertEquals(43,p.getEndIndex());
        assertEquals('\'',myString.charAt(p.getEndIndex()));

        myString = "'\\'\\''";
        p = nextTickedString(myString,0);
        assertEquals("\\'\\'",p.getString());
        assertEquals(5,p.getEndIndex());
        assertEquals('\'',myString.charAt(p.getEndIndex()));
    }

    @Test
    public void testRemoveBlockComments() {
        String s = "Simple\nBlock comment\nTest\n  FOO/*  sldjfksldkfjslkdfjsldkfjs\n\n\n\n\n\n\n\n slkdjrslkdfjsldkfj*/\n END";
        String clean = CqlUtils.removeCqlBlockComments(s);
        assertEquals("Simple\nBlock comment\nTest\n  FOO\n END",clean);
    }

    @Test
    public void testRemoveBlockComments2() {
        String s = "/*S*/imple\n/*Block comment*/\nTest\n  FOO/*  sldjfksldkfjslkdfjsldkfjs\n\n\n\n\n\n\n\n slkdjrslkdfjsldkfj*/\n EN/*D*/";
        String clean = CqlUtils.removeCqlBlockComments(s);
        assertEquals("imple\n\nTest\n  FOO\n EN",clean);
    }

    @Test
    public void testRemoveLineComments() {
        String s = "This is a bunch \nof text spread\n out on a bunch\nof //NARF\n lines";
        String clean = CqlUtils.removeCQLLineComments(s);
        assertEquals("This is a bunch \nof text spread\n out on a bunch\nof \n lines",clean);
    }

    @Test
    public void testRemoveLineComments2() {
        String s = "This is a bunch \nof text spread\n out on a bunch\nof //NARF    //     asd\n lines";
        String clean = CqlUtils.removeCQLLineComments(s);
        assertEquals("This is a bunch \nof text spread\n out on a bunch\nof \n lines",clean);
    }

    @Test
    public void testNextBlock1() {
        String s = "This is a bunch {of text spread} out on a bunch\nof //NARF    //     asd\n lines";
        ParseResult result  = CqlUtils.nextBlock(s,'{','}',0);
        assertEquals("{of text spread}",result.getString());
        assertEquals(31,result.getEndIndex());
    }

    @Test
    public void testNextBlock2() {
        String s =  "This is a bunch {of text {NARF{NARF{NARF}NARF}NARF} s\ndf{g}dfg    \ndfgdfgdf{g}df   {\npread}} out on a bunch\nof //NARF    //     asd\n lines";
        ParseResult result  = CqlUtils.nextBlock(s,'{','}',0);
        assertEquals("{of text {NARF{NARF{NARF}NARF}NARF} s\ndf{g}dfg    \ndfgdfgdf{g}df   {\npread}}",result.getString());
        assertEquals(91,result.getEndIndex());
    }

    @Test
    public void testNextBlock3() {
        String s =  "{}";
        ParseResult result  = CqlUtils.nextBlock(s,'{','}',0);
        assertEquals("{}",result.getString());
        assertEquals(1,result.getEndIndex());
    }

    @Test
    public void testNextBlock4() {
        String s =  "        {asdasdasd}";
        ParseResult result  = CqlUtils.nextBlock(s,'{','}',0);
        assertEquals("{asdasdasd}",result.getString());
        assertEquals(18,result.getEndIndex());
    }

    @Test
    public void testParseValidOidUrl() {
        String url = "urn:oid:2.16.840.1.113883.3.464.1004.1548";
        String result = CqlUtils.parseOid(url);
        assertEquals(result,"2.16.840.1.113883.3.464.1004.1548");
    }

    @Test
    public void testParseInvalidOidUrl() {
        String url = "fooobarrred";
        try {
            String result = CqlUtils.parseOid(url);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            log.warn("IAE",iae);
        }
    }
}
