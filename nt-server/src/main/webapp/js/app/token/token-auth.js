define(["jquery", "app/token/token-store"], function ($, TokenStore) {

    function TokenAuth() {
    }

    TokenAuth.addAuthHeaders = function(xhr) {
        var token = TokenStore.get();
        xhr.setRequestHeader ("Authorization", "Bearer " + token);
    };

    return TokenAuth;
});
