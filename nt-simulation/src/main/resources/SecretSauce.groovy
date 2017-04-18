import com.bn.ninjatrader.simulation.data.BarData
import com.bn.ninjatrader.simulation.model.Mark
import com.bn.ninjatrader.simulation.model.Marker
import groovy.transform.Field

@Field pullbackSensitivity = 0.005;
@Field maxBuyRisk = 0.10;
@Field pullback = 0;
@Field trailingStop = 0;
@Field pip;
@Field takeProfitFlag = false;

def onSimulationStart() {
    pip = 0;
    pullback = 0;
    trailingStop = 0;
    takeProfitFlag = false;
}

def processBar() {

    pullbackHandler();

    // Buy low risk entry
    buyHandler();

    sellHandler();

    takeProfitHandler();
}

def pullbackHandler() {
    def twoBarsAgo = $HISTORY.getNBarsAgo(2);
    def threeBarsAgo = $HISTORY.getNBarsAgo(3);
    def fourBarsAgo = $HISTORY.getNBarsAgo(4);
    def sixBarsAgo = $HISTORY.getNBarsAgo(6);

    if (!sixBarsAgo.isPresent()) {
        return;
    }

    def lowOf2BarsAgo = twoBarsAgo.get().get2('$PRICE_LOW');
    def lowestOf4Bars = lowest(['$PRICE_LOW'], 4, 0);
    def pcntDiffFromCurrentLow = Math.abs($PRICE_LOW - lowOf2BarsAgo) / $PRICE_LOW;

    if (lowOf2BarsAgo == lowestOf4Bars // 2 bars ago is the lowest
            && lowOf2BarsAgo < fourBarsAgo.get().get2('$PRICE_LOW') // and it's lower than 4 bars ago.
            && pcntDiffFromCurrentLow >= pullbackSensitivity) {

        def lowOf3BarsAgo = threeBarsAgo.get().get2('$PRICE_LOW')
        def lowOf4BarsAgo = fourBarsAgo.get().get2('$PRICE_LOW')
        def lowOf6BarsAgo = sixBarsAgo.get().get2('$PRICE_LOW')

        if (Math.abs(lowOf3BarsAgo - lowOf2BarsAgo) / lowOf3BarsAgo >= pullbackSensitivity
                || Math.abs(lowOf4BarsAgo - lowOf2BarsAgo) / lowOf4BarsAgo >= pullbackSensitivity
                || Math.abs(lowOf6BarsAgo - lowOf2BarsAgo) / lowOf6BarsAgo >= pullbackSensitivity) {

            pullback = lowestOf4Bars;

            if (pullback > trailingStop) {
                trailingStop = pullback;
            }

            $MARKERS.add(Mark.onDate($DATE).withColor("blue").withMarker(Marker.ARROW_BOTTOM))
        }
    }
}

def buyHandler() {
    if (!$HISTORY.getNBarsAgo(6).isPresent() || $PRICE_CLOSE <= $EMA18 || $EMA18 <= 0) {
        return;
    }

    pip = $BOARDLOT.getBoardLot($PRICE_CLOSE).getTick();
    def buyEntryPrice = $PRICE_HIGH + (2 * pip);
    def cutloss = lowest(['$PRICE_LOW'], 6, 0) ;
    def buyEntryRisk = Math.abs(buyEntryPrice - cutloss) / buyEntryPrice;
    if (buyEntryRisk <= maxBuyRisk) {
        if ($PORTFOLIO.isEmpty() && !$BROKER.hasPendingOrder()) {
            $BROKER.buyOrder().withSymbol($SYMBOL)
                    .withCashAmount($ACCOUNT.liquidCash)
                    .withBarsFromNow(1)
                    .withExpireAfterNumOfBars(1)
                    .atPrice(buyEntryPrice)
                    .submit();

            def emaDeviation = Math.abs(buyEntryPrice - $EMA18) / buyEntryPrice;

            if (emaDeviation >= 0.16) {
                takeProfitFlag = false;
            } else {
                takeProfitFlag = true;
            }
        }
    }
}

def sellHandler() {
    if ($PORTFOLIO.isEmpty()) {
        return;
    }

    pip = $BOARDLOT.getBoardLot($PRICE_CLOSE).getTick();
    def cutloss = trailingStop - (2 * pip);

    if ($PRICE_LOW < cutloss) {
        def totalSharesToSell = $ACCOUNT.portfolio.getTotalShares($SYMBOL);

        if ($PORTFOLIO.canCommitShares($SYMBOL, totalSharesToSell)) {
            $PORTFOLIO.commitShares($SYMBOL, totalSharesToSell);

            // If Gap down
            if ($PRICE_HIGH < cutloss) {
                $BROKER.sellOrder()
                        .withSymbol($SYMBOL)
                        .atClose()
                        .submit();
            } else {
                $BROKER.sellOrder()
                        .withSymbol($SYMBOL)
                        .withExpireAfterNumOfBars(2)
                        .atPrice(cutloss)
                        .submit();
            }
        }
    }
}

def takeProfitHandler() {
    if (!takeProfitFlag || $PRICE_HIGH <= $EMA18 || $ACCOUNT.portfolio.isEmpty()) {
        return;
    }

    def pcntDeviation = ($PRICE_HIGH - $EMA18) / $EMA18;

    if (pcntDeviation >= 0.16) {
        def totalSharesToSell = $PORTFOLIO.getTotalShares($SYMBOL);
        if ($PORTFOLIO.canCommitShares($SYMBOL, totalSharesToSell)) {
            $PORTFOLIO.commitShares($SYMBOL, totalSharesToSell);

            $BROKER.sellOrder()
                    .withSymbol($SYMBOL)
                    .withExpireAfterNumOfBars(2)
                    .atPrice(Math.max($PRICE_LOW, $EMA18 * 1.16))
                    .submit();
        }
    }
}

def onBuyFulfilled() {
    trailingStop = lowest(['$PRICE_LOW'], 6, 0);
}

def onSellFulfilled() {

}

def lowest(variables, fromBarsAgo, toBarsAgo) {
    double lowest = Double.MAX_VALUE;

    int from = Math.min(fromBarsAgo, toBarsAgo);
    int to = Math.max(fromBarsAgo, toBarsAgo);

    for (int i=from; i <= to; i++) {
        final Optional<BarData> pastBarData = $HISTORY.getNBarsAgo(i);
        if (pastBarData.isPresent()) {
            for (String var : variables) {
                lowest = Math.min(lowest, pastBarData.get().get2(var))
            }
        }
    }
    return lowest;
}

def onSimulationEnd() {}
