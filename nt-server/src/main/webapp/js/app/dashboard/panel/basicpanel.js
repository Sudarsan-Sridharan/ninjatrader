define(['jquery'], function ($) {

    function BasicPanel(title, className) {
        this.container = $("<div></div>").addClass("panel").addClass(className);
        this.panelWrap = $("<div></div>").addClass("panelWrap");
        this.header = $("<div></div>").addClass("panelHeader");
        this.title = $("<h3></h3>").addClass("panelTitle").html(title);
        this.content = $("<div></div>").addClass("panelContent");

        this.container.append(this.panelWrap);
        this.panelWrap.append(this.header).append(this.content);
        this.header.append(this.title);
    }

    BasicPanel.prototype.node = function() {
        return this.container;
    };

    return BasicPanel;
});
