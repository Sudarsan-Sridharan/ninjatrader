define(['jquery', 'require', './basicpanel', 'app/client/algoclient'], function ($, require, BasicPanel) {

    var AlgoClient = require("app/client/algoclient");

    function AlgoListPanel() {
        BasicPanel.call(this, "Algorithms", "algoListPanel");
        this.load();
    }

    AlgoListPanel.prototype = Object.create(BasicPanel.prototype);
    AlgoListPanel.prototype.constructor = AlgoListPanel;

    AlgoListPanel.prototype.load = function() {
        var content = this.content;

        AlgoClient.getAll(function(algorithms) {
            content.html("");
            for (var i in algorithms) {
                var algorithm = algorithms[i];
                var itemContainer = $('<div class="algoItem"></div>');
                var link = $('<a href="algorithm?algoId=' + algorithm.algorithmId + '" target="_blank">' + algorithm.description + '</a>');
                var deleteBtn = $('<a class="deleteAlgo" href="#">X</a>');
                itemContainer.append(link).append(deleteBtn);
                content.append(itemContainer);
            }

            var item = $('<div class="algoItem"><a href="algorithm" target="_blank">Create New Algorithm</div>');
            content.append(item);
        });
    };

    return AlgoListPanel;
});
