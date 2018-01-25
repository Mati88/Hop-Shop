package com.kchmielewski.sda.java6.shophop.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.PriorityQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class OrderTest {
    private final Order order = new Order();
    private final Product firstProduct = mock(Product.class);
    private final Product secondProduct = mock(Product.class);
    private final PriorityQueue<Promotion> promotions = new PriorityQueue<>();
    private final Promotion tenPercentDiscount = mock(Promotion.class);
    private final Promotion fiveUnitsPriceDiscount = mock(Promotion.class);

    @Before
    public void setUp() {
        given(tenPercentDiscount.isApplicable(order)).willReturn(true);
        given(fiveUnitsPriceDiscount.isApplicable(order)).willReturn(true);
        given(tenPercentDiscount.compareTo(fiveUnitsPriceDiscount)).willReturn(-1);
        given(fiveUnitsPriceDiscount.compareTo(tenPercentDiscount)).willReturn(1);
    }

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
    public void forNoPromotionsPriceIsEqualToTotalPrice() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.valueOf(3.20));

        order.addProduct(firstProduct, 3);

        assertThat(order.recalculateWithPromotions(promotions)).isEqualByComparingTo(order.getTotalPrice());
//        assertThat(order.recalculateWithPromotions(promotions)).isEqualByComparingTo(BigDecimal.valueOf(9.60));
    }

    @Test
    public void forOnePromotionThePriceIsDiscounted() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.valueOf(3.20));

        order.addProduct(firstProduct, 3);
        given(tenPercentDiscount.apply(order)).willReturn(order.getTotalPrice().multiply(BigDecimal.valueOf(0.9)));

        promotions.add(tenPercentDiscount);

        assertThat(order.recalculateWithPromotions(promotions)).isEqualByComparingTo(BigDecimal.valueOf(8.64));
    }

    //TODO: clean up mess with given...when...then sections
    @Test
    public void forTwoPromotionsTheOneWithHigherPriorityIsAppliedFirst() {
        given(firstProduct.getPrice()).willReturn(BigDecimal.valueOf(3.20));

        order.addProduct(firstProduct, 3);
        given(tenPercentDiscount.apply(order)).willReturn(order.getTotalPrice().multiply(BigDecimal.valueOf(0.9)));
        BigDecimal temp = tenPercentDiscount.apply(order);
        given(fiveUnitsPriceDiscount.apply(order)).willReturn(temp.subtract(BigDecimal.valueOf(5)));

        promotions.add(tenPercentDiscount);
        promotions.add(fiveUnitsPriceDiscount);

        assertThat(order.recalculateWithPromotions(promotions)).isEqualByComparingTo(BigDecimal.valueOf(3.64));
    }
}