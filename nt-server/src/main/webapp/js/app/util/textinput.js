define(['jquery', 'require'], function ($, require) {

    TextInput.builder = function() {
        return new TextInput();
    }

    /**
     * @constructor
     */
    function TextInput() {
        this.container = $("<div></div>").addClass("field");
        this.label;
        this.input = $('<input type="text">');
    }

    TextInput.prototype.name = function(name) {
        this.input.attr("name", name);
        return this;
    }

    TextInput.prototype.label = function(label) {
        this.label = $("<label>" + label + "</label>");
        return this;
    };;

    TextInput.prototype.size = function(size) {
        this.input.attr("size", size);
        return this;
    };

    TextInput.prototype.maxlength = function(maxlength) {
        this.input.attr("maxlength", maxlength);
        return this;
    };

    TextInput.prototype.placeholder = function(placeholder) {
        this.input.attr("placeholder", placeholder);
        return this;
    };

    TextInput.prototype.addClass = function(className) {
        this.container.addClass(className);
        return this;
    };

    TextInput.prototype.build = function() {
        this.container.html("");
        if (this.label) {
            this.container.append(this.label);
        }
        this.container.append(this.input);

        this.container.input = this.input;
        this.container.label = this.label;
        return this.container;
    };

    return TextInput;
});
