package com.accenture.xgen.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SeparatorTest {


    @Test
    public void shouldAcceptUnicode() {
        Separator separator = new Separator('\u25B2');
        Assert.assertEquals(String.valueOf('\u25B2'), separator.toString());
    }

    @Test
    public void shouldAcceptCharsetFlagInConstructor() {
        Separator separator = new Separator("\\u25B2", "y");
        Assert.assertEquals("\\\\u25B2", separator.toString());
    }
}
