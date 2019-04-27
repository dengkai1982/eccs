package kaiyi.app.eccs;

import kaiyi.puer.commons.data.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtils {

    /**
     * 计算百分比
     * @param rate 百分比
     * @param total 总数
     * @return
     */
    public static Currency computerPercentage(Number rate, Number total){
        int scale=2;
        BigDecimal rateDecimal=new BigDecimal(rate.toString()).setScale(scale,RoundingMode.DOWN);
        rateDecimal=rateDecimal.divide(new BigDecimal(100)).setScale(scale,RoundingMode.DOWN);
        BigDecimal totalDecimal=new BigDecimal(total.toString()).setScale(scale,RoundingMode.DOWN);
        totalDecimal=totalDecimal.multiply(rateDecimal);
        return new Currency(totalDecimal);
        //return Currency.build(totalDecimal.toString(),scale);
        /*
        BigDecimal numberDecimal=new BigDecimal(number.toString()).setScale(scale,RoundingMode.DOWN);;
        BigDecimal totalDecimal=new BigDecimal(total.toString()).setScale(scale,RoundingMode.DOWN);;
        BigDecimal result=numberDecimal.divide(totalDecimal,scale,BigDecimal.ROUND_DOWN);
        return result.multiply(new BigDecimal(100)).intValue();*/
    }
}
