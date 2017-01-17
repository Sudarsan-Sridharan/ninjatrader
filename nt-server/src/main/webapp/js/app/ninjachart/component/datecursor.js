define(["d3", "require", "./abstractcomponent"], function(d3, require) {

    var AbstractComponent = require("./abstractcomponent");

    var _componentName = "dateCursor";

    function DateCursor(config) {
        AbstractComponent.call(this, config, null, _componentName);

        this.getMain().append("line")
            .attr("y2", config.chartHeight);
    }

    DateCursor.prototype = Object.create(AbstractComponent.prototype);
    DateCursor.prototype.constructor = DateCursor;

    DateCursor.prototype.onMouseMove = function(coords) {
        if (!coords) return;
        var index = Math.floor(this.getConfig().xByIndex.invert(coords[0]));
        this.moveToIndex(index);
    };

    DateCursor.prototype.moveToIndex = function(index) {
        var config = this.getConfig();
        var x = config.xByIndex(index) + config.columnWidth / 2;
        this.getMain().attr("transform", "translate(" + x + ",0)");
    };
    
    return DateCursor;
});