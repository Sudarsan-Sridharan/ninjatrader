define(['jquery', 'require', './basicpanel', 'app/client/admin-client', 'app/util/textinput'],
    function ($, require, BasicPanel) {

    var AdminClient = require("app/client/admin-client");
    var TextInput = require("app/util/textinput");

    function AdminPanel(status) {
        BasicPanel.call(this, "Admin", "adminPanel");

        this.status = status;

        // Import Quotes
        this.importContainer = $("<div></div>").addClass("form");
        this.importDateField = TextInput.builder().label("Date").size(8).placeholder("yyyyMMdd").addClass("date").build();
        this.importBtn = $("<button>Import Quotes</button>");
        this.importContainer.append(this.importDateField).append(this.importBtn);

        // Price Adjustment
        this.priceAdjContainer = $("<div></div>").addClass("form");
        this.priceAdjSymbolField = TextInput.builder().label("Symbol").size(8).placeholder("ABC").addClass("symbol").build();
        this.priceAdjFromField = TextInput.builder().label("From").size(10).placeholder("yyyyMMdd").addClass("from").build();
        this.priceAdjToField = TextInput.builder().label("To").size(10).placeholder("yyyyMMdd").addClass("to").build();
        this.priceAdjScriptField = TextInput.builder().label("Script").size(30).placeholder("$PRICE / 10").addClass("script").build();
        this.priceAdjBtn = $('<button>Adjust Prices</button>');
        this.priceAdjContainer.append(this.priceAdjSymbolField)
            .append(this.priceAdjFromField)
            .append(this.priceAdjToField)
            .append(this.priceAdjScriptField)
            .append(this.priceAdjBtn)

        // Rename Stock Symbol
        this.renameSymbolContainer = $("<div></div>").addClass("form");
        this.renameSymbolFromField = TextInput.builder().label("From").size(8).placeholder("ABC").addClass("from").build();
        this.renameSymbolToField = TextInput.builder().label("To").size(8).placeholder("ABC").addClass("to").build();
        this.renameSymbolBtn = $('<button>Rename Symbol</button>');
        this.renameSymbolContainer.append(this.renameSymbolFromField)
            .append(this.renameSymbolToField)
            .append(this.renameSymbolBtn)

        this.content.append(this.importContainer)
            .append(this.priceAdjContainer)
            .append(this.renameSymbolContainer);

        this.init();
    }

    AdminPanel.prototype = Object.create(BasicPanel.prototype);
    AdminPanel.prototype.constructor = AdminPanel;

    AdminPanel.prototype.init = function() {
        var that = this;
        var status = this.status;
        var importBtn = this.importBtn;

        // Import Quotes Button
        this.importBtn.click(function() {
            $(this).prop("disabled", true);

            var statusItem = status.show("Importing quotes...");

            var importDate = that.importDateField.input.val();

            AdminClient.importQuotes(importDate).done(function() {
                statusItem.quickShow("Successfully imported quotes.");
            }).fail(function(e) {
                console.log(e)
                statusItem.show("Failed to import quotes: " + e.statusText);
            }).always(function() {
                importBtn.prop("disabled", false);
            });
        });

        // Price Adjustment Button
        this.priceAdjBtn.click(function() {
            var statusItem = status.show("Adjusting prices...");

            AdminClient.adjustPrices(that.priceAdjSymbolField.input.val(),
                that.priceAdjFromField.input.val(),
                that.priceAdjToField.input.val(),
                that.priceAdjScriptField.input.val()
            ).success(function() {
                statusItem.quickShow("Adjusting prices... Success!");
            }).fail(function(xhr, status, error) {
                statusItem.show("Adjusting prices... Failed! " + xhr.responseText);
            });
        });

        // Rename Stock Symbol Button
        this.renameSymbolBtn.click(function() {
            var statusItem = status.show("Renaming stock symbol...");
            var from = that.renameSymbolFromField.input.val();
            var to = that.renameSymbolToField.input.val();

            AdminClient.renameStockSymbol(from, to)
                .success(function() {
                    statusItem.quickShow("Renaming stock symbol... Success!");
                })
                .fail(function(xhr, status, error) {
                    statusItem.show("Renaming stock symbol... Failed! " + xhr.responseText);
                });
        });
    };

    return AdminPanel;
});
