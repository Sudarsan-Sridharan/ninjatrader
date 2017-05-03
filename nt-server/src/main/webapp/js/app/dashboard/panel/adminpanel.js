define(['jquery', 'require', './basicpanel', 'app/client/admin-client', 'app/util/textinput'],
    function ($, require, BasicPanel) {

    var AdminClient = require("app/client/admin-client");
    var TextInput = require("app/util/textinput");

    function AdminPanel(status) {
        BasicPanel.call(this, "Admin", "adminPanel");

        this.importBtn = $("<button>Import Quotes</button>");

        this.priceAdjContainer = $("<div></div>").addClass("priceAdj");
        this.priceAdjSymbolField = TextInput.builder().label("Symbol").size(8).placeholder("ABC").addClass("symbol").build();
        this.priceAdjFromField = TextInput.builder().label("From").size(10).placeholder("yyyyMMdd").addClass("from").build();
        this.priceAdjToField = TextInput.builder().label("To").size(10).placeholder("yyyyMMdd").addClass("to").build();
        this.priceAdjScriptField = TextInput.builder().label("Script").size(30).placeholder("$PRICE / 10").addClass("script").build();
        this.priceAdjBtn = $('<button>Adjust Prices</button>');

        this.status = status;

        this.priceAdjContainer.append(this.priceAdjSymbolField)
            .append(this.priceAdjFromField)
            .append(this.priceAdjToField)
            .append(this.priceAdjScriptField)
            .append(this.priceAdjBtn)

        this.content.append(this.importBtn).append(this.priceAdjContainer);

        this.init();
    }

    AdminPanel.prototype = Object.create(BasicPanel.prototype);
    AdminPanel.prototype.constructor = AdminPanel;

    AdminPanel.prototype.init = function() {
        var that = this;
        var status = this.status;
        var importBtn = this.importBtn;

        this.importBtn.click(function() {
            $(this).prop("disabled", true);

            var statusItem = status.show("Importing quotes...");

            AdminClient.importQuotes().done(function() {
                statusItem.msg("Successfully imported quotes.").quickShow();
            }).fail(function(e) {
                statusItem.msg("Failed to import quotes: " + e.statusText).quickShow();
            }).always(function() {
                importBtn.prop("disabled", false);
            });
        });

        this.priceAdjBtn.click(function() {
            var statusItem = status.show("Adjusting prices...");

            AdminClient.adjustPrices(that.priceAdjSymbolField.input.val(),
                that.priceAdjFromField.input.val(),
                that.priceAdjToField.input.val(),
                that.priceAdjScriptField.input.val()
            ).success(function() {
                statusItem.quickShow("Adjusting prices... Success!");
            }).fail(function(xhr, status, error) {
                statusItem.quickShow("Adjusting prices... Failed! " + xhr.responseText);
            });
        });
    };

    return AdminPanel;
});
