package mat.cql;

import org.junit.Test;

import static mat.cql.CqlStringUtils.nextQuotedString;
import static mat.cql.CqlStringUtils.nextTickedString;
import static org.junit.Assert.assertEquals;

public class TestCqlStringUtils {

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
    public void restRemoveBlockComments() {
        String s = "Simple\nBlock comment\nTest\n  FOO/*  sldjfksldkfjslkdfjsldkfjs\n\n\n\n\n\n\n\n slkdjrslkdfjsldkfj*/\n END";
        String clean = CqlStringUtils.removeCqlBlockComments(s);
        assertEquals("Simple\nBlock comment\nTest\n  FOO\n END",clean);
    }

    @Test
    public void restRemoveBlockComments2() {
        String s = "/*S*/imple\n/*Block comment*/\nTest\n  FOO/*  sldjfksldkfjslkdfjsldkfjs\n\n\n\n\n\n\n\n slkdjrslkdfjsldkfj*/\n EN/*D*/";
        String clean = CqlStringUtils.removeCqlBlockComments(s);
        assertEquals("imple\n\nTest\n  FOO\n EN",clean);
    }

    @Test
    public void removeLineComments() {
        String s = "This is a bunch \nof text spread\n out on a bunch\nof //NARF\n lines";
        String clean = CqlStringUtils.removeLineComments(s);
        assertEquals("This is a bunch \nof text spread\n out on a bunch\nof \n lines",clean);
    }

    @Test
    public void removeLineComments2() {
        String s = "This is a bunch \nof text spread\n out on a bunch\nof //NARF    //     asd\n lines";
        String clean = CqlStringUtils.removeLineComments(s);
        assertEquals("This is a bunch \nof text spread\n out on a bunch\nof \n lines",clean);
    }
}
