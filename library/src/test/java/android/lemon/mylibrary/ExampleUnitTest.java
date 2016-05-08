package android.lemon.mylibrary;

import android.lemon.assist.Check;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 3);
    }

    @Test
    public void checkChinese() throws Exception {
        assertTrue(Check.isChinese("大帅哥"));
        assertFalse(Check.isChinese("bbbbb"));
        assertTrue(Check.isChinese("大帅哥"));
        assertFalse(Check.isChinese("bbb大帅哥"));

        assertTrue(Check.isEmail("243107006@qq.com"));
        assertFalse(Check.isEmail("243107006@eeeee"));
        assertFalse(Check.isIDNum("243107006@eeeee"));
        assertTrue(Check.isIDNum("350521199307156537"));
    }


}