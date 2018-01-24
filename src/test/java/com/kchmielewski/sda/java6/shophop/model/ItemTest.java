package com.kchmielewski.sda.java6.shophop.model;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ItemTest {

    @Test
    public void shouldThrowExceptionForInvalidParameters() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> new Item(null, BigDecimal.ONE)).isInstanceOf(IllegalArgumentException.class);
        softly.assertThatThrownBy(() -> new Item("   ", BigDecimal.ONE)).isInstanceOf(IllegalArgumentException.class);
        softly.assertThatThrownBy(() -> new Item("ABC", null)).isInstanceOf(IllegalArgumentException.class);
        softly.assertThatThrownBy(() -> new Item("ABC", BigDecimal.ZERO)).isInstanceOf(IllegalArgumentException.class);
        softly.assertAll();
    }
}