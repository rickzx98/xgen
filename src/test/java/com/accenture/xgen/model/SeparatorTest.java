package com.accenture.xgen.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SeparatorTest {

    @Test
    public void shouldDoubleBackslash() {
        Separator separator = new Separator("\\");
        Assert.assertEquals("\\\\", separator.toString());
        separator = new Separator("\\udsd");
        Assert.assertEquals("\\\\udsd", separator.toString());
    }
}
