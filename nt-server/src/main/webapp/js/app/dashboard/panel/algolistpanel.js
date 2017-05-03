define(['jquery', 'require', './basicpanel', 'app/client/algo-client'], function ($, require, BasicPanel) {

    var AlgoClient = require("app/client/algo-client");

    function AlgoListPanel(status) {
        BasicPanel.call(this, "Algorithms", "algoListPanel");
        this.status = status;
        this.load();
    }

    AlgoListPanel.prototype = Object.create(BasicPanel.prototype);
    AlgoListPanel.prototype.constructor = AlgoListPanel;

    AlgoListPanel.prototype.load = function() {
        var that = this;
        var content = this.content;

        AlgoClient.getAll(function(algorithms) {
            content.html("");
            for (var i in algorithms) {
                var algorithm = algorithms[i];
                var itemContainer = $('<div class="algoItem"></div>');
                var link = $('<a href="algorithm?algoId=' + algorithm.algorithmId + '" target="_blank">' + algorithm.description + '</a>');
                var deleteBtn = $('<a class="deleteAlgo" href="#">X</a>').attr("algoId", algorithm.algorithmId).attr("algoDesc", algorithm.description);
                itemContainer.append(link).append(deleteBtn);
                content.append(itemContainer);
            }

            var item = $('<div class="algoItem"><a href="algorithm" target="_blank">Create New Algorithm</div>');
            content.append(item);

            that._initDeleteBtns();
        });
    };

    AlgoListPanel.prototype._initDeleteBtns = function() {
        var status = this.status;
        this.content.find(".deleteAlgo").click(function() {
            var deleteBtn = $(this);
            var algoDesc = deleteBtn.attr("algoDesc");
            var algoId = deleteBtn.attr("algoId");
            var algoItem = deleteBtn.parent();

            if (confirm("Delete Algorithm " + algoDesc + "?")) {
                var statusMsg = "Deleting algorithm: " + algoDesc + "...";
                var statusItem = status.show(statusMsg);
                AlgoClient.delete(algoId).done(function() {
                    algoItem.remove();
                    statusItem.quickShow(statusMsg + " Success!")
                });
            }
        });
    }

    return AlgoListPanel;
});
