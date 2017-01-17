define(["d3"], function(d3) {
    function AbstractComponent(config, panel, name) {
        this.config = config;
        this.panel = panel;
        this.main = d3.select(document.createElementNS(d3.namespaces.svg, 'g'))
            .classed(name, true);
    }

    AbstractComponent.prototype.show = function() {

    };

    AbstractComponent.prototype.onMouseMove = function(coords) {

    };

    AbstractComponent.prototype.onZoom = function(source) {

    };

    AbstractComponent.prototype.onChartWidthChange = function(width) {

    };

    AbstractComponent.prototype.getNode = function() {
        return this.main.node();
    };

    AbstractComponent.prototype.getConfig = function() {
        return this.config;
    };

    AbstractComponent.prototype.getMain = function() {
        return this.main;
    };

    return AbstractComponent;
});