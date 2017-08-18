define(['jquery', 'app/auth/token-auth'], function ($, TokenAuth) {

    var ajaxUrl = context.serviceHost + "/algorithms/";

    var cache = {};
    cache.ids = [];

    function AlgoClient() {
    }

    AlgoClient.getAll = function(callback) {
        if (!cache.all) {
            $.ajax({
                url: ajaxUrl,
                type: 'GET',
                contentType: 'application/json',
                beforeSend: TokenAuth.addAuthHeaders
            }).done(function(algorithms) {
                cache.all = algorithms;
                callback(cache.all);
            });
        } else {
            callback(cache.all);
        }
    };

    AlgoClient.getById = function(algoId) {
        return $.ajax({
            url: ajaxUrl + algoId,
            type: 'GET',
            contentType: 'application/json',
            beforeSend: TokenAuth.addAuthHeaders
        })
    };

    AlgoClient.save = function(jsonObj) {
        return $.ajax({
            url: ajaxUrl,
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(jsonObj),
            beforeSend: TokenAuth.addAuthHeaders
        });
    };

    AlgoClient.delete = function(algoId) {
        return $.ajax({
            url: ajaxUrl + algoId,
            type: 'DELETE',
            contentType: 'application/json',
            dataType: 'json',
            beforeSend: TokenAuth.addAuthHeaders
        });
    };

    return AlgoClient;
});
