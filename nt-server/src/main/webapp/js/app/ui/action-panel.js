define(['jquery', 'require'], function() {

    function ActionPanel(containerId) {
        this._container = $(containerId).addClass("actionPanel");
        this._wrapper = $("<div></div>").addClass("actionWrapper");
        this._title = $("<div></div>").addClass("title");
        this._body = $("<div></div>").addClass("body");

        this._wrapper.append(this._title);
        this._wrapper.append(this._body);
        this._container.append(this._wrapper);
    }

    ActionPanel.prototype.show = function() {
        this._container.addClass("show");
        return this;
    }

    ActionPanel.prototype.hide = function() {
        this._container.removeClass("show");
        return this;
    }

    ActionPanel.prototype.toggle = function() {
        this._container.toggleClass("show");
        return this;
    }

    ActionPanel.prototype.title = function(title) {
        this._title.html(title);
        return this;
    }

    ActionPanel.prototype.body = function(node) {
        this._body.empty().append(node);
        return this;
    }

    return ActionPanel;
});