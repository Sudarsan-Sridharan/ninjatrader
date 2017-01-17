define(["d3", "require", "./abstractcomponent"], function(d3, require) {

    var AbstractComponent = require("./abstractcomponent");

    var _componentName = "priceCursor";
    var _lineWidth = 9999999999999;

    function PriceCursor(config) {
        AbstractComponent.call(this, config, null, _componentName);

        this.line = this.getMain().append("line").attr("x2", _lineWidth);
    }

    PriceCursor.prototype = Object.create(AbstractComponent.prototype);
    PriceCursor.prototype.constructor = PriceCursor;

    PriceCursor.prototype.show = function() {
    };

    PriceCursor.prototype.onMouseMove = function(coordinates) {
        this.getMain().attr("transform", "translate(0," + coordinates[1] + ")");
    };

    PriceCursor.prototype.onZoom = function() {
    };

    return PriceCursor;
});