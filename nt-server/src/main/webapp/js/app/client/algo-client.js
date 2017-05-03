define(['jquery'], function ($) {

    var ajaxUrl = context.serviceHost + "/algorithms/";

    var cache = {};
    cache.ids = [];

    function AlgoClient() {
    }

    AlgoClient.getAll = function() {
        return $.get(ajaxUrl);
    };

    AlgoClient.getAll = function(callback) {
        if (!cache.all) {
            $.get(ajaxUrl).done(function(algorithms) {
                cache.all = algorithms;
                callback(cache.all);
            });
        } else {
            callback(cache.all);
        }
    };

    AlgoClient.getById = function(algoId) {
        return $.get(ajaxUrl + algoId);
    };

    AlgoClient.save = function(jsonObj) {
        return $.ajax({
            url: ajaxUrl,
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(jsonObj)
        });
    };

    AlgoClient.delete = function(algoId) {
        return $.ajax({
            url: ajaxUrl + algoId,
            type: 'DELETE',
            contentType: 'application/json',
            dataType: 'json'
        });
    };

    return AlgoClient;
});
