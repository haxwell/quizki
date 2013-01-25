package com.haxwell.apps.questions;

import org.junit.*;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class QuizkiTest {

    @Before
    public void prepare() {
        setBaseUrl("http://localhost:8080/");
    }

    @Test
    public void testLogin() {
        beginAt("index.jsp");
        assertTitleEquals("Home Page - Quizki");
    }	
}
