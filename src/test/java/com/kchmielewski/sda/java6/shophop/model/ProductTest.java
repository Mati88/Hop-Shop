package com.kchmielewski.sda.java6.shophop.model;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.math.BigDecimal;

public class ProductTest {

    @Test
    public void shouldThrowExceptionForInvalidParameters() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> new Product(null, BigDecimal.ONE)).isInstanceOf(IllegalArgumentException.class);
        softly.assertThatThrownBy(() -> new Product("   ", BigDecimal.ONE)).isInstanceOf(IllegalArgumentException.class);
        softly.assertThatThrownBy(() -> new Product("ABC", null)).isInstanceOf(IllegalArgumentException.class);
        softly.assertThatThrownBy(() -> new Product("ABC", BigDecimal.ZERO)).isInstanceOf(IllegalArgumentException.class);
        softly.assertAll();
    }

    @Test
    public void shouldNotThrowExceptionForValidParameters() {
        Product item = new Product("Item name", BigDecimal.valueOf(12.5));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(item.getName()).isEqualTo("Item name");
        softly.assertThat(item.getPrice()).isEqualTo(BigDecimal.valueOf(12.5));
        softly.assertAll();
    }
}