package kaiyi.app.eccs.test;

import kaiyi.app.eccs.CurrencyUtils;
import kaiyi.puer.commons.data.Currency;
import org.junit.Test;

public class CompupterTest {

    @Test
    public void rate(){
        System.out.println(CurrencyUtils.computerPercentage(15,Currency.parseForNoDecimalPoint(10000)).toString());
    }
}
