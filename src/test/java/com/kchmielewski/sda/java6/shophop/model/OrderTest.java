package com.kchmielewski.sda.java6.shophop.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class OrderTest {
    private final Order order = new Order();
    private final Product firstProduct = mock(Product.class);
    private final Product secondProduct = mock(Product.class);

    @Test
    public void addProductThrowsExceptionForNullProduct() {
        assertThatThrownBy(() -> order.addProduct(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addProductThrowsExceptionForZeroQuantity() {
        assertThatThrownBy(() -> order.addProduct(firstProduct, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void checkIfNonExistentProductIsAdded() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.ONE);

        order.addProduct(firstProduct, 5);

        assertThat(order.getProducts()).containsEntry(firstProduct, 5);
    }

    @Test
    public void checkIfExistentProductQuantityIsIncreased() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.ONE);

        order.addProduct(firstProduct, 5);
        order.addProduct(firstProduct);

        assertThat(order.getProducts()).containsEntry(firstProduct, 6);
    }

    @Test
    public void forNewProductTotalPriceEqualsProductPriceTimesQuantity() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.valueOf(3.20));

        order.addProduct(firstProduct, 3);

        assertThat(order.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(9.60));
    }

    @Test
    public void forSameProductAddedTwiceTotalPriceIsIncreased() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.valueOf(3.20));

        order.addProduct(firstProduct, 3);
        order.addProduct(firstProduct, 3);

        assertThat(order.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(19.20));
    }

    @Test
    public void forTwoDifferentProductsTotalPriceEqualsQuantityTimesTheirPrice() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.valueOf(3.20));
        given(secondProduct.getPrice()).willReturn(BigDecimal.valueOf(4.20));

        order.addProduct(firstProduct, 2);
        order.addProduct(secondProduct, 2);

        assertThat(order.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(14.80));
    }

    @Test
    public void checkIfPromotionIsApplied() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.valueOf(3.20));

        order.addProduct(firstProduct, 3);
        BigDecimal totalPrice = order.recalculateWithPromotion(order -> order.getTotalPrice().divide(BigDecimal.valueOf(2), BigDecimal.ROUND_UP));

        assertThat(totalPrice).isEqualByComparingTo(BigDecimal.valueOf(4.80));
    }
}