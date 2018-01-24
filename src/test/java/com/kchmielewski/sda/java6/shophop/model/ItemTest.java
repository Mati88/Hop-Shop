package com.kchmielewski.sda.java6.shophop.model;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.math.BigDecimal;

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

    @Test
    public void shouldNotThrowExceptionForValidParameters() {
        Item item = new Item("Item name", BigDecimal.valueOf(12.5));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(item.getName()).isEqualTo("Item name");
        softly.assertThat(item.getPrice()).isEqualTo(BigDecimal.valueOf(12.5));
        softly.assertAll();
    }
}