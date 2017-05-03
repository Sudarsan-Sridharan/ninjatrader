define(["app/util/boardlot"], function(BoardLot) {

    function Format() {
    }

    Format.price = function(priceValue) {
        var decimalPlaces = BoardLot.getDecimalPlaces(priceValue);
        return +priceValue.toFixed(decimalPlaces);
    };

    return Format;
});